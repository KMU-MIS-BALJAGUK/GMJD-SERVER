package org.baljaguk.domain.chat.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.chat.entity.ChatMessage;
import org.baljaguk.domain.chat.entity.ChatRoom;
import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.baljaguk.domain.chat.repository.ChatMessageRepository;
import org.baljaguk.domain.chat.repository.ChatRoomRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomRepository chatRoomRepository;

    @Async
    public void saveMessage(ChatMessageDto chatMessageDto) {
        try {
            //roomId -> 채팅방 추출
            ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getRoomId())
                    .orElseThrow(() -> new IllegalArgumentException("chat room not exists from saveMessage- roomId:" + chatMessageDto.getRoomId()));

            //dto -> entity
            ChatMessage chatMessage = ChatMessage.from(chatMessageDto, chatRoom);

            chatMessageRepository.save(chatMessage);
        } catch (Exception e) {
            log.error("save chat message error",e);
        }
    }
}
