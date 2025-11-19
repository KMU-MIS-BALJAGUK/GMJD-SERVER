package org.baljaguk.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.chat.config.RabbitMqConfig;
import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.baljaguk.domain.chat.entity.dto.ChatMessageRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

//STOMP (메시지 퍼블리싱) 컨트롤러
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RabbitTemplate rabbitTemplate;

    /**
     * @param roomId  채팅방 ID(url)
     * @param message 클라이언트가 보낸 JSON 페이로드 (ChatMessageRequest)
     * @param user Oauth2User객체에서 email 속성
     * @return        @SendTo 경로로 브로드캐스팅될 JSON 페이로드 (ChatMessageDto)
     */
//    @MessageMapping("/chat.room.{roomId}") // websocket 매핑
//    @SendTo("/topic/chat.room.{roomId}") // STOMP(rabbitmq) 실시간 브로드캐스팅
//    public ChatMessageDto sendMessage(@DestinationVariable Long roomId,
//                                      ChatMessageRequest message,
//                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
//
//        if (user == null) {
//            throw new IllegalArgumentException("로그인 정보가 없음");
//        }
//
//        Object principalId = user.getAttribute("id");
//
//        long userId;
//        if (principalId instanceof Integer) {
//            userId = ((Integer) principalId).longValue();
//        } else if (principalId instanceof Long) {
//            userId = (Long) principalId;
//        } else {
//            throw new IllegalStateException("OAuth2User에서 유효한 사용자 ID(Long)를 추출할 수 없습니다.");
//        }
//
//        //DTO 생성
//        ChatMessageDto chatMessageDto = new ChatMessageDto();
//
//        // Queue로 message publishing (AMPQ)
//        rabbitTemplate.convertAndSend(
//                RabbitMqConfig.PERSISTENCE_QUEUE_NAME,
//                chatMessageDto
//        );
//
//        log.info("Message sent to AMQP queue for persistence.");
//
//        // websocket에 연결된 동일한 채널(roomId)으로 브로드 캐스팅(STOMP)
//        return chatMessageDto;
//    }
}