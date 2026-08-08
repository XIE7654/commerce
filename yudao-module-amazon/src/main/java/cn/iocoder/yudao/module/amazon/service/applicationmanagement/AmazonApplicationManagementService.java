package cn.iocoder.yudao.module.amazon.service.applicationmanagement;

import cn.iocoder.yudao.module.amazon.controller.admin.applicationmanagement.vo.AmazonApplicationManagementReqVO;

/** Amazon Application Management 服务。 */
public interface AmazonApplicationManagementService {

    /**
     * 轮换开发者应用的 Client Secret，新的密钥由 Amazon 投递到开发者已注册的队列。
     *
     * @param request 店铺与站点授权参数
     */
    void rotateApplicationClientSecret(AmazonApplicationManagementReqVO request);
}
