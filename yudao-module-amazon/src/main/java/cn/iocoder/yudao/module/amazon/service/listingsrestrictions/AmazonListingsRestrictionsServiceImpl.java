package cn.iocoder.yudao.module.amazon.service.listingsrestrictions;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsRestrictionsReqVO; import cn.iocoder.yudao.module.amazon.service.listings.AmazonListingsService; import jakarta.annotation.Resource; import org.springframework.stereotype.Service; import java.util.Map;
/** Listings Restrictions API 服务实现。 */
@Service public class AmazonListingsRestrictionsServiceImpl implements AmazonListingsRestrictionsService { @Resource private AmazonListingsService delegate; public Map<String,Object> get(AmazonListingsRestrictionsReqVO r){return delegate.getListingsRestrictions(r);} }
