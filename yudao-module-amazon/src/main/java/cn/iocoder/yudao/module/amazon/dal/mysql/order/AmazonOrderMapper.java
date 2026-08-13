package cn.iocoder.yudao.module.amazon.dal.mysql.order;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.order.AmazonOrderDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * Amazon 订单 Mapper。
 */
@Mapper
public interface AmazonOrderMapper extends BaseMapperX<AmazonOrderDO> {

    /**
     * 按店铺和 Amazon 订单编号查询订单，受当前租户数据权限隔离。
     *
     * @param shopId 店铺编号
     * @param amazonOrderId Amazon 订单编号
     * @return 订单；不存在时返回 {@code null}
     */
    default AmazonOrderDO selectByShopIdAndAmazonOrderId(Long shopId, String amazonOrderId) {
        return selectOne(AmazonOrderDO::getShopId, shopId,
                AmazonOrderDO::getAmazonOrderId, amazonOrderId);
    }

    /**
     * 查询店铺已成功同步订单中的最新同步时间。
     *
     * @param shopId 店铺编号
     * @return 最新同步时间；尚未同步过时返回 {@code null}
     */
    default LocalDateTime selectLatestSyncTimeByShopId(Long shopId) {
        AmazonOrderDO order = selectOne(new LambdaQueryWrapper<AmazonOrderDO>()
                .select(AmazonOrderDO::getLastSyncTime)
                .eq(AmazonOrderDO::getShopId, shopId)
                .isNotNull(AmazonOrderDO::getLastSyncTime)
                .orderByDesc(AmazonOrderDO::getLastSyncTime)
                .last("LIMIT 1"));
        return order == null ? null : order.getLastSyncTime();
    }
}
