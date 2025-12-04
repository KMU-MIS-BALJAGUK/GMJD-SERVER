//package org.baljaguk.domain.chat.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
////ampq 설정
//@Configuration
//public class RabbitMqConfig {
//
//    public static final String PERSISTENCE_QUEUE_NAME = "gmjd-chat-persistence-queue";
//
//    //비동기 작업(DB 저장) 큐
//    @Bean
//    public Queue persistenceQueue() {
//        return QueueBuilder.durable(PERSISTENCE_QUEUE_NAME)
//                .withArgument("x-dead-letter-exchange", "dlx-exchange")
//                .withArgument("x-dead-letter-routing-key", "dlq-persistence")
//                .build();
//    }
//
//    // rabbitmqTemplate가 DTO <-> json 직렬화, 역직렬화
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    //rabbitmqTemplate 생성
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
//            ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(jsonMessageConverter());
//
//        // 재시도 정책 설정
//        factory.setDefaultRequeueRejected(false); // 실패 시 재큐잉 방지
//        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//
//        return factory;
//    }
//
//
//    @Bean
//    public Queue deadLetterQueue() {
//        return new Queue("dlq-chat-persistence-queue", true);
//    }
//
//    @Bean
//    public DirectExchange deadLetterExchange() {
//        return new DirectExchange("dlx-exchange");
//    }
//
//    @Bean
//    public Binding deadLetterBinding() {
//        return BindingBuilder.bind(deadLetterQueue())
//                .to(deadLetterExchange())
//                .with("dlq-persistence");
//    }
//
//}
