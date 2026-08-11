package cn.iocoder.yudao.module.amazon.service.reports;

import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportCreateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportIdReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportScheduleCreateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportScheduleIdReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportSchedulesListReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportsListReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import cn.iocoder.yudao.module.amazon.enums.AmazonReportTypeEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import cn.iocoder.yudao.module.amazon.service.shop.AmazonShopService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static cn.hutool.core.collection.CollUtil.isEmpty;
import static cn.hutool.core.util.StrUtil.isBlank;
import static cn.iocoder.yudao.module.amazon.utils.AmazonDateTimeUtils.parseOptional;
import static cn.iocoder.yudao.module.amazon.utils.AmazonDateTimeUtils.validateRange;
import static cn.iocoder.yudao.module.amazon.utils.AmazonQueryUtils.buildQuery;
import static cn.iocoder.yudao.module.amazon.utils.AmazonQueryUtils.join;
import static cn.iocoder.yudao.module.amazon.utils.AmazonQueryUtils.putIfNotBlank;

/**
 * Amazon Reports 服务实现。
 *
 * <p>Reports API 先异步创建任务，待任务状态为 DONE 后使用返回的 reportDocumentId 查询短时下载地址。</p>
 */
@Service
public class AmazonReportsServiceImpl implements AmazonReportsService {

    private static final String REPORTS_PATH = "/reports/2021-06-30";
    /** Reports API 支持创建计划的 ISO 8601 周期。 */
    private static final Set<String> REPORT_SCHEDULE_PERIODS = Set.of(
            "PT5M", "PT15M", "PT30M", "PT1H", "PT2H", "PT4H", "PT8H", "PT12H", "P1D", "P2D", "P3D", "PT84H",
            "P7D", "P14D", "P15D", "P18D", "P30D", "P1M");

    @Resource
    private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopService amazonShopService;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> createReport(AmazonReportCreateReqVO request) {
        validateRange(request.getDataStartTime(), request.getDataEndTime(), "dataStartTime", "dataEndTime");
        return execute(request.getShopId(), request.getCountryCode(), (shop, marketplace, accessToken) -> {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("reportType", request.getReportType().getAvailableReport());
            body.put("marketplaceIds", List.of(marketplace.getMarketplaceId()));
            putBodyValue(body, "dataStartTime", request.getDataStartTime());
            putBodyValue(body, "dataEndTime", request.getDataEndTime());
            if (request.getReportOptions() != null && !request.getReportOptions().isEmpty()) {
                body.put("reportOptions", request.getReportOptions());
            }
            return amazonSellingPartnerClient.createReport(URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + REPORTS_PATH + "/reports"),
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

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getReportSchedules(AmazonReportSchedulesListReqVO request) {
        validateScheduleListRequest(request);
        return execute(request.getShopId(), request.getCountryCode(), (shop, marketplace, accessToken) -> {
            Map<String, String> query = Map.of("reportTypes", joinReportTypes(request.getReportTypes()));
            URI uri = URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + REPORTS_PATH + "/schedules?" + buildQuery(query));
            return amazonSellingPartnerClient.getReports(uri, accessToken, "getReportSchedules", "report-schedules", shop.getId(),
                    request.getCountryCode(), marketplace.getMarketplaceId());
        });
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> createReportSchedule(AmazonReportScheduleCreateReqVO request) {
        validateScheduleRequest(request);
        return execute(request.getShopId(), request.getCountryCode(), (shop, marketplace, accessToken) -> {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("reportType", request.getReportType().getAvailableReport());
            body.put("marketplaceIds", List.of(marketplace.getMarketplaceId()));
            body.put("period", request.getPeriod());
            putBodyValue(body, "nextReportCreationTime", request.getNextReportCreationTime());
            if (request.getReportOptions() != null && !request.getReportOptions().isEmpty()) {
                body.put("reportOptions", request.getReportOptions());
            }
            return amazonSellingPartnerClient.mutateByCategory(URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + REPORTS_PATH + "/schedules"),
                    accessToken, HttpMethod.POST, body, AmazonApiCategory.REPORTS, "createReportSchedule", "report-schedule",
                    shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
        });
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getReportSchedule(AmazonReportScheduleIdReqVO request) {
        return execute(request.getShopId(), request.getCountryCode(), (shop, marketplace, accessToken) ->
                amazonSellingPartnerClient.getReports(buildIdUri(marketplace, "/schedules/", request.getReportScheduleId()), accessToken,
                        "getReportSchedule", "report-schedule", shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId()));
    }

    /** {@inheritDoc} */
    @Override
    public void cancelReportSchedule(AmazonReportScheduleIdReqVO request) {
        execute(request.getShopId(), request.getCountryCode(), (shop, marketplace, accessToken) -> {
            // Amazon 该操作成功时通常不返回正文，使用支持空响应的调用路径保留请求审计。
            amazonSellingPartnerClient.mutateByCategoryOptional(buildIdUri(marketplace, "/schedules/", request.getReportScheduleId()),
                    accessToken, HttpMethod.DELETE, null, AmazonApiCategory.REPORTS, "cancelReportSchedule", "report-schedule",
                    shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
            return null;
        });
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
        AmazonShopDO shop = amazonShopService.requireShop(shopId);
        AmazonMarketplaceEnum marketplace = amazonShopService.requireMarketplace(countryCode);
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
            putIfNotBlank(query, "processingStatuses", join(request.getProcessingStatuses()));
            putIfNotBlank(query, "pageSize", request.getPageSize() == null ? null : request.getPageSize().toString());
            putIfNotBlank(query, "createdSince", request.getCreatedSince());
            putIfNotBlank(query, "createdUntil", request.getCreatedUntil());
        }
        return URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + REPORTS_PATH + "/reports?" + buildQuery(query));
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
        return URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + REPORTS_PATH + resourcePath
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
        validateRange(request.getCreatedSince(), request.getCreatedUntil(), "createdSince", "createdUntil");
    }

    /**
     * 验证计划周期和可选的首次生成时间，避免请求到达 Amazon 后才因格式错误被拒绝。
     *
     * @param request 创建报表计划请求
     */
    private void validateScheduleRequest(AmazonReportScheduleCreateReqVO request) {
        if (!REPORT_SCHEDULE_PERIODS.contains(request.getPeriod())) {
            throw new IllegalArgumentException("period 必须为 Amazon 支持的 ISO 8601 周期");
        }
        parseOptional(request.getNextReportCreationTime(), "nextReportCreationTime");
    }

    /**
     * 验证计划列表的必填报表类型，保证非 Web 调用同样遵守 Amazon 的筛选约束。
     *
     * @param request 报表计划列表查询请求
     */
    private void validateScheduleListRequest(AmazonReportSchedulesListReqVO request) {
        if (isEmpty(request.getReportTypes())) {
            throw new IllegalArgumentException("reportTypes 不能为空");
        }
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
