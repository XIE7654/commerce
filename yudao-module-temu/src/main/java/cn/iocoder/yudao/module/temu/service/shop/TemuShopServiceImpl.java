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
import cn.iocoder.yudao.module.temu.controller.admin.authorization.vo.AuthorizationAccessTokenInfoReqVO;
import cn.iocoder.yudao.module.temu.service.authorization.AuthorizationService;
import cn.iocoder.yudao.module.temu.service.auth.TemuAuthService;
import cn.iocoder.yudao.module.temu.controller.admin.auth.vo.TemuAuthInfoReqVO;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import tools.jackson.databind.JsonNode;
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
    private AuthorizationService authorizationService;
    @Resource
    private TemuAuthService temuAuthService;

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
        JsonNode authorization = queryAuthorization(createReqVO);
        JsonNode localMallTags = queryLocalMallTags(createReqVO);
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
    private JsonNode queryLocalMallTags(TemuShopSaveReqVO request) {
        try {
            TemuAuthInfoReqVO authRequest = new TemuAuthInfoReqVO();
            authRequest.setSite(request.getSite());
            authRequest.setAccessToken(request.getAuthToken());
            JsonNode response = temuAuthService.getLocalMallTags(authRequest);
            JsonNode result = response == null ? null : response.get("result");
            if (response == null || !response.path("success").asBoolean(false)
                    || result == null || result.isNull() || !result.path("tags").isArray()) {
                throw createAccessTokenInvalidException(response);
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
    private JsonNode queryAuthorization(TemuShopSaveReqVO request) {
        try {
            AuthorizationAccessTokenInfoReqVO authRequest = new AuthorizationAccessTokenInfoReqVO();
            authRequest.setSite(request.getSite());
            authRequest.setAccessToken(request.getAuthToken());
            JsonNode response = authorizationService.getAccessTokenInfo(authRequest);
            JsonNode result = response == null ? null : response.get("result");
            if (response == null || !response.path("success").asBoolean(false)
                    || result == null || result.isNull() || result.path("mallId").isMissingNode()
                    || result.path("mallId").isNull()) {
                throw createAccessTokenInvalidException(response);
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
     * 将 Temu 授权响应映射为卖家持久化对象。
     *
     * @param response Temu 授权响应
     * @param localMallTagsResponse Temu 本地店铺标签响应
     * @return 待保存的卖家对象
     */
    private TemuSellerDO buildSeller(JsonNode response, JsonNode localMallTagsResponse) {
        JsonNode result = response.get("result");
        TemuSellerDO seller = new TemuSellerDO();
        seller.setSemiUniqueId(text(result, "semiUniqueId"));
        seller.setRegionId(integer(result, "regionId"));
        seller.setMallId(result.path("mallId").asLong());
        seller.setMallType(integer(result, "mallType"));
        seller.setTags(json(localMallTagsResponse.get("result"), "tags"));
        seller.setAppSubscribeStatus(integer(result, "appSubscribeStatus"));
        seller.setExpiredTime(longValue(result, "expiredTime"));
        if (seller.getExpiredTime() != null) {
            seller.setExpiredAt(Instant.ofEpochSecond(seller.getExpiredTime())
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        seller.setAppSubscribeEventCodeList(json(result, "appSubscribeEventCodeList"));
        seller.setAuthEventCodeList(json(result, "authEventCodeList"));
        seller.setApiScopeList(json(result, "apiScopeList"));
        seller.setResponseJson(response.toString());
        seller.setLastSyncTime(java.time.LocalDateTime.now());
        return seller;
    }

    /** 从响应节点读取可空文本字段。 */
    private String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }

    /** 从响应节点读取可空整数型字段。 */
    private Integer integer(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asInt();
    }

    /** 从响应节点读取可空长整数型字段。 */
    private Long longValue(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asLong();
    }

    /** 将响应中的数组或对象字段序列化为数据库 JSON 文本。 */
    private String json(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.toString();
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
        JsonNode authorization = queryTemuAuthInfo(request);
        JsonNode result = authorization.get("result");
        TemuSellerDO existingSeller = sellerMapper.selectOne(new LambdaQueryWrapper<TemuSellerDO>()
                .eq(TemuSellerDO::getShopId, existingShop.getId()));
        Long mallId = result.path("mallId").asLong();
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

    /** 使用 TemuAuthService 查询并校验编辑时的新授权信息。 */
    private JsonNode queryTemuAuthInfo(TemuShopSaveReqVO request) {
        try {
            TemuAuthInfoReqVO authRequest = new TemuAuthInfoReqVO();
            authRequest.setSite(request.getSite());
            authRequest.setAccessToken(request.getAuthToken());
            JsonNode response = temuAuthService.getAccessTokenInfo(authRequest);
            JsonNode result = response == null ? null : response.get("result");
            if (response == null || !response.path("success").asBoolean(false)
                    || result == null || result.isNull() || result.path("mallId").isMissingNode()
                    || result.path("mallId").isNull()) {
                throw createAccessTokenInvalidException(response);
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
     * 将 Temu 业务失败响应转换为前端可识别的业务异常。
     * Temu 已提供错误文案时原样透传，响应缺少文案时才使用本地提示兜底。
     *
     * @param response Temu 原始响应，可为空
     * @return 包含 Temu 错误文案的业务异常
     */
    private ServiceException createAccessTokenInvalidException(JsonNode response) {
        if (response != null) {
            for (String fieldName : List.of("error_message", "error_msg", "errorMessage", "message")) {
                JsonNode message = response.path(fieldName);
                if (message.isValueNode() && !message.asText().isBlank()) {
                    return new ServiceException(ACCESS_TOKEN_INVALID.getCode(), message.asText());
                }
            }
        }
        return exception(ACCESS_TOKEN_INVALID);
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
