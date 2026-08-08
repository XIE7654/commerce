package cn.iocoder.yudao.module.amazon.service.customerfeedback;

import cn.iocoder.yudao.module.amazon.controller.admin.customerfeedback.vo.CustomerFeedbackBrowseNodeReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.customerfeedback.vo.CustomerFeedbackItemReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/** Customer Feedback API 服务实现。 */
@Service
public class CustomerFeedbackServiceImpl implements CustomerFeedbackService {
    private static final String BASE_PATH = "/customerFeedback/2024-06-01";
    @Resource private AmazonOAuthService amazonOAuthService;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */ @Override public Map<String, Object> getItemReviewTopics(CustomerFeedbackItemReqVO request) { return item(request, "/items/%s/reviews/topics", "getItemReviewTopics", true); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getItemBrowseNode(CustomerFeedbackItemReqVO request) { return item(request, "/items/%s/browseNode", "getItemBrowseNode", false); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getBrowseNodeReviewTopics(CustomerFeedbackBrowseNodeReqVO request) { return browseNode(request, "/browseNodes/%s/reviews/topics", "getBrowseNodeReviewTopics", true); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getItemReviewTrends(CustomerFeedbackItemReqVO request) { return item(request, "/items/%s/reviews/trends", "getItemReviewTrends", false); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getBrowseNodeReviewTrends(CustomerFeedbackBrowseNodeReqVO request) { return browseNode(request, "/browseNodes/%s/reviews/trends", "getBrowseNodeReviewTrends", false); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getBrowseNodeReturnTopics(CustomerFeedbackBrowseNodeReqVO request) { return browseNode(request, "/browseNodes/%s/returns/topics", "getBrowseNodeReturnTopics", false); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getBrowseNodeReturnTrends(CustomerFeedbackBrowseNodeReqVO request) { return browseNode(request, "/browseNodes/%s/returns/trends", "getBrowseNodeReturnTrends", false); }

    /** 按 ASIN 调用 Customer Feedback，并补入模型定义要求的 marketplaceId 与可选排序参数。 */
    private Map<String, Object> item(CustomerFeedbackItemReqVO request, String path, String operation, boolean requiresSortBy) {
        return invoke(request.getShopId(), request.getCountryCode(), String.format(path, encode(request.getAsin())), operation, request.getSortBy(), requiresSortBy);
    }
    /** 按浏览节点调用 Customer Feedback，并在主题接口中校验 sortBy。 */
    private Map<String, Object> browseNode(CustomerFeedbackBrowseNodeReqVO request, String path, String operation, boolean requiresSortBy) {
        return invoke(request.getShopId(), request.getCountryCode(), String.format(path, encode(request.getBrowseNodeId())), operation, request.getSortBy(), requiresSortBy);
    }
    /** 构建请求并使用当前租户店铺凭证发起调用，响应按 Customer Feedback 分类归档。 */
    private Map<String, Object> invoke(Long shopId, String countryCode, String path, String operation, String sortBy, boolean requiresSortBy) {
        if (requiresSortBy && (sortBy == null || sortBy.isBlank())) throw new IllegalArgumentException("sortBy 不能为空");
        AmazonShopDO shop = requireShop(shopId); AmazonMarketplaceEnum marketplace = requireMarketplace(countryCode);
        String query = "marketplaceId=" + encode(marketplace.getMarketplaceId()) + (sortBy == null || sortBy.isBlank() ? "" : "&sortBy=" + encode(sortBy));
        URI uri = URI.create(marketplace.getEndpoint() + BASE_PATH + path + "?" + query);
        return amazonSellingPartnerClient.getByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()), AmazonApiCategory.CUSTOMER_FEEDBACK, operation, operation, shop.getId(), countryCode, marketplace.getMarketplaceId());
    }
    /** 查询当前租户店铺，确保租户拦截器完成数据隔离。 */
    private AmazonShopDO requireShop(Long shopId) { AmazonShopDO shop = amazonShopMapper.selectById(shopId); if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId); return shop; }
    /** 根据国家代码取得受支持的 SP-API 站点。 */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) { AmazonMarketplaceEnum value = AmazonMarketplaceEnum.fromCountryCode(countryCode); if (value == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode); return value; }
    /** 以 UTF-8 百分号编码路径与查询参数，避免标识符中的保留字符改变 URI 语义。 */
    private String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~"); }
}
