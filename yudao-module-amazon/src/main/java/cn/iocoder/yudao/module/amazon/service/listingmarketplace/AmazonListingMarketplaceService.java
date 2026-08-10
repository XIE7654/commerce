package cn.iocoder.yudao.module.amazon.service.listingmarketplace;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.amazon.controller.admin.listingmarketplace.vo.*;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingmarketplace.AmazonListingMarketplaceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * Listing信息表 Service 接口
 *
 * @author 自达源码
 */
public interface AmazonListingMarketplaceService {

    /**
     * 同步当前租户中所有启用店铺已参与站点的 Amazon Listings 到本地数据库。
     *
     * @return 同步店铺、站点、商品数量及失败明细
     */
    AmazonListingMarketplaceSyncRespVO syncAllAvailableListings();

    /**
     * 创建Listing信息表
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createListingMarketplace(@Valid AmazonListingMarketplaceSaveReqVO createReqVO);

    /**
     * 更新Listing信息表
     *
     * @param updateReqVO 更新信息
     */
    void updateListingMarketplace(@Valid AmazonListingMarketplaceSaveReqVO updateReqVO);

    /**
     * 删除Listing信息表
     *
     * @param id 编号
     */
    void deleteListingMarketplace(Long id);

    /**
    * 批量删除Listing信息表
    *
    * @param ids 编号
    */
    void deleteListingMarketplaceListByIds(List<Long> ids);

    /**
     * 获得Listing信息表
     *
     * @param id 编号
     * @return Listing信息表
     */
    AmazonListingMarketplaceDO getListingMarketplace(Long id);

    /**
     * 获得Listing信息表分页
     *
     * @param pageReqVO 分页查询
     * @return Listing信息表分页
     */
    PageResult<AmazonListingMarketplaceDO> getListingMarketplacePage(AmazonListingMarketplacePageReqVO pageReqVO);

}
