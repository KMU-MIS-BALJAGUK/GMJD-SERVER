package org.baljaguk.domain.chat.service;

import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatListenerTest {

    @InjectMocks
    private ChatListener chatListener;

    @Mock
    private ChatPersistenceService chatPersistenceService;

    @Test
    @DisplayName("메시지 핸들러 성공 테스트")
    void handleChatMessage_success() {
        // given
        ChatMessageDto chatMessageDto = new ChatMessageDto(1L, "Hello",1L, LocalDateTime.now());

        // when
        chatListener.handleChatMessage(chatMessageDto);

        // then
        verify(chatPersistenceService).saveMessage(chatMessageDto);
    }

    @Test
    @DisplayName("DB 저장 실패 시 예외 발생 테스트")
    void handleChatMessage_fail_on_db_error() {
        // given
        ChatMessageDto chatMessageDto = new ChatMessageDto(1L, "Hello",1L, LocalDateTime.now());
        RuntimeException exception = new RuntimeException("DB error");

        doThrow(exception).when(chatPersistenceService).saveMessage(chatMessageDto);

        // when & then
        assertThrows(RuntimeException.class, () -> {
            chatListener.handleChatMessage(chatMessageDto);
        });

        verify(chatPersistenceService).saveMessage(chatMessageDto);
    }
}
