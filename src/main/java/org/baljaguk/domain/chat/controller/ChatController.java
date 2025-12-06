package org.baljaguk.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.baljaguk.domain.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;



@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat.room.{roomId}")
    public void sendMessage(@DestinationVariable("roomId")Long roomId, ChatMessageDto chatMessageDto) {

        //roomId 검증
        if(!roomId.equals(chatMessageDto.getRoomId())) {
            log.info("Room Id does not match room Id {}", roomId);
            throw new IllegalArgumentException("Room Id does not match room Id " + roomId);
        }

        //DB저장
        chatService.saveMessage(chatMessageDto);

        //broadcasting
        simpMessagingTemplate.convertAndSend("/topic/chat.room/"+roomId, chatMessageDto);
    }
}