package org.baljaguk.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.baljaguk.domain.chat.service.ChatService;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat.room.{roomId}")
    public void sendMessage(@DestinationVariable("roomId")Long roomId,
                            ChatMessageDto chatMessageDto,
                            Principal principal) {

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getUserId();

        //roomId 지정
        ChatMessageDto finalChatMessageDto = chatMessageDto.toBuilder()
                .roomId(roomId)
                .userId(userId)
                .build();

        //DB저장
        ChatMessageDto savedMessage = chatService.saveMessage(finalChatMessageDto);

        ChatMessageDto messageToSend = ChatMessageDto.builder()
                .roomId(savedMessage.getRoomId())
                .userId(savedMessage.getUserId())
                .message(savedMessage.getMessage())
                .createdAt(savedMessage.getCreatedAt())
                .build();

        //broadcasting
        simpMessagingTemplate.convertAndSend("/topic/chat.room/"+roomId, messageToSend);
    }
}
