package cn.iocoder.yudao.module.amazon.service.vendordirectfulfillment;

import cn.iocoder.yudao.module.amazon.controller.admin.vendordirectfulfillment.vo.VendorDirectFulfillmentRequestVO;

import java.util.Map;

/** Vendor Direct Fulfillment API 服务。 */
public interface VendorDirectFulfillmentService {

    /**
     * 调用固定资源路径的 Vendor Direct Fulfillment 接口。
     *
     * @param request 店铺、站点、路径参数、查询参数和请求体
     * @param operation Amazon API 操作名称
     * @param method HTTP 请求方式
     * @param resourcePath 由 Controller 固定声明的 API 路径模板
     * @return Amazon 原始 JSON 响应，空响应返回空 Map
     */
    Map<String, Object> invoke(VendorDirectFulfillmentRequestVO request, String operation, String method, String resourcePath);
}
