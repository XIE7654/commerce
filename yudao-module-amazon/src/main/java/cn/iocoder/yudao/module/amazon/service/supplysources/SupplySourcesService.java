package cn.iocoder.yudao.module.amazon.service.supplysources;

import cn.iocoder.yudao.module.amazon.controller.admin.supplysources.vo.SupplySourcesReqVO;
import java.util.Map;

/** Amazon Supply Sources 服务。 */
public interface SupplySourcesService {
    /** 查询供货源列表。 @param request 店铺、站点和分页参数 @return Amazon 原始响应 */ Map<String, Object> getSupplySources(SupplySourcesReqVO request);
    /** 创建供货源。 @param request 店铺、站点和官方请求体 @return Amazon 原始响应 */ Map<String, Object> createSupplySource(SupplySourcesReqVO request);
    /** 查询供货源详情。 @param request 店铺、站点和供货源编号 @return Amazon 原始响应 */ Map<String, Object> getSupplySource(SupplySourcesReqVO request);
    /** 更新供货源。 @param request 店铺、站点和官方请求体 @return Amazon 原始响应 */ Map<String, Object> updateSupplySource(SupplySourcesReqVO request);
    /** 归档供货源。 @param request 店铺、站点和供货源编号 @return Amazon 原始响应 */ Map<String, Object> archiveSupplySource(SupplySourcesReqVO request);
    /** 更新供货源状态。 @param request 店铺、站点和官方请求体 @return Amazon 原始响应 */ Map<String, Object> updateSupplySourceStatus(SupplySourcesReqVO request);
}
