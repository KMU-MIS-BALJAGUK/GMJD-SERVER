package org.baljaguk.domain.chat.controller;

import org.baljaguk.domain.chat.config.RabbitMqConfig;
import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.baljaguk.domain.chat.entity.dto.ChatMessageRequest;
import org.baljaguk.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @InjectMocks
    private ChatController chatController;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private UserRepository userRepository;

    private OAuth2User oAuth2User;

    @BeforeEach
    void setUp() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", "testuser@example.com");
        oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(attributes)),
                attributes,
                "email");
    }

    @Test
    @DisplayName("메시지 전송 성공 테스트")
    void sendMessage_success() {
        // given
        Long roomId = 1L;
        ChatMessageRequest messageRequest = new ChatMessageRequest("Hello");

        // when
        ChatMessageDto result = chatController.sendMessage(roomId, messageRequest, oAuth2User);

        // then
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMqConfig.PERSISTENCE_QUEUE_NAME),
                eq(result)
        );

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("로그인하지 않은 사용자 메시지 전송 실패 테스트")
    void sendMessage_fail_when_user_is_null() {
        // given
        Long roomId = 1L;
        ChatMessageRequest messageRequest = new ChatMessageRequest("Hello");

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            chatController.sendMessage(roomId, messageRequest, null);
        });

        assertThat(exception.getMessage()).isEqualTo("로그인 정보가 없음");
    }
}
