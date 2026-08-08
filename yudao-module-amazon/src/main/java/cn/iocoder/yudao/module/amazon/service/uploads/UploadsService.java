package cn.iocoder.yudao.module.amazon.service.uploads;

import cn.iocoder.yudao.module.amazon.controller.admin.uploads.vo.UploadsCreateDestinationReqVO;

import java.util.Map;

/** Amazon Uploads API 服务。 */
public interface UploadsService {

    /**
     * 为指定资源创建一次性上传目的地。
     *
     * @param request 店铺、资源和内容完整性参数
     * @return Amazon 返回的上传地址和请求头 JSON
     */
    Map<String, Object> createUploadDestination(UploadsCreateDestinationReqVO request);
}
