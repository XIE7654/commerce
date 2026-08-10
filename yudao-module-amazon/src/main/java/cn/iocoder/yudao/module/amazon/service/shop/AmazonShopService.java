package cn.iocoder.yudao.module.amazon.service.shop;

import java.util.*;

import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import jakarta.validation.*;
import cn.iocoder.yudao.module.amazon.controller.admin.shop.vo.*;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * Amazon店铺授权 Service 接口
 *
 * @author 自达源码
 */
public interface AmazonShopService {

    /**
     * 创建Amazon店铺授权
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShop(@Valid AmazonShopSaveReqVO createReqVO);

    /**
     * 更新Amazon店铺授权
     *
     * @param updateReqVO 更新信息
     */
    void updateShop(@Valid AmazonShopSaveReqVO updateReqVO);

    /**
     * 删除Amazon店铺授权
     *
     * @param id 编号
     */
    void deleteShop(Long id);

    /**
    * 批量删除Amazon店铺授权
    *
    * @param ids 编号
    */
    void deleteShopListByIds(List<Long> ids);

    /**
     * 获得Amazon店铺授权
     *
     * @param id 编号
     * @return Amazon店铺授权
     */
    AmazonShopDO getShop(Long id);

    /**
     * 获得Amazon店铺授权分页
     *
     * @param pageReqVO 分页查询
     * @return Amazon店铺授权分页
     */
    PageResult<AmazonShopDO> getShopPage(AmazonShopPageReqVO pageReqVO);

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