package cn.iocoder.yudao.module.temu.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Temu 店铺订单同步消息。
 *
 * <p>租户编号通过 RabbitMQ Header 传递，消息体仅保存店铺编号，避免重复携带租户上下文。</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemuOrderSyncMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 需要同步订单的 Temu 店铺编号。 */
    private Long shopId;

}
