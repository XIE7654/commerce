package cn.iocoder.yudao.module.amazon.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * amazon 错误码枚举类
 *
 * amazon 系统，使用 1-301-000-000 段
 */
public interface ErrorCodeConstants {

    ErrorCode SHOP_NOT_EXISTS = new ErrorCode(1_301_001_000, "Amazon店铺授权不存在");

    ErrorCode LISTING_MARKETPLACE_NOT_EXISTS = new ErrorCode(1_301_002_000, "Listing信息表不存在");

    ErrorCode REPORT_REQUEST_NOT_EXISTS = new ErrorCode(1_301_003_000, "Amazon 报表请求及异步处理任务不存在");
}
