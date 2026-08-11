package cn.iocoder.yudao.module.amazon.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Amazon Listing Marketplace 同步 RabbitMQ 队列配置。 */
@Configuration(proxyBeanMethods = false)
public class AmazonListingMarketplaceSyncRabbitMQConfig {

    public static final String EXCHANGE_NAME = "amazon.listing.marketplace.sync.exchange";
    public static final String ROUTING_KEY = "amazon.listing.marketplace.sync";
    public static final String QUEUE_NAME = "amazon.listing.marketplace.sync.queue";

    @Bean
    public DirectExchange amazonListingMarketplaceSyncExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue amazonListingMarketplaceSyncQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding amazonListingMarketplaceSyncBinding() {
        return BindingBuilder.bind(amazonListingMarketplaceSyncQueue())
                .to(amazonListingMarketplaceSyncExchange()).with(ROUTING_KEY);
    }
}
