package cn.iocoder.yudao.module.temu.service.seller;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.temu.controller.admin.seller.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.seller.TemuSellerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.temu.dal.mysql.seller.TemuSellerMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.temu.enums.ErrorCodeConstants.*;

/**
 * Temu 卖家商城授权信息 Service 实现类
 *
 * @author 自达源码
 */
@Service
@Validated
public class TemuSellerServiceImpl implements TemuSellerService {

    @Resource
    private TemuSellerMapper sellerMapper;

    @Override
    public Long createSeller(TemuSellerSaveReqVO createReqVO) {
        // 插入
        TemuSellerDO seller = BeanUtils.toBean(createReqVO, TemuSellerDO.class);
        sellerMapper.insert(seller);

        // 返回
        return seller.getId();
    }

    @Override
    public void updateSeller(TemuSellerSaveReqVO updateReqVO) {
        // 校验存在
        validateSellerExists(updateReqVO.getId());
        // 更新
        TemuSellerDO updateObj = BeanUtils.toBean(updateReqVO, TemuSellerDO.class);
        sellerMapper.updateById(updateObj);
    }

    @Override
    public void deleteSeller(Long id) {
        // 校验存在
        validateSellerExists(id);
        // 删除
        sellerMapper.deleteById(id);
    }

    @Override
        public void deleteSellerListByIds(List<Long> ids) {
        // 删除
        sellerMapper.deleteByIds(ids);
        }


    private void validateSellerExists(Long id) {
        if (sellerMapper.selectById(id) == null) {
            throw exception(SELLER_NOT_EXISTS);
        }
    }

    @Override
    public TemuSellerDO getSeller(Long id) {
        return sellerMapper.selectById(id);
    }

    @Override
    public PageResult<TemuSellerDO> getSellerPage(TemuSellerPageReqVO pageReqVO) {
        return sellerMapper.selectPage(pageReqVO);
    }

}