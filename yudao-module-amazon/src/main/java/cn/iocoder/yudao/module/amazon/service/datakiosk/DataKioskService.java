package cn.iocoder.yudao.module.amazon.service.datakiosk;

import cn.iocoder.yudao.module.amazon.controller.admin.datakiosk.vo.*;
import java.util.Map;

/** Data Kiosk API 服务。 */
public interface DataKioskService {
    /**
     * 创建异步 GraphQL 查询任务。
     * @param request 店铺、站点、查询语句与可选分页令牌
     * @return 包含 Amazon 查询任务编号的原始响应
     */ Map<String, Object> createQuery(DataKioskCreateQueryReqVO request);
    /**
     * 获取符合筛选条件的查询任务。
     * @param request 店铺、站点与分页筛选条件
     * @return Amazon 原始响应
     */ Map<String, Object> getQueries(DataKioskQueriesReqVO request);
    /**
     * 获取单个查询任务详情。
     * @param request 店铺、站点与查询任务编号
     * @return Amazon 原始响应
     */ Map<String, Object> getQuery(DataKioskQueryIdReqVO request);
    /**
     * 取消尚未结束的查询任务；成功时 Amazon 返回空响应。
     * @param request 店铺、站点与查询任务编号
     * @return 空 Map 或 Amazon 原始响应
     */ Map<String, Object> cancelQuery(DataKioskQueryIdReqVO request);
    /**
     * 获取文档的短效下载 URL。
     * @param request 店铺、站点与文档编号
     * @return Amazon 原始响应
     */ Map<String, Object> getDocument(DataKioskDocumentIdReqVO request);
}
