package cn.iocoder.yudao.module.temu.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * temu 错误码枚举类
 *
 * temu 系统，使用 1-002-000-000 段
 */
public interface ErrorCodeConstants {

    ErrorCode SHOP_NOT_EXISTS = new ErrorCode(1_300_001_000, "Temu 店铺不存在");
    ErrorCode API_REQUEST_LOG_NOT_EXISTS = new ErrorCode(1_300_002_000, "Temu OpenAPI 请求调用日志不存在");
    ErrorCode SELLER_NOT_EXISTS = new ErrorCode(1_300_003_000, "Temu 卖家商城授权信息不存在");
    ErrorCode ACCESS_TOKEN_INVALID = new ErrorCode(1_300_003_001, "Temu accessToken 错误，无法获取授权信息");
    ErrorCode SHOP_MALL_NOT_MATCH = new ErrorCode(1_300_003_002, "Temu 授权 Token 对应的商城与当前店铺不一致");
}
