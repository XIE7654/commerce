package cn.iocoder.yudao.module.amazon.service.listingmarketplace;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.time.LocalDateTime;
import cn.iocoder.yudao.module.amazon.controller.admin.listingmarketplace.vo.*;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listing.AmazonListingDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingattribute.AmazonListingAttributeDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingimage.AmazonListingImageDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingissue.AmazonListingIssueDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingmarketplace.AmazonListingMarketplaceDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingstatus.AmazonListingStatusDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.seller.AmazonShopMarketplaceDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopWithMarketplacesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.amazon.dal.mysql.listing.AmazonListingMapper;
import cn.iocoder.yudao.module.amazon.dal.mysql.listingattribute.AmazonListingAttributeMapper;
import cn.iocoder.yudao.module.amazon.dal.mysql.listingimage.AmazonListingImageMapper;
import cn.iocoder.yudao.module.amazon.dal.mysql.listingissue.AmazonListingIssueMapper;
import cn.iocoder.yudao.module.amazon.dal.mysql.listingmarketplace.AmazonListingMarketplaceMapper;
import cn.iocoder.yudao.module.amazon.dal.mysql.listingstatus.AmazonListingStatusMapper;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.listings.AmazonListingsService;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;
import cn.iocoder.yudao.module.amazon.mq.AmazonListingMarketplaceSyncMessage;
import cn.iocoder.yudao.module.amazon.mq.AmazonListingMarketplaceSyncRabbitMQConfig;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;
import static cn.iocoder.yudao.module.amazon.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.amazon.utils.AmazonDateTimeUtils.parseOrNull;
import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.getList;
import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.getMap;
import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.getString;
import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.toMap;

/**
 * Listing信息表 Service 实现类
 *
 * @author 自达源码
 */
@Service
@Validated
public class AmazonListingMarketplaceServiceImpl implements AmazonListingMarketplaceService {

    @Resource
    private AmazonListingMarketplaceMapper listingMarketplaceMapper;
    @Resource
    private AmazonListingStatusMapper listingStatusMapper;
    @Resource
    private AmazonListingImageMapper listingImageMapper;
    @Resource
    private AmazonListingAttributeMapper listingAttributeMapper;
    @Resource
    private AmazonListingIssueMapper listingIssueMapper;
    @Resource
    private AmazonListingMapper listingMapper;
    @Resource
    private AmazonShopMapper shopMapper;
    @Resource
    private AmazonListingsService amazonListingsService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    /** 将全量同步任务投递到 RabbitMQ，由消费者异步执行。 */
    @Override
    public void enqueueSyncAllAvailableListings() {
        rabbitTemplate.convertAndSend(AmazonListingMarketplaceSyncRabbitMQConfig.EXCHANGE_NAME,
                AmazonListingMarketplaceSyncRabbitMQConfig.ROUTING_KEY,
                new AmazonListingMarketplaceSyncMessage());
    }

    /** {@inheritDoc} */
    @Override
    public AmazonListingMarketplaceSyncRespVO syncAllAvailableListings() {
        AmazonListingMarketplaceSyncRespVO result = new AmazonListingMarketplaceSyncRespVO();
        List<AmazonShopWithMarketplacesDO> shops = shopMapper.selectEnabledWithMarketplaces();
        result.setShopCount(shops.size());
        for (AmazonShopWithMarketplacesDO shopWithMarketplaces : shops) {
            AmazonShopDO shop = shopWithMarketplaces.getShop();
            List<AmazonShopMarketplaceDO> participations = shopWithMarketplaces.getParticipations();
            List<MarketplaceTarget> targets = resolveMarketplaceTargets(shop, participations, result);
            if (targets.isEmpty() && participations.isEmpty()) {
                syncDefaultMarketplace(shop, result);
                continue;
            }
            if (!targets.isEmpty()) {
                syncMarketplaces(shop, targets, result);
            }
        }
        return result;
    }

    /** 从店铺参与记录整理站点，并为缺失国家代码的 Marketplace ID 尝试补全配置。 */
    private List<MarketplaceTarget> resolveMarketplaceTargets(AmazonShopDO shop,
                                                               List<AmazonShopMarketplaceDO> participations,
                                                               AmazonListingMarketplaceSyncRespVO result) {
        Map<String, String> participationCountries = new LinkedHashMap<>();
        for (AmazonShopMarketplaceDO participation : participations) {
            if (StrUtil.isNotBlank(participation.getMarketplaceId())) {
                participationCountries.put(participation.getMarketplaceId(), participation.getCountryCode());
            }
        }
        Set<String> ids = participationCountries.keySet();
        List<MarketplaceTarget> targets = new ArrayList<>();
        for (String id : ids) {
            String countryCode = participationCountries.get(id);
            if (StrUtil.isBlank(countryCode)) {
                AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromMarketplaceId(id);
                countryCode = marketplace == null ? null : marketplace.getCountryCode();
            }
            if (StrUtil.isBlank(countryCode)) {
                result.getFailures().add("店铺 " + shop.getId() + " 的站点 " + id + " 缺少有效国家代码");
            } else {
                targets.add(new MarketplaceTarget(id, countryCode));
            }
        }
        return targets;
    }

    @Override
    public Long createListingMarketplace(AmazonListingMarketplaceSaveReqVO createReqVO) {
        // 插入
        AmazonListingMarketplaceDO listingMarketplace = BeanUtils.toBean(createReqVO, AmazonListingMarketplaceDO.class);
        listingMarketplaceMapper.insert(listingMarketplace);

        // 返回
        return listingMarketplace.getId();
    }

    @Override
    public void updateListingMarketplace(AmazonListingMarketplaceSaveReqVO updateReqVO) {
        // 校验存在
        validateListingMarketplaceExists(updateReqVO.getId());
        // 更新
        AmazonListingMarketplaceDO updateObj = BeanUtils.toBean(updateReqVO, AmazonListingMarketplaceDO.class);
        listingMarketplaceMapper.updateById(updateObj);
    }

    @Override
    public void deleteListingMarketplace(Long id) {
        // 校验存在
        validateListingMarketplaceExists(id);
        // 删除
        listingMarketplaceMapper.deleteById(id);
    }

    @Override
        public void deleteListingMarketplaceListByIds(List<Long> ids) {
        // 删除
        listingMarketplaceMapper.deleteByIds(ids);
        }


    private void validateListingMarketplaceExists(Long id) {
        if (listingMarketplaceMapper.selectById(id) == null) {
            throw exception(LISTING_MARKETPLACE_NOT_EXISTS);
        }
    }

    @Override
    public AmazonListingMarketplaceDO getListingMarketplace(Long id) {
        return listingMarketplaceMapper.selectById(id);
    }

    @Override
    public PageResult<AmazonListingMarketplaceDO> getListingMarketplacePage(AmazonListingMarketplacePageReqVO pageReqVO) {
        return listingMarketplaceMapper.selectPage(pageReqVO);
    }

    /**
     * 在店铺尚未同步参与站点时，使用默认 Marketplace 同步 Listings，保证启用店铺不会被跳过。
     *
     * @param shop 启用的 Amazon 店铺
     * @param result 用于累计同步统计与失败信息
     */
    private void syncDefaultMarketplace(AmazonShopDO shop, AmazonListingMarketplaceSyncRespVO result) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromMarketplaceId(shop.getMarketplaceId());
        if (marketplace == null) {
            result.getFailures().add("店铺 " + shop.getId() + " 缺少有效的默认 Marketplace 配置");
            return;
        }
        syncMarketplaces(shop, List.of(new MarketplaceTarget(marketplace.getMarketplaceId(), marketplace.getCountryCode())), result);
    }

    /**
     * 分页查询一个店铺多个站点的 Listings，并将每页结果保存为本地 Listing 与站点信息。
     *
     * @param shop 启用的 Amazon 店铺
     * @param targets 当前同步的 Amazon Marketplace 及国家代码
     * @param result 用于累计同步统计与失败信息
     */
    private void syncMarketplaces(AmazonShopDO shop, List<MarketplaceTarget> targets,
                                  AmazonListingMarketplaceSyncRespVO result) {
        if (targets.isEmpty()) {
            return;
        }
        // Listings Items 仅支持同一销售区域的多个站点；同店跨区域时按区域拆分请求。
        Map<String, List<MarketplaceTarget>> targetsByRegion = new LinkedHashMap<>();
        for (MarketplaceTarget target : targets) {
            AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromMarketplaceId(target.marketplaceId());
            String salesRegion = marketplace == null ? target.countryCode() : marketplace.getSalesRegion();
            targetsByRegion.computeIfAbsent(salesRegion, ignored -> new ArrayList<>()).add(target);
        }
        for (List<MarketplaceTarget> regionalTargets : targetsByRegion.values()) {
            syncRegionalMarketplaces(shop, regionalTargets, result);
        }
    }

    /** 批量分页同步同一销售区域的站点，确保其可共用同一个 SP-API 端点。 */
    private void syncRegionalMarketplaces(AmazonShopDO shop, List<MarketplaceTarget> targets,
                                          AmazonListingMarketplaceSyncRespVO result) {
        List<String> marketplaceIds = targets.stream().map(MarketplaceTarget::marketplaceId).toList();
        List<String> countryCodes = targets.stream().map(MarketplaceTarget::countryCode).distinct().toList();
        try {
            String pageToken = null;
            Set<String> pageTokens = new HashSet<>();
            do {
                AmazonApiResponse<Map<String, Object>> response = amazonListingsService.searchListingsItems(
                        buildSearchRequest(shop.getId(), countryCodes, pageToken));
                Map<String, Object> payload = response.getData();
                for (Object item : getList(payload, "items")) {
                    saveListingItem(shop.getId(), marketplaceIds, item, result);
                }
                pageToken = getString(payload, "nextPageToken");
            } while (StrUtil.isNotBlank(pageToken) && pageTokens.add(pageToken));
            result.setMarketplaceCount(result.getMarketplaceCount() + targets.size());
        } catch (RuntimeException ex) {
            result.getFailures().add("店铺 " + shop.getId() + " 的站点 " + String.join(",", marketplaceIds)
                    + " 同步失败: " + StrUtil.blankToDefault(ex.getMessage(), ex.getClass().getSimpleName()));
        }
    }

    /**
     * 构造每页 Listings 查询参数，获取站点摘要、属性和问题以支持完整本地归档。
     *
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param pageToken Amazon 分页 Token，首页为 {@code null}
     * @return Listings 查询请求
     */
    private AmazonListingsSearchReqVO buildSearchRequest(Long shopId, List<String> countryCodes, String pageToken) {
        AmazonListingsSearchReqVO request = new AmazonListingsSearchReqVO();
        request.setShopId(shopId);
        request.setCountryCodes(countryCodes);
        request.setIncludedData(List.of("summaries", "attributes", "issues"));
        request.setSortBy("lastUpdatedDate");
        request.setSortOrder("DESC");
        request.setPageSize(20);
        request.setPageToken(pageToken);
        return request;
    }

    /**
     * 保存一个 Amazon Listings Item 的全部 summaries；同一 SKU 在不同站点各保存一条站点记录。
     *
     * @param shopId 店铺编号
     * @param requestedMarketplaceIds 本轮请求的站点，用于过滤异常返回的其它站点数据
     * @param item Amazon Listings Item 原始对象
     * @param result 用于累计已保存记录数
     */
    private void saveListingItem(Long shopId, List<String> requestedMarketplaceIds, Object item,
                                 AmazonListingMarketplaceSyncRespVO result) {
        if (!(item instanceof Map<?, ?> rawItem)) {
            return;
        }
        Map<String, Object> listingItem = toMap(rawItem);
        String sku = getString(listingItem, "sku");
        if (StrUtil.isBlank(sku)) {
            return;
        }
        AmazonListingDO listing = saveListing(shopId, sku);
        for (Object summaryItem : getList(listingItem, "summaries")) {
            if (!(summaryItem instanceof Map<?, ?> rawSummary)) {
                continue;
            }
            Map<String, Object> summary = toMap(rawSummary);
            String marketplaceId = getString(summary, "marketplaceId");
            if (!requestedMarketplaceIds.contains(marketplaceId)) {
                continue;
            }
            AmazonListingMarketplaceDO listingMarketplace = saveListingMarketplace(listing.getId(), marketplaceId, summary);
            saveListingDetails(listingMarketplace.getId(), summary, listingItem);
            result.setListingMarketplaceCount(result.getListingMarketplaceCount() + 1);
        }
    }

    /** 同步目标站点及其所属国家代码。 */
    private record MarketplaceTarget(String marketplaceId, String countryCode) {
    }

    /**
     * 按店铺和 SKU 新增或刷新 Listing 主表的同步时间。
     *
     * @param shopId 店铺编号
     * @param sku Amazon Seller SKU
     * @return 已保存的 Listing 主记录
     */
    private AmazonListingDO saveListing(Long shopId, String sku) {
        LocalDateTime syncTime = LocalDateTime.now();
        AmazonListingDO listing = listingMapper.selectByShopIdAndSku(shopId, sku);
        if (listing == null) {
            listing = AmazonListingDO.builder().shopId(shopId).sku(sku)
                    .firstSyncTime(syncTime).lastSyncTime(syncTime).build();
            listingMapper.insert(listing);
        } else {
            listing.setLastSyncTime(syncTime);
            listingMapper.updateById(listing);
        }
        return listing;
    }

    /**
     * 按 Listing 和 Marketplace 新增或刷新站点信息，保持本地数据与 Amazon summaries 一致。
     *
     * @param listingId Listing 主表编号
     * @param marketplaceId Amazon Marketplace ID
     * @param summary Amazon summary 原始对象
     */
    private AmazonListingMarketplaceDO saveListingMarketplace(Long listingId, String marketplaceId, Map<String, Object> summary) {
        AmazonListingMarketplaceDO record = listingMarketplaceMapper
                .selectByListingIdAndMarketplaceId(listingId, marketplaceId);
        if (record == null) {
            record = new AmazonListingMarketplaceDO();
            record.setListingId(listingId);
            record.setMarketplaceId(marketplaceId);
        }
        record.setAsin(getString(summary, "asin"));
        record.setProductType(getString(summary, "productType"));
        record.setConditionType(getString(summary, "conditionType"));
        record.setItemName(getString(summary, "itemName"));
        record.setAmazonCreatedTime(parseOrNull(getString(summary, "createdDate")));
        record.setAmazonUpdatedTime(parseOrNull(getString(summary, "lastUpdatedDate")));
        record.setLastSyncTime(LocalDateTime.now());
        if (record.getId() == null) {
            listingMarketplaceMapper.insert(record);
        } else {
            listingMarketplaceMapper.updateById(record);
        }
        return record;
    }

    /**
     * 将当前 Listings Item 的明细按站点记录创建或更新；不删除其它店铺和历史同步数据。
     *
     * @param listingMarketplaceId Listing 站点记录编号
     * @param summary 当前站点的摘要数据
     * @param listingItem 当前 SKU 的完整 Listings Item 数据
     */
    private void saveListingDetails(Long listingMarketplaceId, Map<String, Object> summary,
                                    Map<String, Object> listingItem) {
        saveStatuses(listingMarketplaceId, summary);
        saveImages(listingMarketplaceId, summary, getMap(listingItem, "attributes"));
        saveAttributes(listingMarketplaceId, getMap(listingItem, "attributes"));
        saveIssues(listingMarketplaceId, getList(listingItem, "issues"));
    }

    /**
     * 保存摘要中的状态数组。
     *
     * @param listingMarketplaceId Listing 站点记录编号
     * @param summary Amazon summary 原始对象
     */
    private void saveStatuses(Long listingMarketplaceId, Map<String, Object> summary) {
        for (Object value : getList(summary, "status")) {
            String status = value == null ? null : String.valueOf(value);
            if (StrUtil.isNotBlank(status)) {
                AmazonListingStatusDO record = listingStatusMapper
                        .selectByListingMarketplaceIdAndStatus(listingMarketplaceId, status);
                if (record == null) {
                    record = new AmazonListingStatusDO();
                    record.setListingMarketplaceId(listingMarketplaceId);
                    record.setStatus(status);
                    listingStatusMapper.insert(record);
                } else {
                    // 更新审计时间，标识该状态在本轮同步中仍由 Amazon 返回。
                    listingStatusMapper.updateById(record);
                }
            }
        }
    }

    /**
     * 保存 summary 主图及属性中所有图片定位器，属性原值保持独立存储以免丢失 Amazon 扩展字段。
     *
     * @param listingMarketplaceId Listing 站点记录编号
     * @param summary Amazon summary 原始对象
     * @param attributes Amazon attributes 原始对象
     */
    private void saveImages(Long listingMarketplaceId, Map<String, Object> summary, Map<String, Object> attributes) {
        List<AmazonListingImageDO> images = new ArrayList<>();
        Map<String, Object> mainImage = getMap(summary, "mainImage");
        addImage(images, listingMarketplaceId, "MAIN", getString(mainImage, "link"),
                getInteger(mainImage, "width"), getInteger(mainImage, "height"));
        attributes.forEach((name, value) -> {
            if (name.toLowerCase(Locale.ROOT).contains("image")) {
                collectImageUrls(images, listingMarketplaceId, normalizeImageType(name), value);
            }
        });
        for (AmazonListingImageDO image : images) {
            AmazonListingImageDO record = listingImageMapper.selectByListingMarketplaceIdAndTypeAndSortOrder(
                    listingMarketplaceId, image.getImageType(), image.getSortOrder());
            if (record == null) {
                listingImageMapper.insert(image);
            } else {
                record.setImageUrl(image.getImageUrl());
                record.setWidth(image.getWidth());
                record.setHeight(image.getHeight());
                listingImageMapper.updateById(record);
            }
        }
    }

    /**
     * 保存 attributes 的每个顶级字段，值以 JSON 原样保存以兼容不同 product type。
     *
     * @param listingMarketplaceId Listing 站点记录编号
     * @param attributes Amazon attributes 原始对象
     */
    private void saveAttributes(Long listingMarketplaceId, Map<String, Object> attributes) {
        attributes.forEach((name, value) -> {
            AmazonListingAttributeDO record = listingAttributeMapper
                    .selectByListingMarketplaceIdAndAttributeName(listingMarketplaceId, name);
            if (record == null) {
                record = new AmazonListingAttributeDO();
                record.setListingMarketplaceId(listingMarketplaceId);
                record.setAttributeName(name);
            }
            record.setAttributeValue(toJsonString(value));
            if (record.getId() == null) {
                listingAttributeMapper.insert(record);
            } else {
                listingAttributeMapper.updateById(record);
            }
        });
    }

    /**
     * 保存 Listings API 返回的 issues，并保留完整 issue 原始值以承载 Amazon 新增字段。
     *
     * @param listingMarketplaceId Listing 站点记录编号
     * @param issues Amazon issues 原始数组
     */
    private void saveIssues(Long listingMarketplaceId, List<?> issues) {
        for (Object issueItem : issues) {
            if (!(issueItem instanceof Map<?, ?> rawIssue)) {
                continue;
            }
            Map<String, Object> issue = toMap(rawIssue);
            String issueCode = getString(issue, "code");
            String severity = getString(issue, "severity");
            String message = getString(issue, "message");
            AmazonListingIssueDO record = listingIssueMapper.selectByUniqueFields(listingMarketplaceId, issueCode,
                    severity, message);
            if (record == null) {
                record = new AmazonListingIssueDO();
                record.setListingMarketplaceId(listingMarketplaceId);
                record.setIssueCode(issueCode);
                record.setSeverity(severity);
                record.setMessage(message);
            }
            record.setAttributeNames(toJsonString(getList(issue, "attributeNames")));
            record.setIssueValue(toJsonString(issue));
            if (record.getId() == null) {
                listingIssueMapper.insert(record);
            } else {
                listingIssueMapper.updateById(record);
            }
        }
    }

    /**
     * 从图片属性的任意嵌套结构中提取 URL，兼容 media_location、link、url 等 Listings API 字段。
     *
     * @param images 待保存图片集合
     * @param listingMarketplaceId Listing 站点记录编号
     * @param imageType 图片类型
     * @param value 图片属性值
     */
    private void collectImageUrls(List<AmazonListingImageDO> images, Long listingMarketplaceId, String imageType,
                                  Object value) {
        if (value instanceof Map<?, ?> rawMap) {
            Map<String, Object> map = toMap(rawMap);
            String url = firstImageUrl(map);
            if (url != null) {
                addImage(images, listingMarketplaceId, imageType, url, getInteger(map, "width"), getInteger(map, "height"));
            }
            map.values().forEach(child -> collectImageUrls(images, listingMarketplaceId, imageType, child));
        } else if (value instanceof List<?> values) {
            values.forEach(child -> collectImageUrls(images, listingMarketplaceId, imageType, child));
        }
    }

    /**
     * 返回对象中首个可识别的图片 URL 字段。
     *
     * @param value 图片属性中的一个对象
     * @return 图片 URL；未找到时返回 {@code null}
     */
    private String firstImageUrl(Map<String, Object> value) {
        for (String key : List.of("media_location", "link", "url", "image_url")) {
            String url = getString(value, key);
            if (StrUtil.startWithIgnoreCase(url, "http://") || StrUtil.startWithIgnoreCase(url, "https://")) {
                return url;
            }
        }
        return null;
    }

    /**
     * 将 Amazon 属性名转换为数据库允许的图片类型，避免超出字段长度。
     *
     * @param attributeName Amazon 属性名称
     * @return 不超过 32 个字符的图片类型
     */
    private String normalizeImageType(String attributeName) {
        return StrUtil.subWithLength(attributeName.toUpperCase(Locale.ROOT), 0, 32);
    }

    /**
     * 追加去重后的图片记录，并为同类型图片分配稳定排序号。
     *
     * @param images 待保存图片集合
     * @param listingMarketplaceId Listing 站点记录编号
     * @param imageType 图片类型
     * @param imageUrl 图片地址
     * @param width 图片宽度
     * @param height 图片高度
     */
    private void addImage(List<AmazonListingImageDO> images, Long listingMarketplaceId, String imageType, String imageUrl,
                          Integer width, Integer height) {
        if (StrUtil.isBlank(imageUrl) || images.stream().anyMatch(image -> imageUrl.equals(image.getImageUrl()))) {
            return;
        }
        AmazonListingImageDO record = new AmazonListingImageDO();
        record.setListingMarketplaceId(listingMarketplaceId);
        record.setImageType(imageType);
        record.setImageUrl(imageUrl);
        record.setWidth(width);
        record.setHeight(height);
        record.setSortOrder((int) images.stream().filter(image -> imageType.equals(image.getImageType())).count());
        images.add(record);
    }

    /**
     * 读取可能由 JSON 反序列化为任意 Number 的整数。
     *
     * @param source JSON 对象
     * @param key 整数字段名
     * @return 整数值；字段缺失或格式无效时返回 {@code null}
     */
    private Integer getInteger(Map<String, Object> source, String key) {
        Object value = source.get(key);
        if (value instanceof Number number) {
            return number.intValue();
        }
        try {
            return value == null ? null : Integer.valueOf(String.valueOf(value));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

}
