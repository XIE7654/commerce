package cn.iocoder.yudao.module.amazon.controller.admin.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Amazon Orders 2026-01-01 指定订单查询请求参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonOrder2026GetReqVO extends AmazonOrderGetReqVO {

    @Schema(description = "需要返回的数据集")
    private List<String> includedData;

}
