package cn.iocoder.yudao.module.amazon.service.fbainboundeligibility;

import cn.iocoder.yudao.module.amazon.controller.admin.fulfillment.vo.AmazonFulfillmentApiReqVO;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.service.fulfillment.AmazonFulfillmentApiServiceSupport;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Amazon FBA Inbound Eligibility API 服务实现。 */
@Service
public class FbaInboundEligibilityServiceImpl extends AmazonFulfillmentApiServiceSupport implements FbaInboundEligibilityService {

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getItemEligibilityPreview(AmazonFulfillmentApiReqVO request) {
        if (request.getQuery() == null || isBlank(request.getQuery().get("asin")) || isBlank(request.getQuery().get("program"))) {
            throw new IllegalArgumentException("asin 和 program 不能为空");
        }
        return invoke(request, "getItemEligibilityPreview", new OperationDefinition(HttpMethod.GET,
                "/fba/inbound/v1/eligibility/itemPreview"), AmazonApiCategory.FULFILLMENT_BY_AMAZON,
                "item-eligibility-preview");
    }

    /**
     * 判断必填查询参数是否为空白。
     *
     * @param value 参数值
     * @return 参数为空或仅有空白字符时返回 true
     */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
