package cn.iocoder.yudao.module.amazon.dal.dataobject.shop;

import cn.iocoder.yudao.module.amazon.dal.dataobject.seller.AmazonShopMarketplaceDO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/** 启用店铺及其参与 Marketplace 的联表聚合结果。 */
@Data
public class AmazonShopWithMarketplacesDO {

    private AmazonShopDO shop;

    private List<AmazonShopMarketplaceDO> participations = new ArrayList<>();
}
