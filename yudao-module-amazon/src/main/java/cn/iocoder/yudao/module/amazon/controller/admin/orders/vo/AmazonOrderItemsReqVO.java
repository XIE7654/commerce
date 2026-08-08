package cn.iocoder.yudao.module.amazon.controller.admin.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Amazon 订单商品分页查询请求参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonOrderItemsReqVO extends AmazonOrderGetReqVO {

    @Schema(description = "上一页返回的分页令牌")
    private String nextToken;

}
