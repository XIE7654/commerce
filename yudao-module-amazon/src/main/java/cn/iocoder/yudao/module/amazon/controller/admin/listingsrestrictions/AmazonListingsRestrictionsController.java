package cn.iocoder.yudao.module.amazon.controller.admin.listingsrestrictions;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsRestrictionsReqVO;
import cn.iocoder.yudao.module.amazon.service.listingsrestrictions.AmazonListingsRestrictionsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Amazon Listings Restrictions 管理接口。
 */
@Tag(name = "管理后台 - Amazon Listings Restrictions")
@RestController
@RequestMapping("/amazon/listings-restrictions")
public class AmazonListingsRestrictionsController {
    @Resource
    private AmazonListingsRestrictionsService service;

    @PostMapping("/get")
    @PreAuthorize("@ss.hasPermission('amazon:listings-restrictions:query')")
    public CommonResult<Map<String, Object>> get(@Valid @RequestBody AmazonListingsRestrictionsReqVO r) {
        return CommonResult.success(service.get(r));
    }
}
