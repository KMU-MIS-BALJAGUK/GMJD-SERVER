package org.baljaguk.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.chat.entity.ChatMessage;
import org.baljaguk.domain.chat.entity.ChatRoom;
import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.baljaguk.domain.chat.repository.ChatMessageRepository;
import org.baljaguk.domain.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatPersistenceService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * ChatListener에서 메시지 consume 시 DB 저장 로직 method
     * @param chatMessageDto
    */
    @Transactional
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다: " + chatMessageDto.getRoomId()));

        ChatMessage  chatMessage = ChatMessage.builder().message(chatMessageDto.getMessage())
                .userId(chatMessageDto.getUserId())
                .createdAt(chatMessageDto.getCreatedAt())
                .chatRoom(chatRoom)
                .build();

        chatMessageRepository.save(chatMessage);

        return chatMessageDto;
    }
}
