package cn.iocoder.yudao.module.amazon.service.seller;

import java.util.Map;
import cn.iocoder.yudao.module.amazon.sdk.sellers.dto.AccountDto;

/**
 * Amazon 卖家账户档案同步 Service。
 */
public interface AmazonSellerAccountService {

    /**
     * 保存 Sellers Account 响应中的账户与企业档案。
     *
     * @param shopId 店铺编号
     * @param response Sellers API 原始响应
     */
    void syncSellerAccount(Long shopId, Map<String, Object> response);

    /** 保存已完成模型转换的 Sellers Account 数据。 */
    void syncSellerAccount(Long shopId, AccountDto account);
}
