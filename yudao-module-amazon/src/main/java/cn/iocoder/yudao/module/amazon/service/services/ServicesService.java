package cn.iocoder.yudao.module.amazon.service.services;

import cn.iocoder.yudao.module.amazon.controller.admin.services.vo.ServicesReqVO;
import java.util.Map;

/** Amazon Services 服务。 */
public interface ServicesService {
    /** 查询服务工单。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> getServiceJobByServiceJobId(ServicesReqVO request);
    /** 取消服务工单。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> cancelServiceJobByServiceJobId(ServicesReqVO request);
    /** 完成服务工单。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> completeServiceJobByServiceJobId(ServicesReqVO request);
    /** 查询服务工单列表。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> getServiceJobs(ServicesReqVO request);
    /** 创建预约。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> addAppointmentForServiceJobByServiceJobId(ServicesReqVO request);
    /** 改约。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> rescheduleAppointmentForServiceJobByServiceJobId(ServicesReqVO request);
    /** 分配预约资源。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> assignAppointmentResources(ServicesReqVO request);
    /** 设置预约履约数据。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> setAppointmentFulfillmentData(ServicesReqVO request);
    /** 查询时段范围容量。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> getRangeSlotCapacity(ServicesReqVO request);
    /** 查询固定时段容量。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> getFixedSlotCapacity(ServicesReqVO request);
    /** 更新资源日程。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> updateSchedule(ServicesReqVO request);
    /** 创建预约预留。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> createReservation(ServicesReqVO request);
    /** 更新预约预留。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> updateReservation(ServicesReqVO request);
    /** 取消预约预留。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> cancelReservation(ServicesReqVO request);
    /** 查询工单可用预约时段。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> getAppointmmentSlotsByJobId(ServicesReqVO request);
    /** 查询通用预约时段。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> getAppointmentSlots(ServicesReqVO request);
    /** 创建服务文档上传地址。 @param request 请求参数 @return Amazon 原始响应 */ Map<String, Object> createServiceDocumentUploadDestination(ServicesReqVO request);
}
