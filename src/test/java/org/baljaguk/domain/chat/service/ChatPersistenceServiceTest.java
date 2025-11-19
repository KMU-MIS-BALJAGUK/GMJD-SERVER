package org.baljaguk.domain.chat.service;

import org.baljaguk.domain.chat.entity.ChatMessage;
import org.baljaguk.domain.chat.entity.ChatRoom;
import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.baljaguk.domain.chat.repository.ChatMessageRepository;
import org.baljaguk.domain.chat.repository.ChatRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatPersistenceServiceTest {

    @InjectMocks
    private ChatPersistenceService chatPersistenceService;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("메시지 저장 성공 테스트")
    void saveMessage_success() {
        // given
        Long roomId = 1L;
        Long userId = 1L;
        String message = "Hello, world!";
        LocalDateTime now = LocalDateTime.now();

        ChatMessageDto chatMessageDto = new ChatMessageDto(roomId, message,userId,now);
        ChatRoom chatRoom = new ChatRoom(); // Assuming ChatRoom has a default constructor or builder

        when(chatRoomRepository.findById(roomId)).thenReturn(Optional.of(chatRoom));

        // when
        ChatMessageDto result = chatPersistenceService.saveMessage(chatMessageDto);

        // then
        ArgumentCaptor<ChatMessage> captor = ArgumentCaptor.forClass(ChatMessage.class);
        verify(chatMessageRepository).save(captor.capture());
        ChatMessage savedMessage = captor.getValue();

        assertThat(result).isEqualTo(chatMessageDto);
        assertThat(savedMessage.getMessage()).isEqualTo(message);
        assertThat(savedMessage.getUserId()).isEqualTo(userId);
        assertThat(savedMessage.getChatRoom()).isEqualTo(chatRoom);
    }

    @Test
    @DisplayName("채팅방이 존재하지 않을 때 메시지 저장 실패 테스트")
    void saveMessage_fail_when_chatRoom_not_found() {
        // given
        Long roomId = 1L;
        ChatMessageDto chatMessageDto = new ChatMessageDto(roomId,"Hello", 1L, LocalDateTime.now());

        when(chatRoomRepository.findById(roomId)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            chatPersistenceService.saveMessage(chatMessageDto);
        });

        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 채팅방입니다: " + roomId);
    }
}
