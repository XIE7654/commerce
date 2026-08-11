package cn.iocoder.yudao.module.amazon.service.shipmentinvoicing;

import cn.iocoder.yudao.module.amazon.controller.admin.shipmentinvoicing.vo.ShipmentInvoicingRequestVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/** Amazon Shipment Invoicing API 服务实现。 */
@Service
public class ShipmentInvoicingServiceImpl implements ShipmentInvoicingService {

    private static final String API_PREFIX = "/fba/outbound/brazil/v0/shipments/";

    @Resource
    private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getShipmentDetails(ShipmentInvoicingRequestVO request) {
        return get(request, "", "getShipmentDetails", "shipment-details");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> submitInvoice(ShipmentInvoicingRequestVO request) {
        requireInvoiceBody(request.getInvoiceBody());
        RequestContext context = context(request);
        return amazonSellingPartnerClient.mutateByCategory(uri(context.marketplace(), request, "/invoice"), context.accessToken(),
                HttpMethod.POST, request.getInvoiceBody(), AmazonApiCategory.SHIPMENT_INVOICING, "submitInvoice", "invoice",
                context.shopId(), request.getCountryCode(), context.marketplace().getMarketplaceId());
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getInvoiceStatus(ShipmentInvoicingRequestVO request) {
        return get(request, "/invoice/status", "getInvoiceStatus", "invoice-status");
    }

    /** 调用 Shipment Invoicing 只读接口并归档 Amazon 响应。 */
    private Map<String, Object> get(ShipmentInvoicingRequestVO request, String suffix, String operation, String storageName) {
        RequestContext context = context(request);
        return amazonSellingPartnerClient.getByCategory(uri(context.marketplace(), request, suffix), context.accessToken(),
                AmazonApiCategory.SHIPMENT_INVOICING, operation, storageName, context.shopId(), request.getCountryCode(),
                context.marketplace().getMarketplaceId());
    }

    /** 校验提交发票的完整性，Amazon 使用 Content-MD5 拒绝内容不一致的请求。 */
    private void requireInvoiceBody(Map<String, Object> body) {
        if (body == null || body.isEmpty()) {
            throw new IllegalArgumentException("invoiceBody 不能为空");
        }
        if (isBlank(body.get("ContentMD5Value")) || isBlank(body.get("InvoiceContent"))) {
            throw new IllegalArgumentException("invoiceBody 必须包含 ContentMD5Value 和 InvoiceContent");
        }
    }

    /** 仅允许 Brazil Marketplace 调用该 API，这是 Amazon 模型定义的服务范围。 */
    private RequestContext context(ShipmentInvoicingRequestVO request) {
        if (!"BR".equalsIgnoreCase(request.getCountryCode())) {
            throw new IllegalArgumentException("Shipment Invoicing 仅支持 BR 站点");
        }
        AmazonShopDO shop = amazonShopMapper.selectById(request.getShopId());
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + request.getShopId());
        }
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(request.getCountryCode());
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + request.getCountryCode());
        }
        return new RequestContext(shop.getId(), amazonOAuthService.getSellerAccessToken(shop.getId()), marketplace);
    }

    /** 生成货件资源 URI，并对货件编号编码以保持路径边界。 */
    private URI uri(AmazonMarketplaceEnum marketplace, ShipmentInvoicingRequestVO request, String suffix) {
        return URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + API_PREFIX + encode(requiredShipmentId(request.getShipmentId())) + suffix);
    }

    /** 校验货件编号，保证 Service 被非 Controller 调用时也不会构造无效路径。 */
    private String requiredShipmentId(String shipmentId) {
        if (shipmentId == null || shipmentId.isBlank()) {
            throw new IllegalArgumentException("shipmentId 不能为空");
        }
        return shipmentId.trim();
    }

    /** 对路径变量执行 UTF-8 百分号编码。 */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /** 判断请求体字段是否为空，兼容 JSON 反序列化出的不同值类型。 */
    private boolean isBlank(Object value) {
        return value == null || value.toString().isBlank();
    }

    /** Shipment Invoicing 调用所需的店铺、令牌和区域端点上下文。 */
    private record RequestContext(Long shopId, String accessToken, AmazonMarketplaceEnum marketplace) {
    }
}
