package com.domain.hello.rabbitmq.provider;

import com.domain.hello.rabbitmq.config.RabbitMQConfiguration;
import com.domain.hello.rabbitmq.domain.Bank;
import com.domain.hello.rabbitmq.domain.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/24 16:04
 */
@Component
public class SyncProvider {

    @Autowired
    private AmqpTemplate amqpTemplate;


    public void sendUser(User user) {
        System.out.println("Provider user: " + user.getName()+user.getAge());
        amqpTemplate.convertAndSend(RabbitMQConfiguration.USER_QUEUE_NAME, user);
    }


    public void sendBank(Bank bank) {
        System.out.println("Provider user: " + bank.getName()+"code:"+bank.getCode());
        amqpTemplate.convertAndSend(RabbitMQConfiguration.BANK_QUEUE_NAME, bank);
    }
}
