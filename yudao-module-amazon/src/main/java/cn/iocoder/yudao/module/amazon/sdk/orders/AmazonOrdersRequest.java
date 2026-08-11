package cn.iocoder.yudao.module.amazon.sdk.orders;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.net.URI;

/** Orders SDK 请求上下文；授权信息由 Service 注入，避免 SDK 依赖店铺领域对象。 */
@Data
public class AmazonOrdersRequest {
    private Long shopId;
    private String endpoint;
    private String accessToken;
    private String countryCode;
    private String marketplaceId;
    private String orderId;
    private String path;
    private String operation;
    private String storage;
    private Map<String, String> query = new LinkedHashMap<>();
    private Map<String, Object> body;
    /** Service 已完成业务校验后的完整 URI；为空时由 SDK 根据字段构造。 */
    private URI uri;
}
