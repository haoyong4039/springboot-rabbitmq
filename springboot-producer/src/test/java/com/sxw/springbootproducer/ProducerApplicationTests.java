package com.sxw.springbootproducer;

import com.sxw.entity.Order;
import com.sxw.springbootproducer.producer.OrderSender;
import com.sxw.springbootproducer.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerApplicationTests
{
    @Autowired
    private OrderSender orderSender;

    @Autowired
    private OrderService orderService;

    @Test
    public void testSendSimple() throws Exception
    {
        Order order = new Order();
        order.setId(00000000001);
        order.setName("testOrderSimple");
        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
        orderSender.send(order);
    }

    @Test
    public void testSend() throws Exception
    {
        Order order = new Order();
        order.setId(20110108);
        order.setName("testOrder");
        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
        orderService.createOrder(order);
    }
}
