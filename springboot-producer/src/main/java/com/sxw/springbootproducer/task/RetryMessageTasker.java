package com.sxw.springbootproducer.task;

import com.sxw.entity.BrokerMessageLog;
import com.sxw.entity.Order;
import com.sxw.springbootproducer.constant.Constants;
import com.sxw.springbootproducer.mapper.BrokerMessageLogMapper;
import com.sxw.springbootproducer.producer.RabbitOrderSender;
import com.sxw.springbootproducer.utils.FastJsonConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RetryMessageTasker
{

    private static Logger logger = LoggerFactory.getLogger(RetryMessageTasker.class);

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void reSend()
    {
        logger.info("-----------定时任务开始-----------");

        // 查询消息状态为0(发送中) 且已经超时的消息集合
        List<BrokerMessageLog> list = brokerMessageLogMapper.queryStatusAndTimeoutMessage();

        logger.info("MsgResendList:",list);

        list.forEach(messageLog -> {
            if (messageLog.getTryCount() >= 3)
            {
                // 重复投递三次以上的消息状态设置为失败
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLog.getMessageId(), Constants.ORDER_SEND_FAILURE, new Date());
            }
            else
            {
                // 重复投递
                brokerMessageLogMapper.updateReSend(messageLog.getMessageId(), new Date());
                Order reSendOrder = FastJsonConvertUtil.convertJSONToObject(messageLog.getMessage(), Order.class);
                try
                {
                    rabbitOrderSender.sendOrder(reSendOrder);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.err.println("-----------异常处理-----------");
                }
            }
        });

        logger.info("----------------------------------");
    }
}

