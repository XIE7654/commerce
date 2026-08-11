package cn.iocoder.yudao.module.amazon.sdk.sellers;

import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.sdk.sellers.dto.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.*;

/**
 * Sellers API SDK，按模型文件为每个 operation 提供一个强类型方法。
 */
@Component
public class AmazonSellersApi {
    @Resource
    private AmazonSellingPartnerClient client;

    /**
     * 查询卖家可参与销售的 Marketplace。
     */
    public AmazonSellersResponse<List<MarketplaceParticipationDto>> getMarketplaceParticipations(AmazonSellersRequest request) {
        Map<String, Object> raw = call(request, "/sellers/v1/marketplaceParticipations", "getMarketplaceParticipations", "marketplace-participations");
        List<MarketplaceParticipationDto> data = new ArrayList<>();
        for (Object item : getList(raw, "payload")) {
            if (item instanceof Map<?, ?> map) data.add(participation(toMap(map)));
        }
        return response(raw, data);
    }

    /**
     * 查询卖家账户及其 Marketplace 参与信息。
     */
    public AmazonSellersResponse<AccountDto> getAccount(AmazonSellersRequest request) {
        Map<String, Object> raw = call(request, "/sellers/v1/account", "getAccount", "account");
        return response(raw, account(getMap(raw, "payload")));
    }

    /**
     * 调用统一 HTTP 客户端，并复用既有审计与响应归档能力。
     */
    private Map<String, Object> call(AmazonSellersRequest request, String path, String operation, String storage) {
        if (request == null || request.getShopId() == null) throw new IllegalArgumentException("shopId 不能为空");
        return client.getByCategory(URI.create(request.getEndpoint() + path), request.getAccessToken(),
                AmazonApiCategory.SELLERS, operation, storage, request.getShopId(), null, null);
    }

    /**
     * 将 Amazon 的 payload/errors 结构转换为调用方统一的 code/data/msg。
     */
    private <T> AmazonSellersResponse<T> response(Map<String, Object> raw, T data) {
        List<?> errors = getList(raw, "errors");
        String msg = errors.isEmpty() ? null : String.valueOf(errors.get(0));
        return new AmazonSellersResponse<>(errors.isEmpty() ? 200 : 400, data, msg);
    }

    /**
     * 转换 Marketplace 参与状态，避免领域层依赖不稳定的 JSON 键。
     */
    private MarketplaceParticipationDto participation(Map<String, Object> source) {
        MarketplaceParticipationDto dto = new MarketplaceParticipationDto();
        dto.setMarketplace(marketplace(getMap(source, "marketplace")));
        dto.setParticipation(participationInfo(getMap(source, "participation")));
        dto.setStoreName(getString(source, "storeName"));
        return dto;
    }

    /**
     * 转换 Marketplace 基础信息。
     */
    private MarketplaceDto marketplace(Map<String, Object> source) {
        MarketplaceDto dto = new MarketplaceDto();
        dto.setId(getString(source, "id"));
        dto.setName(getString(source, "name"));
        dto.setCountryCode(getString(source, "countryCode"));
        dto.setDefaultCurrencyCode(getString(source, "defaultCurrencyCode"));
        dto.setDefaultLanguageCode(getString(source, "defaultLanguageCode"));
        dto.setDomainName(getString(source, "domainName"));
        return dto;
    }

    /**
     * 转换卖家在 Marketplace 的参与状态。
     */
    private ParticipationDto participationInfo(Map<String, Object> source) {
        ParticipationDto dto = new ParticipationDto();
        dto.setIsParticipating(getBoolean(source, "isParticipating"));
        dto.setHasSuspendedListings(getBoolean(source, "hasSuspendedListings"));
        return dto;
    }

    /**
     * 转换 Sellers Account 的嵌套账户资料。
     */
    private AccountDto account(Map<String, Object> source) {
        AccountDto dto = new AccountDto();
        dto.setBusinessType(getString(source, "businessType"));
        dto.setSellingPlan(getString(source, "sellingPlan"));
        List<MarketplaceParticipationDto> list = new ArrayList<>();
        for (Object item : getList(source, "marketplaceParticipationList"))
            if (item instanceof Map<?, ?> map) list.add(participation(toMap(map)));
        dto.setMarketplaceParticipationList(list);
        dto.setBusiness(business(getMap(source, "business")));
        dto.setPrimaryContact(primaryContact(getMap(source, "primaryContact")));
        return dto;
    }

    /**
     * 转换企业资料；Amazon 对个人卖家可能省略此对象。
     */
    private BusinessDto business(Map<String, Object> source) {
        BusinessDto dto = new BusinessDto();
        dto.setName(getString(source, "name"));
        dto.setNonLatinName(getString(source, "nonLatinName"));
        dto.setCompanyRegistrationNumber(getString(source, "companyRegistrationNumber"));
        dto.setCompanyTaxIdentificationNumber(getString(source, "companyTaxIdentificationNumber"));
        dto.setRegisteredBusinessAddress(address(getMap(source, "registeredBusinessAddress")));
        return dto;
    }

    /**
     * 转换主要联系人资料。
     */
    private PrimaryContactDto primaryContact(Map<String, Object> source) {
        PrimaryContactDto dto = new PrimaryContactDto();
        dto.setName(getString(source, "name"));
        dto.setNonLatinName(getString(source, "nonLatinName"));
        dto.setAddress(address(getMap(source, "address")));
        return dto;
    }

    /**
     * 转换地址字段，保留 Amazon 的可选字段为空值。
     */
    private AddressDto address(Map<String, Object> source) {
        AddressDto dto = new AddressDto();
        dto.setAddressLine1(getString(source, "addressLine1"));
        dto.setAddressLine2(getString(source, "addressLine2"));
        dto.setCountryCode(getString(source, "countryCode"));
        dto.setStateOrProvinceCode(getString(source, "stateOrProvinceCode"));
        dto.setCity(getString(source, "city"));
        dto.setPostalCode(getString(source, "postalCode"));
        return dto;
    }

    /**
     * 兼容 JSON 解析器可能返回的布尔值或文本布尔值。
     */
    private Boolean getBoolean(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value == null ? null : value instanceof Boolean b ? b : Boolean.valueOf(String.valueOf(value));
    }
}
