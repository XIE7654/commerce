package cn.iocoder.yudao.module.amazon.service.feeds;

import cn.iocoder.yudao.module.amazon.controller.admin.feeds.vo.*;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/** Amazon Feeds API 服务实现。 */
@Service
public class AmazonFeedsServiceImpl implements AmazonFeedsService {
    private static final String PATH = "/feeds/2021-06-30";
    @Resource private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource private AmazonOAuthService amazonOAuthService;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** 查询 Feed 列表；分页令牌只能单独使用。 */
    @Override public Map<String, Object> getFeeds(AmazonFeedsListReqVO request) {
        AmazonMarketplaceEnum marketplace = marketplace(request.getCountryCode());
        if (blank(request.getNextToken()) && empty(request.getFeedTypes())) throw new IllegalArgumentException("feedTypes 与 nextToken 必须传入一个");
        if (!blank(request.getNextToken()) && (!empty(request.getFeedTypes()) || !empty(request.getMarketplaceIds()) || request.getPageSize() != null || !blank(request.getCreatedSince()) || !blank(request.getCreatedUntil()))) throw new IllegalArgumentException("nextToken 不能与其他 Feed 筛选条件同时传入");
        validateDates(request.getCreatedSince(), request.getCreatedUntil());
        Map<String,String> q = new TreeMap<>();
        if (!blank(request.getNextToken())) q.put("nextToken", request.getNextToken()); else {
            put(q,"feedTypes",join(request.getFeedTypes())); put(q,"marketplaceIds",join(empty(request.getMarketplaceIds()) ? List.of(marketplace.getMarketplaceId()) : request.getMarketplaceIds()));
            put(q,"processingStatuses",join(request.getProcessingStatuses())); put(q,"pageSize", request.getPageSize() == null ? null : request.getPageSize().toString()); put(q,"createdSince",request.getCreatedSince()); put(q,"createdUntil",request.getCreatedUntil());
        }
        return call(request.getShopId(), request.getCountryCode(), marketplace, PATH + "/feeds?" + query(q), "getFeeds", "feeds", null);
    }

    /** 创建 Feed；调用前必须已上传 Feed Document。 */
    @Override public Map<String,Object> createFeed(AmazonFeedCreateReqVO request) {
        AmazonMarketplaceEnum m = marketplace(request.getCountryCode()); Map<String,Object> body = new LinkedHashMap<>(); body.put("feedType",request.getFeedType()); body.put("marketplaceIds",request.getMarketplaceIds()); body.put("inputFeedDocumentId",request.getInputFeedDocumentId()); if (request.getFeedOptions() != null && !request.getFeedOptions().isEmpty()) body.put("feedOptions",request.getFeedOptions());
        return call(request.getShopId(),request.getCountryCode(),m,PATH+"/feeds","createFeed","feed",body);
    }
    /** 查询 Feed 详情。 */
    @Override public Map<String,Object> getFeed(AmazonFeedIdReqVO request) { return resource(request,"/feeds/","getFeed","feed"); }
    /** 取消尚未处理的 Feed。 */
    @Override public void cancelFeed(AmazonFeedIdReqVO request) { AmazonMarketplaceEnum m=marketplace(request.getCountryCode()); AmazonShopDO s=shop(request.getShopId()); amazonSellingPartnerClient.cancelFeed(URI.create(amazonMarketplaceProvider.getEndpoint(m)+PATH+"/feeds/"+encodePath(request.getId())), amazonOAuthService.getSellerAccessToken(s.getId()),s.getId(),request.getCountryCode(),m.getMarketplaceId()); }
    /** 创建 Feed Document 上传凭证。 */
    @Override public Map<String,Object> createFeedDocument(AmazonFeedDocumentCreateReqVO request) { AmazonMarketplaceEnum m=marketplace(request.getCountryCode()); return call(request.getShopId(),request.getCountryCode(),m,PATH+"/documents","createFeedDocument","feed-document",Map.of("contentType",request.getContentType())); }
    /** 查询 Feed Document 元数据。 */
    @Override public Map<String,Object> getFeedDocument(AmazonFeedIdReqVO request) { return resource(request,"/documents/","getFeedDocument","feed-document"); }

    private Map<String,Object> resource(AmazonFeedIdReqVO r,String p,String op,String storage){ AmazonMarketplaceEnum m=marketplace(r.getCountryCode()); return call(r.getShopId(),r.getCountryCode(),m,PATH+p+encodePath(r.getId()),op,storage,null); }
    private Map<String,Object> call(Long id,String cc,AmazonMarketplaceEnum m,String path,String op,String storage,Object body){ AmazonShopDO s=shop(id); String token=amazonOAuthService.getSellerAccessToken(s.getId()); URI u=URI.create(amazonMarketplaceProvider.getEndpoint(m)+path); if(body==null) return amazonSellingPartnerClient.getFeeds(u,token,op,storage,s.getId(),cc,m.getMarketplaceId()); return amazonSellingPartnerClient.createFeed(u,token,body,op,storage,s.getId(),cc,m.getMarketplaceId()); }
    private AmazonShopDO shop(Long id){ AmazonShopDO s=amazonShopMapper.selectById(id); if(s==null) throw new IllegalArgumentException("Amazon 店铺不存在: "+id); return s; }
    private AmazonMarketplaceEnum marketplace(String cc){ AmazonMarketplaceEnum m=AmazonMarketplaceEnum.fromCountryCode(cc); if(m==null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: "+cc); return m; }
    private void validateDates(String a,String b){ try { if(!blank(a)&&!blank(b)&&OffsetDateTime.parse(b).isBefore(OffsetDateTime.parse(a))) throw new IllegalArgumentException("createdUntil 不能早于 createdSince"); if(!blank(a)) OffsetDateTime.parse(a); if(!blank(b)) OffsetDateTime.parse(b); } catch(java.time.format.DateTimeParseException e){ throw new IllegalArgumentException("创建时间必须为 ISO 8601 日期时间格式",e); } }
    private String query(Map<String,String> q){ return q.entrySet().stream().map(e->encode(e.getKey())+"="+encode(e.getValue())).collect(Collectors.joining("&")); }
    private String encode(String v){ return URLEncoder.encode(v,StandardCharsets.UTF_8).replace("+","%20").replace("%7E","~"); }
    private String encodePath(String v){ return org.springframework.web.util.UriUtils.encodePathSegment(v,StandardCharsets.UTF_8); }
    private void put(Map<String,String> q,String k,String v){if(!blank(v))q.put(k,v);} private String join(List<String> v){return empty(v)?null:String.join(",",v);} private boolean blank(String v){return v==null||v.isBlank();} private boolean empty(Collection<?> v){return v==null||v.isEmpty();}
}
