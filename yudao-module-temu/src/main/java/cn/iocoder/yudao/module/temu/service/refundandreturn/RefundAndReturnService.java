package cn.iocoder.yudao.module.temu.service.refundandreturn;

import cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo.RefundAndReturnAftersalesListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo.RefundAndReturnOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo.RefundAndReturnParentAftersalesListReqVO;
import tools.jackson.databind.JsonNode;

/**
 * Temu Refund And Return 退款退货业务 Service。
 */
public interface RefundAndReturnService {

    /**
     * 查询 Temu 父售后单列表。
     *
     * @param request 父售后单分页与筛选参数
     * @return Temu 官方父售后单列表响应
     */
    JsonNode getParentAftersaleOrderList(RefundAndReturnParentAftersalesListReqVO request);

    /**
     * 查询 Temu 售后单列表。
     *
     * @param request 售后单分页与父售后单筛选参数
     * @return Temu 官方售后单列表响应
     */
    JsonNode getAftersaleOrderList(RefundAndReturnAftersalesListReqVO request);

    /**
     * 查询 Temu 退货单信息。
     *
     * @param request 父售后单与售后单查询参数
     * @return Temu 官方退货单响应
     */
    JsonNode getReturnOrderList(RefundAndReturnOrderReqVO request);
}
