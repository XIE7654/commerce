package cn.iocoder.yudao.module.amazon.service.awd;

import cn.iocoder.yudao.module.amazon.controller.admin.awd.vo.AwdRequestVO;
import java.util.Map;

/** Amazon Warehousing and Distribution (AWD) 服务。 */
public interface AwdService {
    /** 调用 AWD 指定操作并返回 Amazon 原始 JSON。 */
    Map<String, Object> invoke(AwdRequestVO request, String operation, String method, String resourcePath);
}
