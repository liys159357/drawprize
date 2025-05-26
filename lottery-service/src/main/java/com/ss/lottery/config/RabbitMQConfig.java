package com.ss.lottery.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "lottery.exchange";
    public static final String QUEUE_NAME = "lottery.result.queue";
    public static final String DEAD_LETTER_QUEUE = "lottery.result.dlq";
    public static final String ROUTING_KEY = "lottery.result";
    public static final String DEAD_LETTER_ROUTING_KEY = "lottery.result.dlq";

    // 主交换器
    @Bean
    public DirectExchange lotteryExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    // 主队列（配置死信交换器）
    @Bean
    public Queue lotteryResultQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    // 绑定主队列到主交换器
    @Bean
    public Binding lotteryResultBinding() {
        return BindingBuilder.bind(lotteryResultQueue())
                .to(lotteryExchange())
                .with(ROUTING_KEY);
    }

    // 死信队列
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE, true);
    }

    // 绑定死信队列到主交换器
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(lotteryExchange())
                .with(DEAD_LETTER_ROUTING_KEY);
    }
}