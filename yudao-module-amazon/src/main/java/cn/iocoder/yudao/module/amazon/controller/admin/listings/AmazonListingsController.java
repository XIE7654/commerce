package cn.iocoder.yudao.module.amazon.controller.admin.listings;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.service.listings.AmazonListingsService;
import com.amazon.SellingPartnerAPIAA.LWAException;
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
import software.amazon.spapi.ApiException;
import software.amazon.spapi.models.listings.items.v2021_08_01.Item;
import software.amazon.spapi.models.listings.items.v2021_08_01.ItemSearchResults;

/** Amazon Listings Items 管理接口。 */
@Tag(name = "管理后台 - Amazon Listings")
@RestController
@RequestMapping("/amazon/listings")
@Validated
public class AmazonListingsController {
    @Resource private AmazonListingsService amazonListingsService;

    /** 查询指定店铺已同步站点的 Listings Items。 */
    @PostMapping("/search")
    @Operation(summary = "查询 Amazon Listings Items")
    @PreAuthorize("@ss.hasPermission('amazon:listings:query')")
    public CommonResult<ItemSearchResults> search(@Valid @RequestBody AmazonListingsSearchReqVO request) throws ApiException, LWAException {
        return CommonResult.success(amazonListingsService.searchListingsItems(request));
    }

    /** 根据 SKU 查询指定店铺在目标国家站点的单个 Listings Item。 */
    @PostMapping("/item")
    @Operation(summary = "按 SKU 查询 Amazon Listings Item")
    @PreAuthorize("@ss.hasPermission('amazon:listings:query')")
    public CommonResult<Item> getItem(@Valid @RequestBody AmazonListingsItemGetReqVO request) throws ApiException, LWAException {
        return CommonResult.success(amazonListingsService.getListingsItem(request));
    }
}
