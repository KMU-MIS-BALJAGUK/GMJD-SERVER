package org.baljaguk.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.chat.config.RabbitMqConfig;
import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatListener {

    private final ChatPersistenceService chatPersistenceService;

    //RabbitMQ의 queue에서 message를 consume -> DB 저장
    @RabbitListener(queues = RabbitMqConfig.PERSISTENCE_QUEUE_NAME)
    public void handleChatMessage(ChatMessageDto chatMessageDto) {
        try {
            log.info("AMQP Consumed - Saving message to DB: {}", chatMessageDto.getMessage());

            chatPersistenceService.saveMessage(chatMessageDto);

        } catch (Exception e) {
            log.error("DB 저장 실패 (재시도 예정): {}", e.getMessage());
            throw e;
        }
    }
}
