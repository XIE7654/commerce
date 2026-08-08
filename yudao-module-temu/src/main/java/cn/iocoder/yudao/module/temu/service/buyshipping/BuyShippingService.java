package cn.iocoder.yudao.module.temu.service.buyshipping;

import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingLabelReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingServicesReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipLaterConfirmReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipLaterPackagesReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipmentCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipmentUpdateReqVO;
import tools.jackson.databind.JsonNode;

/**
 * Temu Buy Shipping 购单发货业务 Service。
 */
public interface BuyShippingService {

    /**
     * 查询订单可购买的物流服务。
     *
     * @param request 仓库、包裹尺寸和订单参数
     * @return Temu 官方物流服务响应
     */
    JsonNode getShippingServices(BuyShippingServicesReqVO request);

    /**
     * 查询可用于购单发货的仓库。
     *
     * @param request 站点和授权参数
     * @return Temu 官方仓库列表响应
     */
    JsonNode getWarehouseList(BuyShippingBaseReqVO request);

    /**
     * 创建 Temu 购单发货货件。
     *
     * @param request 发货方式和货件明细
     * @return Temu 官方创建货件响应
     */
    JsonNode createShipment(BuyShippingShipmentCreateReqVO request);

    /**
     * 更新需重试的 Temu 购单发货货件。
     *
     * @param request 重试包裹明细
     * @return Temu 官方更新货件响应
     */
    JsonNode updateShipment(BuyShippingShipmentUpdateReqVO request);

    /**
     * 获取已创建货件的运输面单。
     *
     * @param request 包裹编号列表
     * @return Temu 官方面单响应
     */
    JsonNode getShippingLabel(BuyShippingLabelReqVO request);

    /**
     * 分页查询待确认发货的包裹。
     *
     * @param request 分页查询参数
     * @return Temu 官方待发货包裹响应
     */
    JsonNode getShipLaterPackages(BuyShippingShipLaterPackagesReqVO request);

    /**
     * 确认待发货包裹已交由承运商。
     *
     * @param request 包裹发货确认明细
     * @return Temu 官方发货确认响应
     */
    JsonNode confirmShipLaterPackagesShipped(BuyShippingShipLaterConfirmReqVO request);
}
