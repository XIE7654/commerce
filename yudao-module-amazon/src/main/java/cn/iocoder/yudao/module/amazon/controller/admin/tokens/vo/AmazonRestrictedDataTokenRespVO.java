package cn.iocoder.yudao.module.amazon.controller.admin.tokens.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/** Amazon Restricted Data Token 响应参数。 */
@Data
public class AmazonRestrictedDataTokenRespVO {

    @Schema(description = "用于调用所申请受限资源的短期访问令牌")
    private String restrictedDataToken;

    @Schema(description = "令牌有效期，单位：秒", example = "3600")
    private Integer expiresIn;

    /**
     * 将 Amazon Tokens API 原始响应转换为受限令牌响应。
     *
     * @param response Amazon Tokens API 原始响应
     * @return 标准化后的 RDT 响应
     */
    public static AmazonRestrictedDataTokenRespVO of(Map<String, Object> response) {
        AmazonRestrictedDataTokenRespVO result = new AmazonRestrictedDataTokenRespVO();
        Object token = response.get("restrictedDataToken");
        result.setRestrictedDataToken(token == null ? null : String.valueOf(token));
        Object expiresIn = response.get("expiresIn");
        result.setExpiresIn(expiresIn instanceof Number number ? number.intValue()
                : expiresIn == null ? null : Integer.valueOf(String.valueOf(expiresIn)));
        return result;
    }

}
