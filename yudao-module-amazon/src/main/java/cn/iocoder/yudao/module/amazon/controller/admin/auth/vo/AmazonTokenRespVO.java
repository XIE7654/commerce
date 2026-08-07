package cn.iocoder.yudao.module.amazon.controller.admin.auth.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * Amazon Token 响应参数。
 */
@Data
public class AmazonTokenRespVO {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * 将 Amazon 原始响应转换为接口响应对象。
     *
     * @param token Amazon 原始 Token 响应
     * @param fallbackRefreshToken Amazon 未返回 refresh token 时使用的原 Token
     * @return 标准化后的 Token 响应
     */
    public static AmazonTokenRespVO of(Map<String, Object> token, String fallbackRefreshToken) {
        AmazonTokenRespVO response = new AmazonTokenRespVO();
        response.setAccessToken(stringValue(token, "access_token"));
        response.setTokenType(stringValue(token, "token_type"));
        response.setExpiresIn(integerValue(token.get("expires_in")));
        String refreshToken = stringValue(token, "refresh_token");
        response.setRefreshToken(refreshToken == null ? fallbackRefreshToken : refreshToken);
        return response;
    }

    /** 将 Amazon 可能返回的数值或字符串有效期转换为整数。 */
    private static Integer integerValue(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return value == null ? null : Integer.valueOf(String.valueOf(value));
    }

    /** 安全读取 Amazon 原始 JSON 的字符串字段。 */
    private static String stringValue(Map<String, Object> token, String key) {
        Object value = token.get(key);
        return value == null ? null : String.valueOf(value);
    }
}
