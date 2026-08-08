package cn.iocoder.yudao.module.amazon.controller.admin.sellerwallet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Map;

/** Amazon Seller Wallet 通用请求参数。 */
@Data
public class AmazonSellerWalletReqVO {
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1") @NotNull(message = "店铺编号不能为空") private Long shopId;
    @Schema(description = "站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "GB") @NotBlank(message = "国家代码不能为空") private String countryCode;
    @Schema(description = "Seller Wallet 账户编号") private String accountId;
    @Schema(description = "交易编号") private String transactionId;
    @Schema(description = "定时转账编号") private String transferScheduleId;
    @Schema(description = "下一页令牌") private String nextPageToken;
    @Schema(description = "转出国家代码", example = "GB") private String sourceCountryCode;
    @Schema(description = "转出货币代码", example = "GBP") private String sourceCurrencyCode;
    @Schema(description = "转入国家代码", example = "CN") private String destinationCountryCode;
    @Schema(description = "转入货币代码", example = "CNY") private String destinationCurrencyCode;
    @Schema(description = "转账预览基础金额", example = "100.00") private String baseAmount;
    @Schema(description = "目标账户数字签名；创建或更新转账时必填") private String destAccountDigitalSignature;
    @Schema(description = "金额数字签名；创建或更新转账时必填") private String amountDigitalSignature;
    @Schema(description = "Amazon Seller Wallet 原始请求体；创建或更新时必填") private Map<String, Object> body;
}
