package cn.iocoder.yudao.module.amazon.controller.admin.products.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Amazon Products 相关 API 的统一请求参数。
 *
 * <p>不同接口仅使用其对应字段；费用和价格接口的 Amazon 原始请求体由 {@link #body} 透传，
 * 以保留官方模型中的可扩展字段。</p>
 */
@Data
public class AmazonProductsReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "目标站点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "ASIN；查询 Catalog Item、Catalog 分类或按 ASIN 预估费用时必填", example = "B000000000")
    private String asin;

    @Schema(description = "卖家 SKU；查询 Catalog 分类或按 SKU 预估费用时必填", example = "SKU-001")
    private String sellerSku;

    @Schema(description = "Catalog 商品标识符，最多 20 个；须与 identifiersType 配对", example = "B000000000,B000000001")
    @Size(max = 20, message = "商品标识符最多 20 个")
    private List<String> identifiers;

    @Schema(description = "Catalog 商品标识符类型", example = "ASIN")
    private String identifiersType;

    @Schema(description = "Catalog 关键字，最多 20 个；与 identifiers 二选一", example = "wireless,headphones")
    @Size(max = 20, message = "关键字最多 20 个")
    private List<String> keywords;

    @Schema(description = "品牌名称筛选", example = "AmazonBasics")
    private List<String> brandNames;

    @Schema(description = "商品分类节点编号筛选", example = "123456")
    private List<String> classificationIds;

    @Schema(description = "需要返回的 Catalog 数据集", example = "summaries,attributes,images")
    private List<String> includedData;

    @Schema(description = "内容本地化语言", example = "en_US")
    private String locale;

    @Schema(description = "关键字搜索使用的语言", example = "en_US")
    private String keywordsLocale;

    @Schema(description = "单页数量", example = "10")
    private Integer pageSize;

    @Schema(description = "分页令牌")
    private String pageToken;

    @Schema(description = "Amazon 商品类型；查询商品类型定义时必填", example = "PRODUCT")
    private String productType;

    @Schema(description = "商品名称，用于搜索商品类型定义", example = "Wireless Headphones")
    private String itemName;

    @Schema(description = "商品类型定义搜索语言", example = "en_US")
    private String searchLocale;

    @Schema(description = "商品类型定义版本", example = "U8L4z5cB1")
    private String productTypeVersion;

    @Schema(description = "商品类型定义要求级别", example = "LISTING")
    private String requirements;

    @Schema(description = "是否强制商品类型定义要求", example = "ENFORCED")
    private String requirementsEnforced;

    @Schema(description = "变体层级", example = "PARENT")
    private String parentageLevel;

    @Schema(description = "价格或费用接口的 Amazon 原始请求体；批量费用接口按官方模型传 JSON 数组", example = "{\"requests\":[{\"marketplaceId\":\"ATVPDKIKX0DER\",\"asin\":\"B000000000\"}]}")
    private Object body;
}
