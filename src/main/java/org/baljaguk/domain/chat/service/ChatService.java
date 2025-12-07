package org.baljaguk.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.chat.entity.ChatMessage;
import org.baljaguk.domain.chat.entity.ChatRoom;
import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.baljaguk.domain.chat.repository.ChatMessageRepository;
import org.baljaguk.domain.chat.repository.ChatRoomRepository;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    /**
     * ChatListener에서 메시지 consume 시 DB 저장 로직 method
     * @param chatMessageDto 채팅 메시지
     */
    @Transactional
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다: " + chatMessageDto.getRoomId()));

        ChatMessage chatMessage = ChatMessage.builder().message(chatMessageDto.getMessage())
                .userId(chatMessageDto.getUserId())
                .chatRoom(chatRoom)
                .build();

        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        User user = userRepository.findById(savedChatMessage.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String profileImageUrl=user.getProfileImageUrl();

        return ChatMessageDto.builder()
                .userId(savedChatMessage.getUserId())
                .message(savedChatMessage.getMessage())
                .roomId(savedChatMessage.getChatRoom().getId())
                .createdAt(savedChatMessage.getCreatedAt())
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
