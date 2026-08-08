package cn.iocoder.yudao.module.temu.service.refundandreturn;

import cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo.RefundAndReturnAftersalesListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo.RefundAndReturnBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo.RefundAndReturnOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo.RefundAndReturnParentAftersalesListReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import cn.iocoder.yudao.module.temu.service.apirequestlog.TemuApiRequestLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Temu Refund And Return 退款退货业务 Service 实现。
 */
@Service
@Validated
public class RefundAndReturnServiceImpl implements RefundAndReturnService {

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;
    @Resource
    private TemuApiRequestLogService temuApiRequestLogService;

    /**
     * 调用 Temu 父售后单列表查询接口。
     *
     * @param request 父售后单分页与筛选参数
     * @return Temu 官方父售后单列表响应
     */
    @Override
    public JsonNode getParentAftersaleOrderList(RefundAndReturnParentAftersalesListReqVO request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("pageNo", request.getPageNo());
        params.put("pageSize", request.getPageSize());
        params.put("afterSalesStatusGroup", request.getAfterSalesStatusGroup());
        params.put("createAtStart", request.getCreateAtStart());
        params.put("createAtEnd", request.getCreateAtEnd());
        // SDK 会移除空值，使未指定筛选条件遵循 Temu 的默认查询规则。
        return createClient(request).getAfterSales().parentAftersalesList(params);
    }

    /**
     * 调用 Temu 售后单列表查询接口。
     *
     * @param request 售后单分页与父售后单筛选参数
     * @return Temu 官方售后单列表响应
     */
    @Override
    public JsonNode getAftersaleOrderList(RefundAndReturnAftersalesListReqVO request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("pageNo", request.getPageNo());
        params.put("pageSize", request.getPageSize());
        params.put("parentAfterSalesSnList", request.getParentAfterSalesSnList());
        return createClient(request).getAfterSales().aftersalesList(params);
    }

    /**
     * 调用 Temu 退货单查询接口。
     *
     * @param request 父售后单与售后单查询参数
     * @return Temu 官方退货单响应
     */
    @Override
    public JsonNode getReturnOrderList(RefundAndReturnOrderReqVO request) {
        return createClient(request).getAfterSales().parentReturnOrder(Map.of(
                "parentAfterSalesSn", request.getParentAfterSalesSn(), "afterSalesSn", request.getAfterSalesSn()));
    }

    /**
     * 按请求站点创建 Temu SDK 客户端。
     *
     * <p>应用密钥仅从服务端区域配置读取，避免管理端请求泄露敏感应用配置。</p>
     *
     * @param request 包含站点与授权 Token 的退款退货请求
     * @return 已按区域配置初始化的 Temu SDK 客户端
     */
    private TemuClient createClient(RefundAndReturnBaseReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(), site.getEndpoint(),
                temuJsonStorageService, site.name(), temuApiRequestLogService);
    }

    /**
     * 判断配置字符串是否为空白。
     *
     * @param value 待判断配置值
     * @return 值为空或只包含空白字符时返回 true
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
