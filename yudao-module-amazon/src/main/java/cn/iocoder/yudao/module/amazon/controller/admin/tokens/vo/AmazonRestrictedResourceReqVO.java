package cn.iocoder.yudao.module.amazon.controller.admin.tokens.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

/** Amazon Tokens API 中请求受限数据的资源定义。 */
@Data
public class AmazonRestrictedResourceReqVO {

    @Schema(description = "受限操作的 HTTP 方法", requiredMode = Schema.RequiredMode.REQUIRED, example = "GET")
    @NotBlank(message = "受限资源请求方法不能为空")
    @Pattern(regexp = "GET|PUT|POST|DELETE", message = "受限资源请求方法仅支持 GET、PUT、POST、DELETE")
    private String method;

    @Schema(description = "受限操作的 SP-API 路径", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "/orders/v0/orders/902-3159896-1390916/address")
    @NotBlank(message = "受限资源路径不能为空")
    @Pattern(regexp = "/.*", message = "受限资源路径必须以 / 开头")
    private String path;

    @Schema(description = "请求的 PII 数据类型；订单列表、订单详情或订单商品场景可传 buyerInfo、shippingAddress、buyerTaxInformation")
    private List<String> dataElements;

}
