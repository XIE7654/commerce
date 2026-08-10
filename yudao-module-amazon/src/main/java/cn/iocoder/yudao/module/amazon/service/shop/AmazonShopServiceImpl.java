package cn.iocoder.yudao.module.amazon.service.shop;

import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Amazon 店铺及站点基础服务实现。
 */
@Service
public class AmazonShopServiceImpl implements AmazonShopService {

    @Resource
    private AmazonShopMapper amazonShopMapper;

    /** {@inheritDoc} */
    @Override
    public AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /** {@inheritDoc} */
    @Override
    public AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }

}
