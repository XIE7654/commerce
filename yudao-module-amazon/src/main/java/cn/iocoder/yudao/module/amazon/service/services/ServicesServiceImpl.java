package cn.iocoder.yudao.module.amazon.service.services;

import cn.iocoder.yudao.module.amazon.controller.admin.services.vo.ServicesReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Amazon Services 服务实现。 */
@Service
public class ServicesServiceImpl implements ServicesService {
    @Resource private AmazonOAuthService amazonOAuthService; @Resource private AmazonShopMapper amazonShopMapper; @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;
    /** {@inheritDoc} */ @Override public Map<String,Object> getServiceJobByServiceJobId(ServicesReqVO r){return invoke(r,HttpMethod.GET,"/serviceJobs/"+job(r),"getServiceJobByServiceJobId");}
    /** {@inheritDoc} */ @Override public Map<String,Object> cancelServiceJobByServiceJobId(ServicesReqVO r){return invoke(r,HttpMethod.PUT,"/serviceJobs/"+job(r)+"/cancellations","cancelServiceJobByServiceJobId");}
    /** {@inheritDoc} */ @Override public Map<String,Object> completeServiceJobByServiceJobId(ServicesReqVO r){return invoke(r,HttpMethod.PUT,"/serviceJobs/"+job(r)+"/completions","completeServiceJobByServiceJobId");}
    /** {@inheritDoc} */ @Override public Map<String,Object> getServiceJobs(ServicesReqVO r){return invoke(r,HttpMethod.GET,"/serviceJobs","getServiceJobs");}
    /** {@inheritDoc} */ @Override public Map<String,Object> addAppointmentForServiceJobByServiceJobId(ServicesReqVO r){return invoke(r,HttpMethod.POST,"/serviceJobs/"+job(r)+"/appointments","addAppointmentForServiceJobByServiceJobId");}
    /** {@inheritDoc} */ @Override public Map<String,Object> rescheduleAppointmentForServiceJobByServiceJobId(ServicesReqVO r){return invoke(r,HttpMethod.POST,"/serviceJobs/"+job(r)+"/appointments/"+appointment(r),"rescheduleAppointmentForServiceJobByServiceJobId");}
    /** {@inheritDoc} */ @Override public Map<String,Object> assignAppointmentResources(ServicesReqVO r){return invoke(r,HttpMethod.PUT,"/serviceJobs/"+job(r)+"/appointments/"+appointment(r)+"/resources","assignAppointmentResources");}
    /** {@inheritDoc} */ @Override public Map<String,Object> setAppointmentFulfillmentData(ServicesReqVO r){return invoke(r,HttpMethod.PUT,"/serviceJobs/"+job(r)+"/appointments/"+appointment(r)+"/fulfillment","setAppointmentFulfillmentData");}
    /** {@inheritDoc} */ @Override public Map<String,Object> getRangeSlotCapacity(ServicesReqVO r){return invoke(r,HttpMethod.POST,"/serviceResources/"+resource(r)+"/capacity/range","getRangeSlotCapacity");}
    /** {@inheritDoc} */ @Override public Map<String,Object> getFixedSlotCapacity(ServicesReqVO r){return invoke(r,HttpMethod.POST,"/serviceResources/"+resource(r)+"/capacity/fixed","getFixedSlotCapacity");}
    /** {@inheritDoc} */ @Override public Map<String,Object> updateSchedule(ServicesReqVO r){return invoke(r,HttpMethod.PUT,"/serviceResources/"+resource(r)+"/schedules","updateSchedule");}
    /** {@inheritDoc} */ @Override public Map<String,Object> createReservation(ServicesReqVO r){return invoke(r,HttpMethod.POST,"/reservation","createReservation");}
    /** {@inheritDoc} */ @Override public Map<String,Object> updateReservation(ServicesReqVO r){return invoke(r,HttpMethod.PUT,"/reservation/"+reservation(r),"updateReservation");}
    /** {@inheritDoc} */ @Override public Map<String,Object> cancelReservation(ServicesReqVO r){return invoke(r,HttpMethod.DELETE,"/reservation/"+reservation(r),"cancelReservation");}
    /** {@inheritDoc} */ @Override public Map<String,Object> getAppointmmentSlotsByJobId(ServicesReqVO r){return invoke(r,HttpMethod.GET,"/serviceJobs/"+job(r)+"/appointmentSlots","getAppointmmentSlotsByJobId");}
    /** {@inheritDoc} */ @Override public Map<String,Object> getAppointmentSlots(ServicesReqVO r){required(r.getAsin(),"asin");required(r.getStoreId(),"storeId");return invoke(r,HttpMethod.GET,"/appointmentSlots","getAppointmentSlots");}
    /** {@inheritDoc} */ @Override public Map<String,Object> createServiceDocumentUploadDestination(ServicesReqVO r){return invoke(r,HttpMethod.POST,"/documents","createServiceDocumentUploadDestination");}
    /** 统一构建 Services API 查询参数；未指定 marketplaceIds 时限制在当前国家默认站点。 */
    private Map<String,Object> invoke(ServicesReqVO r,HttpMethod method,String path,String operation){AmazonShopDO shop=shop(r.getShopId());AmazonMarketplaceEnum marketplace=marketplace(r.getCountryCode());Map<String,String> query=new LinkedHashMap<>();if(r.getQuery()!=null)query.putAll(r.getQuery());query.put("marketplaceIds",r.getMarketplaceIds()==null||r.getMarketplaceIds().isEmpty()?marketplace.getMarketplaceId():String.join(",",r.getMarketplaceIds()));if("getAppointmentSlots".equals(operation)){query.put("asin",r.getAsin());query.put("storeId",r.getStoreId());}URI uri=URI.create(marketplace.getEndpoint()+"/service/v1"+path+"?"+query(query));String token=amazonOAuthService.getSellerAccessToken(shop.getId());return method==HttpMethod.GET?amazonSellingPartnerClient.getByCategory(uri,token,AmazonApiCategory.SERVICES,operation,operation,shop.getId(),r.getCountryCode(),marketplace.getMarketplaceId()):amazonSellingPartnerClient.mutateByCategoryOptional(uri,token,method,r.getBody(),AmazonApiCategory.SERVICES,operation,operation,shop.getId(),r.getCountryCode(),marketplace.getMarketplaceId());}
    /** 将查询字段以 RFC 3986 形式编码，防止日期和分页令牌改变 URI 语义。 */ private String query(Map<String,String> values){return values.entrySet().stream().filter(e->e.getValue()!=null&&!e.getValue().isBlank()).map(e->UriUtils.encodeQueryParam(e.getKey(),StandardCharsets.UTF_8)+"="+UriUtils.encodeQueryParam(e.getValue(),StandardCharsets.UTF_8)).collect(Collectors.joining("&"));}
    /** 校验并编码服务工单编号。 */ private String job(ServicesReqVO r){return id(r.getServiceJobId(),"serviceJobId");}
    /** 校验并编码预约编号。 */ private String appointment(ServicesReqVO r){return id(r.getAppointmentId(),"appointmentId");}
    /** 校验并编码服务资源编号。 */ private String resource(ServicesReqVO r){return id(r.getResourceId(),"resourceId");}
    /** 校验并编码预约预留编号。 */ private String reservation(ServicesReqVO r){return id(r.getReservationId(),"reservationId");}
    /** 校验必填标识符。 */ private String id(String value,String name){required(value,name);return UriUtils.encodePathSegment(value,StandardCharsets.UTF_8);}
    /** 拒绝空白的官方必填字段。 */ private void required(String value,String name){if(value==null||value.isBlank())throw new IllegalArgumentException(name+" 不能为空");}
    /** 获取当前租户店铺。 */ private AmazonShopDO shop(Long shopId){AmazonShopDO value=amazonShopMapper.selectById(shopId);if(value==null)throw new IllegalArgumentException("Amazon 店铺不存在: "+shopId);return value;}
    /** 解析 Amazon 调用站点。 */ private AmazonMarketplaceEnum marketplace(String code){AmazonMarketplaceEnum value=AmazonMarketplaceEnum.fromCountryCode(code);if(value==null)throw new IllegalArgumentException("不支持的 Amazon 国家代码: "+code);return value;}
}
