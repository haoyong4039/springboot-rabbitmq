package com.sxw.springbootproducer.producer;

import com.sxw.entity.Order;
import com.sxw.springbootproducer.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *  简单模拟发送，详见test测试用例
 */
@Component
public class OrderSender
{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback ccb = new RabbitTemplate.ConfirmCallback()
    {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause)
        {
            System.out.println(ack);
        }
    };

    public void send(Order order)
    {
        rabbitTemplate.setConfirmCallback(ccb);
        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        rabbitTemplate.convertAndSend("order-exchange", "order.B", order, correlationData);
    }
}
