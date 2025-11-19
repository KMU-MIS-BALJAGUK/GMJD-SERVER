package org.baljaguk.domain.chat.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//ampq 설정
@Configuration
public class RabbitMqConfig {

    public static final String PERSISTENCE_QUEUE_NAME = "gmjd-chat-persistence-queue";

    //비동기 작업(DB 저장) 큐
    @Bean
    public Queue persistenceQueue() {
        //durable:true => rabbit mq 상태 유지
        return new Queue(PERSISTENCE_QUEUE_NAME, true);
    }

    // rabbitmqTemplate가 DTO <-> json 직렬화, 역직렬화
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //rabbitmqTemplate 생성
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
