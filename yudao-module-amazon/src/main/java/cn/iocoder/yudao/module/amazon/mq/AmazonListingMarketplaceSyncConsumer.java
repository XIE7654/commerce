package cn.iocoder.yudao.module.amazon.mq;

import cn.iocoder.yudao.module.amazon.service.listingmarketplace.AmazonListingMarketplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** Amazon Listing Marketplace 全量同步消费者。 */
@Component
@RequiredArgsConstructor
public class AmazonListingMarketplaceSyncConsumer {

    private final AmazonListingMarketplaceService listingMarketplaceService;

    /** 消费同步消息并执行全量店铺站点同步。 */
    @RabbitListener(queues = AmazonListingMarketplaceSyncRabbitMQConfig.QUEUE_NAME, concurrency = "1")
    public void onMessage(AmazonListingMarketplaceSyncMessage message) {
        listingMarketplaceService.syncAllAvailableListings();
    }
}
