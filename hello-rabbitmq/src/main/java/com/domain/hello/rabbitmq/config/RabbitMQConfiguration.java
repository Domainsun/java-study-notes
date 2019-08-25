package com.domain.hello.rabbitmq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
/**
 * @description:
 * @author: domain
 * @create: 2019/8/24 16:01
 */

@Configuration
public class RabbitMQConfiguration {

    /**
     * 用户队列
     */
    public static final String USER_QUEUE_NAME = "user_queue";

    /**
     * 银行队列
     */
    public static final String BANK_QUEUE_NAME = "bank_queue";

    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE_NAME);
    }

    @Bean
    public Queue bankQueue() {
        return new Queue(BANK_QUEUE_NAME);
    }
}
