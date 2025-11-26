package org.baljaguk.domain.chat.service;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.chat.config.RabbitMqConfig;
import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatListener {

    private final ChatPersistenceService chatPersistenceService;

    //RabbitMQ의 queue에서 message를 consume -> DB 저장
    @RabbitListener(queues = RabbitMqConfig.PERSISTENCE_QUEUE_NAME,containerFactory = "rabbitListenerContainerFactory")
    public void handleChatMessage(ChatMessageDto chatMessageDto, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.debug("AMQP Consumed - Received message from roomId: {}", chatMessageDto.getRoomId());
            log.debug("AMQP Consumed - Saving message roomId: {}", chatMessageDto.getRoomId());

            chatPersistenceService.saveMessage(chatMessageDto);
            channel.basicAck(deliveryTag, false); // 메시지 처리 성공
        } catch (Exception e) {
            log.error("DB 저장 실패. 메시지를 DLQ로 보냅니다.: {}", e.getMessage());
            channel.basicNack(deliveryTag, false, false); // 메시지 처리 실패, DLQ로 이동
        }
    }
}