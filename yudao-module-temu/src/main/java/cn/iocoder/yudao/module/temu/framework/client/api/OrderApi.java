package cn.iocoder.yudao.module.temu.framework.client.api;

import cn.iocoder.yudao.module.temu.framework.client.TemuApiResponse;
import cn.iocoder.yudao.module.temu.framework.client.TemuClient;
import cn.iocoder.yudao.module.temu.framework.client.TemuClientException;
import cn.iocoder.yudao.module.temu.framework.client.order.CombinedShipmentListReqVO;
import cn.iocoder.yudao.module.temu.framework.client.order.CombinedShipmentListDto;
import cn.iocoder.yudao.module.temu.framework.client.order.CustomizationOrderReqVO;
import cn.iocoder.yudao.module.temu.framework.client.order.CustomizationOrderListDto;
import cn.iocoder.yudao.module.temu.framework.client.order.DecryptShippingInfoReqVO;
import cn.iocoder.yudao.module.temu.framework.client.order.DecryptShippingInfoDto;
import cn.iocoder.yudao.module.temu.framework.client.order.OrderDetailDto;
import cn.iocoder.yudao.module.temu.framework.client.order.OrderListReqVO;
import cn.iocoder.yudao.module.temu.framework.client.order.OrderListDto;
import cn.iocoder.yudao.module.temu.framework.client.order.ParentOrderReqVO;
import cn.iocoder.yudao.module.temu.framework.client.order.ShippingInfoDto;
import org.springframework.http.HttpMethod;
import tools.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

/** Temu 订单 API。 */
public class OrderApi {

    private final TemuClient client;

    /**
     * 创建订单 API。
     *
     * @param client Temu 传输客户端
     */
    public OrderApi(TemuClient client) {
        this.client = client;
    }

    /**
     * 查询订单列表。
     *
     * @param params 订单状态、区域、分页和更新时间等 Temu 请求参数
     * @return Temu 原始订单列表响应
     */
    public TemuApiResponse<OrderListDto> listOrdersV2(OrderListReqVO request) {
        return response("bg.order.list.v2.get", request, OrderListDto.class);
    }

    /**
     * 查询父订单详情。
     *
     * @param params 包含 parentOrderSn 的 Temu 请求参数
     * @return Temu 原始订单详情响应
     */
    public TemuApiResponse<OrderDetailDto> detailOrderV2(ParentOrderReqVO request) {
        return response("bg.order.detail.v2.get", request, OrderDetailDto.class);
    }

    /**
     * 查询父订单收货信息。
     *
     * @param params 包含 parentOrderSn 的 Temu 请求参数
     * @return Temu 原始收货信息响应
     */
    public TemuApiResponse<ShippingInfoDto> shippinginfoOrderV2(ParentOrderReqVO request) {
        return response("bg.order.shippinginfo.v2.get", request, ShippingInfoDto.class);
    }

    /**
     * 查询合单发货订单列表。
     *
     * @param params Temu 合单发货查询参数
     * @return Temu 原始合单发货响应
     */
    public TemuApiResponse<CombinedShipmentListDto> combinedshipmentListOrder(CombinedShipmentListReqVO request) {
        return response("bg.order.combinedshipment.list.get", request, CombinedShipmentListDto.class);
    }

    /**
     * 查询定制订单详情。
     *
     * @param params 包含 orderSnList 的 Temu 请求参数
     * @return Temu 原始定制订单响应
     */
    public TemuApiResponse<CustomizationOrderListDto> customizationOrder(CustomizationOrderReqVO request) {
        return response("bg.order.customization.get", request, CustomizationOrderListDto.class);
    }

    /**
     * 解密订单收货信息。
     *
     * @param params Temu 收货信息解密参数
     * @return Temu 原始解密响应
     */
    public TemuApiResponse<DecryptShippingInfoDto> decryptshippinginfoOrder(DecryptShippingInfoReqVO request) {
        return response("bg.order.decryptshippinginfo.get", request, DecryptShippingInfoDto.class);
    }

    /**
     * 通过新版 client 发送订单 Router 请求并映射通用响应。
     *
     * @param apiType Temu API type
     * @param request 业务请求 VO
     * @param resultType 结果 DTO 类型
     * @param <T> 结果 DTO 类型
     * @return Temu 通用响应及强类型结果
     */
    private <T> TemuApiResponse<T> response(String apiType, Object request, Class<T> resultType) {
        if (request == null) {
            throw new IllegalArgumentException("Temu 订单请求参数不能为空: " + apiType);
        }
        JsonNode raw = client.request(apiType, HttpMethod.POST, toParams(request));
        T result = client.convert(raw.get("result"), resultType);
        if (result == null) {
            throw new TemuClientException("Temu 响应缺少 result: " + apiType);
        }
        TemuApiResponse<T> response = new TemuApiResponse<>();
        response.setSuccess(raw.path("success").asBoolean(false));
        response.setRequestId(text(raw, "requestId"));
        response.setErrorCode(raw.path("errorCode").isMissingNode() ? null : raw.path("errorCode").asInt());
        response.setErrorMsg(text(raw, "errorMsg"));
        response.setResult(result);
        return response;
    }

    /**
     * 将请求 VO 映射为 Temu API 的 camelCase 参数，并由 client 过滤空值后签名。
     *
     * @param request 业务请求 VO
     * @return 请求参数 Map
     */
    private Map<String, Object> toParams(Object request) {
        // ObjectMapper 不对外暴露，使用 DTO 字段显式映射以保证 Temu 参数名稳定。
        Map<String, Object> params = new LinkedHashMap<>();
        if (request instanceof OrderListReqVO value) {
            params.put("parentOrderStatus", value.getParentOrderStatus());
            params.put("regionId", value.getRegionId());
            params.put("pageNumber", value.getPageNumber());
            params.put("pageSize", value.getPageSize());
            params.put("updateTime", value.getUpdateTime());
        } else if (request instanceof ParentOrderReqVO value) {
            params.put("parentOrderSn", value.getParentOrderSn());
        } else if (request instanceof CustomizationOrderReqVO value) {
            params.put("orderSnList", value.getOrderSnList());
        } else if (request instanceof CombinedShipmentListReqVO value) {
            params.put("regionId", value.getRegionId());
            params.put("pageNumber", value.getPageNumber());
            params.put("pageSize", value.getPageSize());
        } else if (request instanceof DecryptShippingInfoReqVO value) {
            params.put("parentOrderSn", value.getParentOrderSn());
            params.put("encryptedShippingInfo", value.getEncryptedShippingInfo());
        } else {
            throw new IllegalArgumentException("不支持的 Temu 订单请求参数类型: " + request.getClass().getName());
        }
        return params;
    }

    /**
     * 读取可为空的响应文本字段。
     *
     * @param node 响应节点
     * @param field 字段名
     * @return 字段文本；字段缺失时返回 {@code null}
     */
    private String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asString();
    }
}
