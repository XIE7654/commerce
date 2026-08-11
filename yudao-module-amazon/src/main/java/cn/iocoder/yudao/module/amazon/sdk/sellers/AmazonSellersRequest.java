package cn.iocoder.yudao.module.amazon.sdk.sellers;

import lombok.Data;

/** Sellers API 请求参数，授权信息由 Service 注入，避免 SDK 依赖店铺领域对象。 */
@Data
public class AmazonSellersRequest {
    private Long shopId;
    private String endpoint;
    private String accessToken;
}
