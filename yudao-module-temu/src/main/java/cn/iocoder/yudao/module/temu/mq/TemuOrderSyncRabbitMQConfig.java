package cn.iocoder.yudao.module.temu.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Temu 订单同步 RabbitMQ 队列配置。
 *
 * <p>主队列处理店铺订单同步；消息多次消费失败后进入死信队列，便于人工排查和补偿。</p>
 */
@Configuration(proxyBeanMethods = false)
public class TemuOrderSyncRabbitMQConfig {

    /** 订单同步直连交换机。 */
    public static final String EXCHANGE_NAME = "temu.order.sync.exchange";
    /** 订单同步路由键。 */
    public static final String ROUTING_KEY = "temu.order.sync";
    /** 订单同步主队列。 */
    public static final String QUEUE_NAME = "temu.order.sync.queue";
    /** 订单同步死信交换机。 */
    public static final String DEAD_LETTER_EXCHANGE_NAME = "temu.order.sync.dlx";
    /** 订单同步死信路由键。 */
    public static final String DEAD_LETTER_ROUTING_KEY = "temu.order.sync.dead";
    /** 订单同步死信队列。 */
    public static final String DEAD_LETTER_QUEUE_NAME = "temu.order.sync.dead.queue";

    /**
     * 声明订单同步主交换机。
     *
     * @return 持久化直连交换机
     */
    @Bean
    public DirectExchange temuOrderSyncExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    /**
     * 声明订单同步主队列，并将失败消息路由到死信交换机。
     *
     * @return 持久化主队列
     */
    @Bean
    public Queue temuOrderSyncQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE_NAME)
                .deadLetterRoutingKey(DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定主队列和订单同步交换机。
     *
     * @return 主队列绑定关系
     */
    @Bean
    public Binding temuOrderSyncBinding() {
        return BindingBuilder.bind(temuOrderSyncQueue()).to(temuOrderSyncExchange()).with(ROUTING_KEY);
    }

    /**
     * 声明订单同步死信交换机。
     *
     * @return 持久化死信交换机
     */
    @Bean
    public DirectExchange temuOrderSyncDeadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE_NAME, true, false);
    }

    /**
     * 声明订单同步死信队列。
     *
     * @return 持久化死信队列
     */
    @Bean
    public Queue temuOrderSyncDeadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_NAME).build();
    }

    /**
     * 绑定死信队列和死信交换机。
     *
     * @return 死信队列绑定关系
     */
    @Bean
    public Binding temuOrderSyncDeadLetterBinding() {
        return BindingBuilder.bind(temuOrderSyncDeadLetterQueue()).to(temuOrderSyncDeadLetterExchange())
                .with(DEAD_LETTER_ROUTING_KEY);
    }

}
