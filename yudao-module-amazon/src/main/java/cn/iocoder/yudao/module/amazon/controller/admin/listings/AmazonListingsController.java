package cn.iocoder.yudao.module.amazon.controller.admin.listings;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.service.listings.AmazonListingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Amazon Listings Items 管理接口。
 */
@Tag(name = "管理后台 - Amazon Listings")
@RestController
@RequestMapping("/amazon/listings")
@Validated
public class AmazonListingsController {

    @Resource
    private AmazonListingsService amazonListingsService;

    /**
     * 查询指定店铺在目标国家站点的 Listings Items，不保存 Amazon 返回数据。
     *
     * @param request 店铺、国家代码及查询筛选条件
     * @return Amazon Listings Items 原始响应
     */
    @PostMapping("/search")
    @Operation(summary = "查询 Amazon Listings Items")
    @PreAuthorize("@ss.hasPermission('amazon:listings:query')")
    public CommonResult<Map<String, Object>> search(@Valid @RequestBody AmazonListingsSearchReqVO request) {
        return CommonResult.success(amazonListingsService.searchListingsItems(request));
    }
}
