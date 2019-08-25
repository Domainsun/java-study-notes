package com.domain.hello.rabbitmq.consumer;

import com.domain.hello.rabbitmq.config.RabbitMQConfiguration;
import com.domain.hello.rabbitmq.domain.Bank;
import com.domain.hello.rabbitmq.domain.BaseReponse;
import com.domain.hello.rabbitmq.domain.User;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/24 16:17
 */
@Component
public class SyncConsumer {

    @Autowired
    private RestTemplate restTemplate;

    @RabbitListener(queues = RabbitMQConfiguration.USER_QUEUE_NAME)
    public void processUser(User user,Channel channel, Message message) throws IOException {
        System.out.println("Consumer user: " + user.toString());
        try {
            BaseReponse baseReponse = restTemplate.postForEntity("http://localhost:8080/syncUser",user,BaseReponse.class).getBody();

            if (baseReponse==null || (!BaseReponse.SUCCESS.equals(baseReponse.getResultCode()))) {
                //确认这条消息消费失败，重新塞回队列，这时候还在队列的头部
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                System.out.println("消费失败"+user.toString());
                return;
            }

            // 确认这条消息消费成功,从队列中移除
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println(("消费成功"+user.toString()));

        } catch (Exception e) {
            e.printStackTrace();
            //确认这条消息消费失败，重新塞回队列，这时候还在队列的头部
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            System.out.println("消费失败"+user.toString());
        }
    }


    @RabbitListener(queues = RabbitMQConfiguration.BANK_QUEUE_NAME)
    public void processBank(Bank bank, Channel channel, Message message) throws IOException {
        System.out.println("Consumer bank: " + bank.toString());
        try {
            BaseReponse baseReponse = restTemplate.postForEntity("http://localhost:8080/syncBank",bank,BaseReponse.class).getBody();

            if (baseReponse==null || (!BaseReponse.SUCCESS.equals(baseReponse.getResultCode()))) {
                //确认这条消息消费失败，重新塞回队列，这时候还在队列的头部
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                System.out.println("消费失败"+bank.toString());
                return;
            }
            // 确认这条消息消费成功,从队列中移除
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            System.out.println(("bank 消费成功"+bank.toString()));

        } catch (Exception e) {
            e.printStackTrace();
            //确认这条消息消费失败，重新塞回队列，这时候还在队列的头部
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            System.out.println("bank 消费失败"+bank.toString());
        }
    }



}
