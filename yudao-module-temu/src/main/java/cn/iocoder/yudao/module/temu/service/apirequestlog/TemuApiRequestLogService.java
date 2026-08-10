package cn.iocoder.yudao.module.temu.service.apirequestlog;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.temu.controller.admin.apirequestlog.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.apirequestlog.TemuApiRequestLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.http.HttpHeaders;
import tools.jackson.databind.JsonNode;

/**
 * Temu OpenAPI 请求调用日志 Service 接口
 *
 * @author 自达源码
 */
public interface TemuApiRequestLogService {

    /**
     * 记录一次 Temu OpenAPI 调用的最终结果；日志写入失败不得影响原始调用。
     *
     * @param context 请求审计上下文
     * @param httpStatusCode HTTP 状态码，网络异常时为空
     * @param responseHeaders 响应头，网络异常时为空
     * @param responseBody 已解析的响应体，异常或解析失败时为空
     * @param exception 请求异常，成功时为空
     */
    void log(TemuApiRequestLogContext context, Integer httpStatusCode, HttpHeaders responseHeaders,
             JsonNode responseBody, Throwable exception);

    void updateFileId(String requestId, Long fileId);

    /**
     * 创建Temu OpenAPI 请求调用日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createApiRequestLog(@Valid TemuApiRequestLogSaveReqVO createReqVO);

    /**
     * 更新Temu OpenAPI 请求调用日志
     *
     * @param updateReqVO 更新信息
     */
    void updateApiRequestLog(@Valid TemuApiRequestLogSaveReqVO updateReqVO);

    /**
     * 删除Temu OpenAPI 请求调用日志
     *
     * @param id 编号
     */
    void deleteApiRequestLog(Long id);

    /**
    * 批量删除Temu OpenAPI 请求调用日志
    *
    * @param ids 编号
    */
    void deleteApiRequestLogListByIds(List<Long> ids);

    /**
     * 获得Temu OpenAPI 请求调用日志
     *
     * @param id 编号
     * @return Temu OpenAPI 请求调用日志
     */
    TemuApiRequestLogDO getApiRequestLog(Long id);

    /**
     * 获得Temu OpenAPI 请求调用日志分页
     *
     * @param pageReqVO 分页查询
     * @return Temu OpenAPI 请求调用日志分页
     */
    PageResult<TemuApiRequestLogDO> getApiRequestLogPage(TemuApiRequestLogPageReqVO pageReqVO);

}
