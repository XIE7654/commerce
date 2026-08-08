package cn.iocoder.yudao.module.amazon.service.easyship;

import cn.iocoder.yudao.module.amazon.controller.admin.easyship.vo.EasyShipRequestVO;
import java.util.Map;

/** Amazon Easy Ship API 服务。 */
public interface EasyShipService {
    /**
     * 调用 Easy Ship 接口。
     *
     * @param request 店铺、站点及 Amazon 请求参数
     * @param operation Amazon 操作名称
     * @param method HTTP 请求方式
     * @param resourcePath API 资源路径
     * @return Amazon 原始 JSON 响应
     */
    Map<String, Object> invoke(EasyShipRequestVO request, String operation, String method, String resourcePath);
}
