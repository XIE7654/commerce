package cn.iocoder.yudao.module.temu.mq;

import cn.iocoder.yudao.module.temu.service.ordermanagement.OrderManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Temu 店铺订单同步 RabbitMQ 消费者。
 *
 * <p>使用单消费者串行处理店铺任务，避免同一店铺的重复消息并发拉取；处理异常会触发框架配置的重试及死信转移。</p>
 */
@Component
@RequiredArgsConstructor
public class TemuOrderSyncConsumer {

    private final OrderManagementService orderManagementService;

    /**
     * 消费店铺订单同步消息。
     *
     * @param message 包含待同步店铺编号的消息
     */
    @RabbitListener(queues = TemuOrderSyncRabbitMQConfig.QUEUE_NAME, concurrency = "1")
    public void onMessage(TemuOrderSyncMessage message) {
        orderManagementService.syncShopOrders(message.getShopId());
    }

}
