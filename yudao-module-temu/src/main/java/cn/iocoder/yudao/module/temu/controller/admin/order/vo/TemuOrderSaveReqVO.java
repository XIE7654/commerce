package cn.iocoder.yudao.module.temu.controller.admin.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - Temu 订单新增/修改 Request VO")
@Data
public class TemuOrderSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19826")
    private Long id;

    @Schema(description = "关联 temu_shop.id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4092")
    @NotNull(message = "关联 temu_shop.id不能为空")
    private Long shopId;

    @Schema(description = "关联 temu_seller.id，由店铺授权关系确定", requiredMode = Schema.RequiredMode.REQUIRED, example = "1515")
    @NotNull(message = "关联 temu_seller.id，由店铺授权关系确定不能为空")
    private Long sellerId;

    @Schema(description = "Temu 父订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Temu 父订单号不能为空")
    private String parentOrderSn;

    @Schema(description = "Temu 子订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Temu 子订单号不能为空")
    private String orderSn;

    @Schema(description = "Temu 站点编号", example = "23343")
    private Integer siteId;

    @Schema(description = "Temu 区域编号", example = "21232")
    private Long regionId;

    @Schema(description = "父订单状态", example = "1")
    private Integer parentOrderStatus;

    @Schema(description = "子订单状态", example = "2")
    private Integer orderStatus;

    @Schema(description = "父订单支付类型", example = "2")
    private String parentOrderPaymentType;

    @Schema(description = "子订单支付类型", example = "2")
    private String orderPaymentType;

    @Schema(description = "履约方式", example = "2")
    private String fulfillmentType;

    @Schema(description = "Temu 商品编号", example = "23194")
    private Long goodsId;

    @Schema(description = "Temu SKU 编号", example = "12776")
    private Long skuId;

    @Schema(description = "商品名称", example = "芋艿")
    private String goodsName;

    @Schema(description = "原始商品名称", example = "赵六")
    private String originalGoodsName;

    @Schema(description = "商品规格")
    private String spec;

    @Schema(description = "原始商品规格", example = "赵六")
    private String originalSpecName;

    @Schema(description = "商品缩略图", example = "https://www.iocoder.cn")
    private String thumbUrl;

    @Schema(description = "下单数量")
    private Integer quantity;

    @Schema(description = "发货前取消数量")
    private Integer canceledQuantityBeforeShipment;

    @Schema(description = "原始下单数量")
    private Integer originalOrderQuantity;

    @Schema(description = "父订单发货方式")
    private Integer shippingMethod;

    @Schema(description = "是否由主商城合单发货")
    private Boolean shipmentConsolidatedByMainMall;

    @Schema(description = "是否含运费")
    private Boolean hasShippingFee;

    @Schema(description = "父订单创建时间")
    private LocalDateTime parentOrderTime;

    @Schema(description = "父订单确认时间")
    private LocalDateTime parentConfirmTime;

    @Schema(description = "子订单创建时间")
    private LocalDateTime orderCreateTime;

    @Schema(description = "要求发货时间")
    private LocalDateTime orderShippingTime;

    @Schema(description = "最晚预计发货时间")
    private LocalDateTime expectShipLatestTime;

    @Schema(description = "最晚送达时间")
    private LocalDateTime latestDeliveryTime;

    @Schema(description = "Temu 订单最后更新时间")
    private LocalDateTime temuUpdateTime;

    @Schema(description = "父订单标签 JSON")
    private String parentOrderLabels;

    @Schema(description = "子订单标签 JSON")
    private String orderLabels;

    @Schema(description = "父订单履约预警 JSON")
    private String parentFulfillmentWarnings;

    @Schema(description = "子订单履约预警 JSON")
    private String fulfillmentWarnings;

    @Schema(description = "包裹异常类型 JSON")
    private String packageAbnormalTypes;

    @Schema(description = "商品映射列表 JSON")
    private String productList;

    @Schema(description = "最近同步时间")
    private LocalDateTime lastSyncTime;

}