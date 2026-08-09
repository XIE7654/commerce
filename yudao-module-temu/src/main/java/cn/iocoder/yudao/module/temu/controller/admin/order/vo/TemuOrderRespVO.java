package cn.iocoder.yudao.module.temu.controller.admin.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - Temu 订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TemuOrderRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19826")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "关联 temu_shop.id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4092")
    @ExcelProperty("关联 temu_shop.id")
    private Long shopId;

    @Schema(description = "关联 temu_seller.id，由店铺授权关系确定", requiredMode = Schema.RequiredMode.REQUIRED, example = "1515")
    @ExcelProperty("关联 temu_seller.id，由店铺授权关系确定")
    private Long sellerId;

    @Schema(description = "Temu 父订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Temu 父订单号")
    private String parentOrderSn;

    @Schema(description = "Temu 子订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Temu 子订单号")
    private String orderSn;

    @Schema(description = "Temu 站点编号", example = "23343")
    @ExcelProperty("Temu 站点编号")
    private Integer siteId;

    @Schema(description = "Temu 区域编号", example = "21232")
    @ExcelProperty("Temu 区域编号")
    private Long regionId;

    @Schema(description = "父订单状态", example = "1")
    @ExcelProperty("父订单状态")
    private Integer parentOrderStatus;

    @Schema(description = "子订单状态", example = "2")
    @ExcelProperty("子订单状态")
    private Integer orderStatus;

    @Schema(description = "父订单支付类型", example = "2")
    @ExcelProperty("父订单支付类型")
    private String parentOrderPaymentType;

    @Schema(description = "子订单支付类型", example = "2")
    @ExcelProperty("子订单支付类型")
    private String orderPaymentType;

    @Schema(description = "履约方式", example = "2")
    @ExcelProperty("履约方式")
    private String fulfillmentType;

    @Schema(description = "Temu 商品编号", example = "23194")
    @ExcelProperty("Temu 商品编号")
    private Long goodsId;

    @Schema(description = "Temu SKU 编号", example = "12776")
    @ExcelProperty("Temu SKU 编号")
    private Long skuId;

    @Schema(description = "商品名称", example = "芋艿")
    @ExcelProperty("商品名称")
    private String goodsName;

    @Schema(description = "原始商品名称", example = "赵六")
    @ExcelProperty("原始商品名称")
    private String originalGoodsName;

    @Schema(description = "商品规格")
    @ExcelProperty("商品规格")
    private String spec;

    @Schema(description = "原始商品规格", example = "赵六")
    @ExcelProperty("原始商品规格")
    private String originalSpecName;

    @Schema(description = "商品缩略图", example = "https://www.iocoder.cn")
    @ExcelProperty("商品缩略图")
    private String thumbUrl;

    @Schema(description = "下单数量")
    @ExcelProperty("下单数量")
    private Integer quantity;

    @Schema(description = "发货前取消数量")
    @ExcelProperty("发货前取消数量")
    private Integer canceledQuantityBeforeShipment;

    @Schema(description = "原始下单数量")
    @ExcelProperty("原始下单数量")
    private Integer originalOrderQuantity;

    @Schema(description = "父订单发货方式")
    @ExcelProperty("父订单发货方式")
    private Integer shippingMethod;

    @Schema(description = "是否由主商城合单发货")
    @ExcelProperty("是否由主商城合单发货")
    private Boolean shipmentConsolidatedByMainMall;

    @Schema(description = "是否含运费")
    @ExcelProperty("是否含运费")
    private Boolean hasShippingFee;

    @Schema(description = "父订单创建时间")
    @ExcelProperty("父订单创建时间")
    private LocalDateTime parentOrderTime;

    @Schema(description = "父订单确认时间")
    @ExcelProperty("父订单确认时间")
    private LocalDateTime parentConfirmTime;

    @Schema(description = "子订单创建时间")
    @ExcelProperty("子订单创建时间")
    private LocalDateTime orderCreateTime;

    @Schema(description = "要求发货时间")
    @ExcelProperty("要求发货时间")
    private LocalDateTime orderShippingTime;

    @Schema(description = "最晚预计发货时间")
    @ExcelProperty("最晚预计发货时间")
    private LocalDateTime expectShipLatestTime;

    @Schema(description = "最晚送达时间")
    @ExcelProperty("最晚送达时间")
    private LocalDateTime latestDeliveryTime;

    @Schema(description = "Temu 订单最后更新时间")
    @ExcelProperty("Temu 订单最后更新时间")
    private LocalDateTime temuUpdateTime;

    @Schema(description = "父订单标签 JSON")
    @ExcelProperty("父订单标签 JSON")
    private String parentOrderLabels;

    @Schema(description = "子订单标签 JSON")
    @ExcelProperty("子订单标签 JSON")
    private String orderLabels;

    @Schema(description = "父订单履约预警 JSON")
    @ExcelProperty("父订单履约预警 JSON")
    private String parentFulfillmentWarnings;

    @Schema(description = "子订单履约预警 JSON")
    @ExcelProperty("子订单履约预警 JSON")
    private String fulfillmentWarnings;

    @Schema(description = "包裹异常类型 JSON")
    @ExcelProperty("包裹异常类型 JSON")
    private String packageAbnormalTypes;

    @Schema(description = "商品映射列表 JSON")
    @ExcelProperty("商品映射列表 JSON")
    private String productList;

    @Schema(description = "最近同步时间")
    @ExcelProperty("最近同步时间")
    private LocalDateTime lastSyncTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}