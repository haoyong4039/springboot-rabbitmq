package com.sxw.springbootproducer.producer;

import com.sxw.entity.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  简单模拟发送，详见test测试用例
 */
@Component
public class OrderSender
{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Order order)
    {
        // 设置消息唯一Id
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getMessageId());
        rabbitTemplate.convertAndSend("order-exchange", "order.abcd", order, correlationData);
        /**
         * 1. 在15672控制台手动创建exchange和queue
         *    在exchange或queue中进行exchange和queue的绑定
         *    routing-key采用order.*或者order.#，区别：*只支持order.xxx，不支持order.xxx.xxx
         *
         * 2. 使用代码进行exchange和queue的绑定，详细配置可自行搜索
         */
    }
}
