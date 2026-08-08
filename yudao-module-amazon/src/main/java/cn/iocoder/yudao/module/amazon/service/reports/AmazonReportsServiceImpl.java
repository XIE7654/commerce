package cn.iocoder.yudao.module.amazon.service.reports;

import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportCreateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportIdReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportsListReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.enums.AmazonReportTypeEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Amazon Reports 服务实现。
 *
 * <p>Reports API 先异步创建任务，待任务状态为 DONE 后使用返回的 reportDocumentId 查询短时下载地址。</p>
 */
@Service
public class AmazonReportsServiceImpl implements AmazonReportsService {

    private static final String REPORTS_PATH = "/reports/2021-06-30";

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> createReport(AmazonReportCreateReqVO request) {
        validateDateRange(request.getDataStartTime(), request.getDataEndTime(), "dataStartTime", "dataEndTime");
        return execute(request.getShopId(), request.getCountryCode(), (shop, marketplace, accessToken) -> {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("reportType", request.getReportType().getAvailableReport());
            body.put("marketplaceIds", List.of(marketplace.getMarketplaceId()));
            putBodyValue(body, "dataStartTime", request.getDataStartTime());
            putBodyValue(body, "dataEndTime", request.getDataEndTime());
            if (request.getReportOptions() != null && !request.getReportOptions().isEmpty()) {
                body.put("reportOptions", request.getReportOptions());
            }
            return amazonSellingPartnerClient.createReport(URI.create(marketplace.getEndpoint() + REPORTS_PATH + "/reports"),
                    accessToken, body, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
        });
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getReports(AmazonReportsListReqVO request) {
        validateListRequest(request);
        return execute(request.getShopId(), request.getCountryCode(), (shop, marketplace, accessToken) ->
                amazonSellingPartnerClient.getReports(buildReportsUri(marketplace, request), accessToken, "getReports", "reports",
                        shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId()));
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getReport(AmazonReportIdReqVO request) {
        return getReportResource(request, "/reports/", "getReport", "report");
    }

    /** {@inheritDoc} */
    @Override
    public void cancelReport(AmazonReportIdReqVO request) {
        execute(request.getShopId(), request.getCountryCode(), (shop, marketplace, accessToken) -> {
            URI uri = buildIdUri(marketplace, "/reports/", request.getId());
            amazonSellingPartnerClient.cancelReport(uri, accessToken, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
            return null;
        });
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getReportDocument(AmazonReportIdReqVO request) {
        return getReportResource(request, "/documents/", "getReportDocument", "report-document");
    }

    /**
     * 查询以路径标识符定位的 Reports 资源，统一处理任务详情和报表文件元数据的授权与审计信息。
     *
     * @param request 店铺、站点和 Amazon 资源编号
     * @param resourcePath Reports API 资源路径
     * @param operationName Amazon API 操作名称
     * @param storageName JSON 存储名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> getReportResource(AmazonReportIdReqVO request, String resourcePath, String operationName,
                                                  String storageName) {
        return execute(request.getShopId(), request.getCountryCode(), (shop, marketplace, accessToken) ->
                amazonSellingPartnerClient.getReports(buildIdUri(marketplace, resourcePath, request.getId()), accessToken,
                        operationName, storageName, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId()));
    }

    /**
     * 在店铺归属的当前租户内获取授权信息并执行 Reports API 调用。
     *
     * @param shopId 当前租户的店铺编号
     * @param countryCode 站点国家代码
     * @param action 使用店铺、站点和 Seller Token 执行的调用
     * @return 调用结果
     */
    private <T> T execute(Long shopId, String countryCode, ReportsAction<T> action) {
        AmazonShopDO shop = requireShop(shopId);
        AmazonMarketplaceEnum marketplace = requireMarketplace(countryCode);
        return action.execute(shop, marketplace, amazonOAuthService.getSellerAccessToken(shop.getId()));
    }

    /**
     * 构造报表任务列表 URI；分页令牌存在时只能作为唯一 Amazon 查询参数传递。
     *
     * @param marketplace 目标站点配置
     * @param request 报表任务列表筛选条件
     * @return 可直接发起请求的 URI
     */
    private URI buildReportsUri(AmazonMarketplaceEnum marketplace, AmazonReportsListReqVO request) {
        Map<String, String> query = new TreeMap<>();
        if (!isBlank(request.getNextToken())) {
            query.put("nextToken", request.getNextToken());
        } else {
            query.put("reportTypes", joinReportTypes(request.getReportTypes()));
            query.put("marketplaceIds", marketplace.getMarketplaceId());
            put(query, "processingStatuses", join(request.getProcessingStatuses()));
            put(query, "pageSize", request.getPageSize() == null ? null : request.getPageSize().toString());
            put(query, "createdSince", request.getCreatedSince());
            put(query, "createdUntil", request.getCreatedUntil());
        }
        return URI.create(marketplace.getEndpoint() + REPORTS_PATH + "/reports?" + buildQuery(query));
    }

    /**
     * 构造携带路径标识符的 Reports API URI，避免任务和文件编号中的特殊字符改变路由语义。
     *
     * @param marketplace 目标站点配置
     * @param resourcePath Reports API 资源路径
     * @param id Amazon 资源编号
     * @return 可直接发起请求的 URI
     */
    private URI buildIdUri(AmazonMarketplaceEnum marketplace, String resourcePath, String id) {
        return URI.create(marketplace.getEndpoint() + REPORTS_PATH + resourcePath
                + UriUtils.encodePathSegment(id, StandardCharsets.UTF_8));
    }

    /**
     * 验证列表查询的分页互斥关系与创建时间区间，避免 Amazon 因混合 nextToken 和筛选参数拒绝请求。
     *
     * @param request 报表任务列表筛选条件
     */
    private void validateListRequest(AmazonReportsListReqVO request) {
        if (!isBlank(request.getNextToken())) {
            if (!isEmpty(request.getReportTypes()) || !isEmpty(request.getProcessingStatuses()) || request.getPageSize() != null
                    || !isBlank(request.getCreatedSince()) || !isBlank(request.getCreatedUntil())) {
                throw new IllegalArgumentException("nextToken 不能与其他报表筛选条件同时传入");
            }
            return;
        }
        if (isEmpty(request.getReportTypes())) {
            throw new IllegalArgumentException("reportTypes 与 nextToken 必须传入一个");
        }
        validateDateRange(request.getCreatedSince(), request.getCreatedUntil(), "createdSince", "createdUntil");
    }

    /**
     * 验证可选 ISO 8601 日期时间区间。
     *
     * @param start 起始时间
     * @param end 结束时间
     * @param startName 起始参数名
     * @param endName 结束参数名
     */
    private void validateDateRange(String start, String end, String startName, String endName) {
        OffsetDateTime startTime = parseDateTime(start, startName);
        OffsetDateTime endTime = parseDateTime(end, endName);
        if (startTime != null && endTime != null && endTime.isBefore(startTime)) {
            throw new IllegalArgumentException(endName + " 不能早于 " + startName);
        }
    }

    /**
     * 解析可选 ISO 8601 日期时间。
     *
     * @param value 待解析时间
     * @param name 参数名称
     * @return 解析结果；空值返回 {@code null}
     */
    private OffsetDateTime parseDateTime(String value, String name) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return OffsetDateTime.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(name + " 必须为 ISO 8601 日期时间格式", exception);
        }
    }

    /**
     * 查询当前租户下的 Amazon 店铺，确保店铺授权不跨租户使用。
     *
     * @param shopId 店铺编号
     * @return 当前租户的 Amazon 店铺
     */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /**
     * 解析国家代码对应的 Amazon Marketplace 配置。
     *
     * @param countryCode 国家代码
     * @return 目标 Marketplace 配置
     */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }

    /**
     * 按 RFC 3986 编码查询参数。
     *
     * @param query 待编码的查询参数
     * @return URI 查询字符串
     */
    private String buildQuery(Map<String, String> query) {
        List<String> entries = new ArrayList<>();
        query.forEach((key, value) -> entries.add(urlEncode(key) + "=" + urlEncode(value)));
        return String.join("&", entries);
    }

    /**
     * 使用 UTF-8 对查询参数进行 RFC 3986 百分号编码。
     *
     * @param value 待编码的参数值
     * @return 编码后的参数值
     */
    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /**
     * 仅在值非空时写入可选请求体字段。
     *
     * @param body 待写入的请求体
     * @param key 字段名称
     * @param value 字段值
     */
    private void putBodyValue(Map<String, Object> body, String key, String value) {
        if (!isBlank(value)) {
            body.put(key, value);
        }
    }

    /**
     * 仅在值非空时写入可选查询参数。
     *
     * @param query 待写入的查询参数
     * @param key 参数名称
     * @param value 参数值
     */
    private void put(Map<String, String> query, String key, String value) {
        if (!isBlank(value)) {
            query.put(key, value);
        }
    }

    /**
     * 将报表类型枚举转换为 Amazon 要求的逗号分隔参数。
     *
     * @param values 参数值列表
     * @return 逗号分隔值；空列表返回 {@code null}
     */
    private String joinReportTypes(List<AmazonReportTypeEnum> values) {
        return isEmpty(values) ? null : values.stream().map(AmazonReportTypeEnum::getAvailableReport)
                .collect(java.util.stream.Collectors.joining(","));
    }

    /**
     * 将字符串列表转换为 Amazon 要求的逗号分隔参数。
     *
     * @param values 参数值列表
     * @return 逗号分隔值；空列表返回 {@code null}
     */
    private String join(List<String> values) {
        return isEmpty(values) ? null : String.join(",", values);
    }

    /**
     * 判断字符串是否为空白。
     *
     * @param value 待判断字符串
     * @return 字符串为空或仅包含空白字符时返回 {@code true}
     */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * 判断集合是否为空。
     *
     * @param values 待判断集合
     * @return 集合为 {@code null} 或不含元素时返回 {@code true}
     */
    private boolean isEmpty(List<?> values) {
        return values == null || values.isEmpty();
    }

    /**
     * 封装已完成店铺、站点和 Seller Token 解析的 Reports API 调用。
     *
     * @param <T> 调用结果类型
     */
    @FunctionalInterface
    private interface ReportsAction<T> {

        /**
         * 执行一次已授权的 Reports API 调用。
         *
         * @param shop 当前租户店铺
         * @param marketplace 目标站点配置
         * @param accessToken Seller LWA access token
         * @return 调用结果
         */
        T execute(AmazonShopDO shop, AmazonMarketplaceEnum marketplace, String accessToken);
    }
}
