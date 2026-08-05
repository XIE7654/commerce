package cn.iocoder.yudao.module.amazon.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Amazon OAuth 授权地址请求参数。
 */
@Data
@Schema(description = "管理后台 - Amazon OAuth 授权地址 Request VO")
public class AmazonAuthorizeReqVO {

    /** 店铺显示名称。 */
    @NotBlank(message = "店铺名称不能为空")
    private String shopName;
    /** OAuth 类型：seller 或 ads。 */
    @NotBlank(message = "授权类型不能为空")
    private String type;
}
