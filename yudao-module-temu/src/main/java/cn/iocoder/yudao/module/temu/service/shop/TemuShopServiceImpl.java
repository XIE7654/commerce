package cn.iocoder.yudao.module.temu.service.shop;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.temu.controller.admin.shop.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.shop.TemuShopDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.temu.dal.mysql.shop.TemuShopMapper;
import cn.iocoder.yudao.module.temu.dal.dataobject.seller.TemuSellerDO;
import cn.iocoder.yudao.module.temu.dal.mysql.seller.TemuSellerMapper;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.client.TemuApiResponse;
import cn.iocoder.yudao.module.temu.framework.client.TemuClient;
import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenInfoResult;
import cn.iocoder.yudao.module.temu.framework.client.auth.LocalMallTagsResult;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import tools.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZoneId;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.temu.enums.ErrorCodeConstants.*;

/**
 * Temu 店铺 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class TemuShopServiceImpl implements TemuShopService {

    @Resource
    private TemuShopMapper shopMapper;
    @Resource
    private TemuSellerMapper sellerMapper;
    @Resource
    private TemuProperties temuProperties;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 校验店铺 accessToken 并保存店铺及其商城授权信息。
     *
     * @param createReqVO 店铺创建参数
     * @return 店铺编号
     * @throws cn.iocoder.yudao.framework.common.exception.ServiceException accessToken 无效或授权响应不完整
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createShop(TemuShopSaveReqVO createReqVO) {
        // 先完成远程鉴权，避免无效 Token 导致店铺或卖家记录落库。
        TemuApiResponse<AccessTokenInfoResult> authorization = queryAuthorization(createReqVO);
        TemuApiResponse<LocalMallTagsResult> localMallTags = queryLocalMallTags(createReqVO);
        TemuSellerDO seller = buildSeller(authorization, localMallTags);

        TemuShopDO shop = BeanUtils.toBean(createReqVO, TemuShopDO.class);
        shopMapper.insert(shop);
        seller.setShopId(shop.getId());
        sellerMapper.insert(seller);

        return shop.getId();
    }

    /**
     * 查询店铺本地标签并校验响应，标签用于后续商品刊登和履约规则判断。
     *
     * @param request 店铺创建参数
     * @return Temu 本地店铺标签响应
     */
    private TemuApiResponse<LocalMallTagsResult> queryLocalMallTags(TemuShopSaveReqVO request) {
        try {
            TemuApiResponse<LocalMallTagsResult> response = createClient(request).getAuth().getLocalMallTags();
            if (response.getResult().getTags() == null) {
                throw createAccessTokenInvalidException(null);
            }
            return response;
        } catch (RuntimeException ex) {
            if (ex instanceof cn.iocoder.yudao.framework.common.exception.ServiceException) {
                throw ex;
            }
            throw exception(ACCESS_TOKEN_INVALID);
        }
    }

    /**
     * 调用授权信息接口并校验 Temu 返回的业务成功标记。
     *
     * @param request 店铺创建参数
     * @return Temu 授权信息响应
     */
    private TemuApiResponse<AccessTokenInfoResult> queryAuthorization(TemuShopSaveReqVO request) {
        try {
            return createClient(request).getAuth().getAccessTokenInfo();
        } catch (RuntimeException ex) {
            if (ex instanceof cn.iocoder.yudao.framework.common.exception.ServiceException) {
                throw ex;
            }
            throw exception(ACCESS_TOKEN_INVALID);
        }
    }

    /**
     * 将 Temu 授权响应映射为卖家持久化对象。
     *
     * @param response Temu 授权响应
     * @param localMallTagsResponse Temu 本地店铺标签响应
     * @return 待保存的卖家对象
     */
    private TemuSellerDO buildSeller(TemuApiResponse<AccessTokenInfoResult> response,
                                     TemuApiResponse<LocalMallTagsResult> localMallTagsResponse) {
        AccessTokenInfoResult result = response.getResult();
        // DTO 与 DO 的同名标量字段统一转换，避免维护大量重复 setter。
        TemuSellerDO seller = BeanUtils.toBean(result, TemuSellerDO.class);
        seller.setTags(writeJson(localMallTagsResponse.getResult().getTags()));
        seller.setAppSubscribeEventCodeList(writeJson(result.getAppSubscribeEventCodeList()));
        seller.setAuthEventCodeList(writeJson(result.getAuthEventCodeList()));
        seller.setApiScopeList(writeJson(result.getApiScopeList()));
        if (seller.getExpiredTime() != null) {
            seller.setExpiredAt(Instant.ofEpochSecond(seller.getExpiredTime())
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        seller.setResponseJson(writeJson(response));
        seller.setLastSyncTime(java.time.LocalDateTime.now());
        return seller;
    }

    /** 将新版客户端响应或字段序列化为数据库 JSON 文本。 */
    private String writeJson(Object value) {
        if (value == null) return null;
        try {
            return objectMapper.writeValueAsString(value);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShop(TemuShopSaveReqVO updateReqVO) {
        TemuShopDO existingShop = shopMapper.selectById(updateReqVO.getId());
        if (existingShop == null) {
            throw exception(SHOP_NOT_EXISTS);
        }
        // 站点或 Token 发生变化时重新同步授权，避免保留失效或属于其他商城的凭证。
        if (!Objects.equals(existingShop.getAuthToken(), updateReqVO.getAuthToken())
                || !Objects.equals(existingShop.getSite(), updateReqVO.getSite())) {
            syncSellerAuthorization(existingShop, updateReqVO);
        }
        TemuShopDO updateObj = BeanUtils.toBean(updateReqVO, TemuShopDO.class);
        shopMapper.updateById(updateObj);
    }

    /**
     * 重新校验并同步店铺授权信息。
     *
     * @param existingShop 数据库中的原店铺
     * @param request 新的店铺编辑参数
     */
    private void syncSellerAuthorization(TemuShopDO existingShop, TemuShopSaveReqVO request) {
        TemuApiResponse<AccessTokenInfoResult> authorization = queryTemuAuthInfo(request);
        AccessTokenInfoResult result = authorization.getResult();
        TemuSellerDO existingSeller = sellerMapper.selectOne(new LambdaQueryWrapper<TemuSellerDO>()
                .eq(TemuSellerDO::getShopId, existingShop.getId()));
        Long mallId = result.getMallId();
        if (existingSeller != null && existingSeller.getMallId() != null
                && !Objects.equals(existingSeller.getMallId(), mallId)) {
            throw exception(SHOP_MALL_NOT_MATCH);
        }
        TemuSellerDO seller = buildSeller(authorization, queryLocalMallTags(request));
        seller.setShopId(existingShop.getId());
        if (existingSeller == null) {
            sellerMapper.insert(seller);
        } else {
            seller.setId(existingSeller.getId());
            sellerMapper.updateById(seller);
        }
    }

    /** 使用新版 Temu client 查询并校验编辑时的新授权信息。 */
    private TemuApiResponse<AccessTokenInfoResult> queryTemuAuthInfo(TemuShopSaveReqVO request) {
        try {
            return createClient(request).getAuth().getAccessTokenInfo();
        } catch (RuntimeException ex) {
            if (ex instanceof cn.iocoder.yudao.framework.common.exception.ServiceException) {
                throw ex;
            }
            throw exception(ACCESS_TOKEN_INVALID);
        }
    }

    /**
     * 将 Temu 业务失败响应转换为前端可识别的业务异常。
     * Temu 已提供错误文案时原样透传，响应缺少文案时才使用本地提示兜底。
     *
     * @param errorMessage Temu 返回的错误文案，可为空
     * @return 包含 Temu 错误文案的业务异常
     */
    private ServiceException createAccessTokenInvalidException(String errorMessage) {
        if (errorMessage != null && !errorMessage.isBlank()) {
            return new ServiceException(ACCESS_TOKEN_INVALID.getCode(), errorMessage);
        }
        return exception(ACCESS_TOKEN_INVALID);
    }

    /** 根据店铺站点和 Token 创建新版 Temu 客户端。 */
    private TemuClient createClient(TemuShopSaveReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || region.getAppKey() == null || region.getAppSecret() == null) {
            throw exception(ACCESS_TOKEN_INVALID);
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAuthToken(), site.name());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteShop(Long id) {
        validateShopExists(id);
        // 卖家表以 shop_id 关联店铺，先删除从表避免残留授权数据。
        sellerMapper.delete(new LambdaQueryWrapper<TemuSellerDO>().eq(TemuSellerDO::getShopId, id));
        shopMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteShopListByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 批量删除时同步清理所有关联卖家授权信息。
        sellerMapper.delete(new LambdaQueryWrapper<TemuSellerDO>().in(TemuSellerDO::getShopId, ids));
        shopMapper.deleteByIds(ids);
    }


    private void validateShopExists(Long id) {
        if (shopMapper.selectById(id) == null) {
            throw exception(SHOP_NOT_EXISTS);
        }
    }

    @Override
    public TemuShopDO getShop(Long id) {
        return shopMapper.selectById(id);
    }

    @Override
    public List<TemuShopDO> getShopListByStatus(Integer status) {
        return shopMapper.selectListByStatus(status);
    }

    @Override
    public PageResult<TemuShopDO> getShopPage(TemuShopPageReqVO pageReqVO) {
        return shopMapper.selectPage(pageReqVO);
    }

}
