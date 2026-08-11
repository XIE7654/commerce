package cn.iocoder.yudao.module.amazon.service.seller;

import cn.iocoder.yudao.module.amazon.dal.dataobject.seller.AmazonSellerAccountDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.seller.AmazonSellerAccountMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import cn.iocoder.yudao.module.amazon.sdk.sellers.dto.AccountDto;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Amazon 卖家账户档案同步 Service 实现。
 */
@Service
public class AmazonSellerAccountServiceImpl implements AmazonSellerAccountService {

    @Resource
    private AmazonSellerAccountMapper amazonSellerAccountMapper;
    @Resource
    private ObjectMapper objectMapper;

    /** {@inheritDoc} */
    @Override
    public void syncSellerAccount(Long shopId, Map<String, Object> response) {
        Map<String, Object> payload = getMap(response, "payload");
        if (payload.isEmpty()) {
            return;
        }
        Map<String, Object> primaryContact = getMap(payload, "primaryContact");
        Map<String, Object> primaryContactAddress = getMap(primaryContact, "address");
        Map<String, Object> business = getMap(payload, "business");
        Map<String, Object> registeredAddress = getMap(business, "registeredBusinessAddress");
        LocalDateTime syncTime = LocalDateTime.now();

        AmazonSellerAccountDO account = amazonSellerAccountMapper.selectByShopId(shopId);
        if (account == null) {
            account = new AmazonSellerAccountDO();
            account.setShopId(shopId);
        }
        account.setBusinessType(getString(payload, "businessType"));
        account.setSellingPlan(getString(payload, "sellingPlan"));
        account.setPrimaryContactName(getString(primaryContact, "name"));
        account.setPrimaryContactNonLatinName(getString(primaryContact, "nonLatinName"));
        account.setPrimaryContactAddressLine1(getString(primaryContactAddress, "addressLine1"));
        account.setPrimaryContactAddressLine2(getString(primaryContactAddress, "addressLine2"));
        account.setPrimaryContactCountryCode(getString(primaryContactAddress, "countryCode"));
        account.setPrimaryContactStateOrProvinceCode(getString(primaryContactAddress, "stateOrProvinceCode"));
        account.setPrimaryContactCity(getString(primaryContactAddress, "city"));
        account.setPrimaryContactPostalCode(getString(primaryContactAddress, "postalCode"));
        account.setBusinessName(getString(business, "name"));
        account.setBusinessNonLatinName(getString(business, "nonLatinName"));
        account.setRegisteredAddressLine1(getString(registeredAddress, "addressLine1"));
        account.setRegisteredAddressLine2(getString(registeredAddress, "addressLine2"));
        account.setRegisteredCountryCode(getString(registeredAddress, "countryCode"));
        account.setRegisteredStateOrProvinceCode(getString(registeredAddress, "stateOrProvinceCode"));
        account.setRegisteredCity(getString(registeredAddress, "city"));
        account.setRegisteredPostalCode(getString(registeredAddress, "postalCode"));
        account.setCompanyRegistrationNumber(getString(business, "companyRegistrationNumber"));
        account.setCompanyTaxIdentificationNumber(getString(business, "companyTaxIdentificationNumber"));
        account.setResponseJson(serializeResponse(response));
        account.setLastSyncTime(syncTime);
        if (account.getId() == null) {
            amazonSellerAccountMapper.insert(account);
        } else {
            amazonSellerAccountMapper.updateById(account);
        }
    }

    /** 将 Sellers SDK 的账户 DTO 转为领域字段并保存，避免领域层继续依赖原始 JSON。 */
    @Override
    public void syncSellerAccount(Long shopId, AccountDto account) {
        if (account == null) return;
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("businessType", account.getBusinessType()); payload.put("sellingPlan", account.getSellingPlan());
        if (account.getBusiness() != null) {
            Map<String, Object> business = new LinkedHashMap<>();
            business.put("name", account.getBusiness().getName()); business.put("nonLatinName", account.getBusiness().getNonLatinName());
            business.put("companyRegistrationNumber", account.getBusiness().getCompanyRegistrationNumber()); business.put("companyTaxIdentificationNumber", account.getBusiness().getCompanyTaxIdentificationNumber());
            if (account.getBusiness().getRegisteredBusinessAddress() != null) business.put("registeredBusinessAddress", addressMap(account.getBusiness().getRegisteredBusinessAddress()));
            payload.put("business", business);
        }
        if (account.getPrimaryContact() != null) {
            Map<String, Object> contact = new LinkedHashMap<>(); contact.put("name", account.getPrimaryContact().getName()); contact.put("nonLatinName", account.getPrimaryContact().getNonLatinName());
            if (account.getPrimaryContact().getAddress() != null) contact.put("address", addressMap(account.getPrimaryContact().getAddress())); payload.put("primaryContact", contact);
        }
        response.put("payload", payload);
        syncSellerAccount(shopId, response);
    }

    private Map<String, Object> addressMap(cn.iocoder.yudao.module.amazon.sdk.sellers.dto.AddressDto address) {
        Map<String, Object> result = new LinkedHashMap<>(); result.put("addressLine1", address.getAddressLine1()); result.put("addressLine2", address.getAddressLine2()); result.put("countryCode", address.getCountryCode()); result.put("stateOrProvinceCode", address.getStateOrProvinceCode()); result.put("city", address.getCity()); result.put("postalCode", address.getPostalCode()); return result;
    }

    /**
     * 读取嵌套对象；异常或缺失字段按空对象处理，以兼容 Amazon 后续新增或省略字段。
     *
     * @param source 源对象
     * @param key 字段名
     * @return 字段对应的 Map
     */
    private Map<String, Object> getMap(Map<String, Object> source, String key) {
        Object value = source.get(key);
        if (!(value instanceof Map<?, ?> map)) {
            return Map.of();
        }
        Map<String, Object> result = new LinkedHashMap<>();
        map.forEach((mapKey, mapValue) -> result.put(String.valueOf(mapKey), mapValue));
        return result;
    }

    /**
     * 将响应字段安全转换为字符串，保留 Amazon 返回的原始文本值。
     *
     * @param source 源对象
     * @param key 字段名
     * @return 字段值；字段缺失时返回 {@code null}
     */
    private String getString(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value == null ? null : String.valueOf(value);
    }

    /**
     * 序列化完整响应快照，便于后续兼容尚未建模的 Amazon 字段。
     *
     * @param response Sellers API 原始响应
     * @return JSON 文本
     * @throws IllegalStateException 响应无法序列化时抛出，阻止写入不完整同步记录
     */
    private String serializeResponse(Map<String, Object> response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JacksonException exception) {
            throw new IllegalStateException("序列化 Amazon Sellers 账户响应失败", exception);
        }
    }
}
