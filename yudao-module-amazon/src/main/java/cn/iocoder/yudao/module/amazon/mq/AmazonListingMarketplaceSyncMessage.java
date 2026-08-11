package cn.iocoder.yudao.module.amazon.mq;

import java.io.Serializable;

/** Amazon Listing Marketplace 全量同步消息。租户上下文由 RabbitMQ Header 传递。 */
public record AmazonListingMarketplaceSyncMessage() implements Serializable {

    private static final long serialVersionUID = 1L;
}
