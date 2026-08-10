package cn.iocoder.yudao.module.amazon.service.shop;

import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;

/**
 * Amazon 店铺及站点基础服务。
 */
public interface AmazonShopService {

    /**
     * 查询当前租户下的 Amazon 店铺，不存在时抛出业务参数异常。
     *
     * @param shopId 店铺编号
     * @return 当前租户的店铺授权信息
     */
    AmazonShopDO requireShop(Long shopId);

    /**
     * 根据国家代码解析 Amazon Marketplace，不支持时抛出业务参数异常。
     *
     * @param countryCode 国家代码
     * @return 目标 Marketplace 配置
     */
    AmazonMarketplaceEnum requireMarketplace(String countryCode);

}
