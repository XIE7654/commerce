package cn.iocoder.yudao.module.amazon.service.feeds;

import cn.iocoder.yudao.module.amazon.controller.admin.feeds.vo.*;
import java.util.Map;

/** Amazon Feeds 服务。 */
public interface AmazonFeedsService {
    /** 查询 Feed 列表。 */
    Map<String, Object> getFeeds(AmazonFeedsListReqVO request);
    /** 创建 Feed。 */
    Map<String, Object> createFeed(AmazonFeedCreateReqVO request);
    /** 查询 Feed 详情。 */
    Map<String, Object> getFeed(AmazonFeedIdReqVO request);
    /** 取消 Feed。 */
    void cancelFeed(AmazonFeedIdReqVO request);
    /** 创建 Feed Document 上传凭证。 */
    Map<String, Object> createFeedDocument(AmazonFeedDocumentCreateReqVO request);
    /** 查询 Feed Document 元数据。 */
    Map<String, Object> getFeedDocument(AmazonFeedIdReqVO request);
}
