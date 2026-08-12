package cn.iocoder.yudao.module.temu.dal.mysql.orderdetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.temu.dal.dataobject.orderdetail.TemuOrderDetailDO;
import org.apache.ibatis.annotations.Mapper;

/** Temu 父订单详情 Mapper。 */
@Mapper
public interface TemuOrderDetailMapper extends BaseMapperX<TemuOrderDetailDO> {

    /**
     * 按店铺和父订单号查询详情，用于同步时幂等写入。
     *
     * @param shopId 店铺编号
     * @param parentOrderSn Temu 父订单号
     * @return 已保存的订单详情；不存在时返回 {@code null}
     */
    default TemuOrderDetailDO selectByShopIdAndParentOrderSn(Long shopId, String parentOrderSn) {
        return selectOne(TemuOrderDetailDO::getShopId, shopId,
                TemuOrderDetailDO::getParentOrderSn, parentOrderSn);
    }
}
