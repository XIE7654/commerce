package cn.iocoder.yudao.module.amazon.service.tracking;

import cn.iocoder.yudao.module.amazon.controller.admin.tracking.vo.TrackingShipmentReqVO;

import java.util.Map;

/** Amazon Tracking API 服务。 */
public interface TrackingService {

    /**
     * 按货件标识查询物流轨迹。
     *
     * @param request 店铺、站点和物流标识
     * @return Amazon 返回的物流轨迹 JSON
     */
    Map<String, Object> getShipmentTracking(TrackingShipmentReqVO request);
}
