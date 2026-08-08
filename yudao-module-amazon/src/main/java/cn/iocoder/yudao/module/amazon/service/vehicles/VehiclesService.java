package cn.iocoder.yudao.module.amazon.service.vehicles;

import cn.iocoder.yudao.module.amazon.controller.admin.vehicles.vo.VehiclesListReqVO;

import java.util.Map;

/** Amazon Vehicles API 服务。 */
public interface VehiclesService {

    /**
     * 分页获取指定 Marketplace 的车型目录。
     *
     * @param request 店铺、站点及分页查询条件
     * @return Amazon 返回的车型目录 JSON
     */
    Map<String, Object> getVehicles(VehiclesListReqVO request);
}
