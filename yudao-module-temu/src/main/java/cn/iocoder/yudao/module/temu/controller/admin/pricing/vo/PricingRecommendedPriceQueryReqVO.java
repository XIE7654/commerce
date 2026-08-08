package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** 查询 Temu 推荐价请求参数。 */
@Data
public class PricingRecommendedPriceQueryReqVO extends PricingBaseReqVO {
    /** 推荐价类型，取值以 Temu 平台定义为准。 */ @NotNull(message = "推荐价类型不能为空") private Integer recommendedPriceType;
    /** Temu 商品 ID 列表。 */ @NotEmpty(message = "商品 ID 列表不能为空") private List<Long> goodsIdList;
    /** 返回文案语言。 */ @NotBlank(message = "语言不能为空") private String language;
}
