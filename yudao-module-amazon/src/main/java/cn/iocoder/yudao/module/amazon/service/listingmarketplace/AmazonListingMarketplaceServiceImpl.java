package cn.iocoder.yudao.module.amazon.service.listingmarketplace;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.time.LocalDateTime;
import cn.iocoder.yudao.module.amazon.controller.admin.listingmarketplace.vo.*;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listing.AmazonListingDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingmarketplace.AmazonListingMarketplaceDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.seller.AmazonShopMarketplaceParticipationDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.amazon.dal.mysql.listing.AmazonListingMapper;
import cn.iocoder.yudao.module.amazon.dal.mysql.listingmarketplace.AmazonListingMarketplaceMapper;
import cn.iocoder.yudao.module.amazon.dal.mysql.seller.AmazonShopMarketplaceParticipationMapper;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.listings.AmazonListingsService;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.amazon.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.amazon.utils.AmazonDateTimeUtils.parseOrNull;
import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.getList;
import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.getPayload;
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
    private AmazonListingMapper listingMapper;
    @Resource
    private AmazonShopMapper shopMapper;
    @Resource
    private AmazonShopMarketplaceParticipationMapper marketplaceParticipationMapper;
    @Resource
    private AmazonListingsService amazonListingsService;

    /** {@inheritDoc} */
    @Override
    public AmazonListingMarketplaceSyncRespVO syncAllAvailableListings() {
        AmazonListingMarketplaceSyncRespVO result = new AmazonListingMarketplaceSyncRespVO();
        List<AmazonShopDO> shops = shopMapper.selectEnabledList();
        result.setShopCount(shops.size());
        for (AmazonShopDO shop : shops) {
            List<AmazonShopMarketplaceParticipationDO> participations = marketplaceParticipationMapper
                    .selectParticipatingByShopId(shop.getId());
            if (participations.isEmpty()) {
                syncDefaultMarketplace(shop, result);
                continue;
            }
            for (AmazonShopMarketplaceParticipationDO participation : participations) {
                syncMarketplace(shop, participation.getMarketplaceId(), participation.getCountryCode(), result);
            }
        }
        return result;
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
        syncMarketplace(shop, marketplace.getMarketplaceId(), marketplace.getCountryCode(), result);
    }

    /**
     * 分页查询一个店铺站点的 Listings，并将每页结果保存为本地 Listing 与站点信息。
     *
     * @param shop 启用的 Amazon 店铺
     * @param marketplaceId 当前同步的 Amazon Marketplace ID
     * @param countryCode 当前同步站点的国家代码
     * @param result 用于累计同步统计与失败信息
     */
    private void syncMarketplace(AmazonShopDO shop, String marketplaceId, String countryCode,
                                 AmazonListingMarketplaceSyncRespVO result) {
        if (StrUtil.isBlank(countryCode)) {
            result.getFailures().add("店铺 " + shop.getId() + " 的站点 " + marketplaceId
                    + " 缺少国家代码");
            return;
        }
        try {
            String pageToken = null;
            Set<String> pageTokens = new HashSet<>();
            do {
                Map<String, Object> response = amazonListingsService.searchListingsItems(
                        buildSearchRequest(shop.getId(), countryCode, pageToken));
                Map<String, Object> payload = getPayload(response);
                for (Object item : getList(payload, "items")) {
                    saveListingItem(shop.getId(), marketplaceId, item, result);
                }
                pageToken = getString(payload, "nextPageToken");
            } while (StrUtil.isNotBlank(pageToken) && pageTokens.add(pageToken));
            result.setMarketplaceCount(result.getMarketplaceCount() + 1);
        } catch (RuntimeException ex) {
            result.getFailures().add("店铺 " + shop.getId() + " 的站点 " + marketplaceId
                    + " 同步失败: " + StrUtil.blankToDefault(ex.getMessage(), ex.getClass().getSimpleName()));
        }
    }

    /**
     * 构造每页 Listings 查询参数，固定请求 summaries 以获得本地站点信息表所需字段。
     *
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param pageToken Amazon 分页 Token，首页为 {@code null}
     * @return Listings 查询请求
     */
    private AmazonListingsSearchReqVO buildSearchRequest(Long shopId, String countryCode, String pageToken) {
        AmazonListingsSearchReqVO request = new AmazonListingsSearchReqVO();
        request.setShopId(shopId);
        request.setCountryCode(countryCode);
        request.setIncludedData(List.of("summaries"));
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
     * @param requestedMarketplaceId 本轮请求的站点，用于过滤异常返回的其它站点数据
     * @param item Amazon Listings Item 原始对象
     * @param result 用于累计已保存记录数
     */
    private void saveListingItem(Long shopId, String requestedMarketplaceId, Object item,
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
            if (!requestedMarketplaceId.equals(marketplaceId)) {
                continue;
            }
            saveListingMarketplace(listing.getId(), marketplaceId, summary);
            result.setListingMarketplaceCount(result.getListingMarketplaceCount() + 1);
        }
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
    private void saveListingMarketplace(Long listingId, String marketplaceId, Map<String, Object> summary) {
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
    }

}
