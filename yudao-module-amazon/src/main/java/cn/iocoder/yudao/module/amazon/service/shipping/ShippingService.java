package cn.iocoder.yudao.module.amazon.service.shipping;

import cn.iocoder.yudao.module.amazon.controller.admin.shipping.vo.ShippingRequestVO;
import java.util.Map;

/** Amazon Shipping API 服务。 */
public interface ShippingService {
    /**
     * 调用指定版本的 Shipping API。
     *
     * @param request 店铺、站点、路径参数与 Amazon 请求参数
     * @param operation Amazon 操作名称
     * @param method HTTP 请求方式
     * @param resourcePath 包含 {id}/{secondaryId} 占位符的 API 资源路径
     * @return Amazon 原始 JSON 响应
     */
    Map<String, Object> invoke(ShippingRequestVO request, String operation, String method, String resourcePath);
}
