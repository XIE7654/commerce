package cn.iocoder.yudao.module.amazon.service.deliverybyamazon;

import cn.iocoder.yudao.module.amazon.controller.admin.deliverybyamazon.vo.DeliveryByAmazonRequestVO;

import java.util.Map;

/** Delivery by Amazon API 服务。 */
public interface DeliveryByAmazonService {
    /**
     * 调用 Delivery by Amazon 接口。
     *
     * @param request 店铺、站点及 Amazon 请求参数
     * @param operation Amazon 操作名称
     * @param method HTTP 请求方式
     * @param resourcePath API 资源路径
     * @return Amazon 原始 JSON 响应
     */
    Map<String, Object> invoke(DeliveryByAmazonRequestVO request, String operation, String method, String resourcePath);
}
