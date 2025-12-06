package org.baljaguk.domain.chat.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Add this import
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("Configuring STOMP endpoints...");
        registry.addEndpoint("/ws/chat")
                .setAllowedOrigins("*")
                .withSockJS();
        log.info("STOMP endpoient /ws/chat registered with allowed origins: *");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("Configuring message broker...");
        //내장 브로커의 prefix
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
        log.info("Message broker configured: Simple broker for /topic, application destination prefix /app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        log.info("Configuring client inbound channel with StompHandler interceptor...");
        registration.interceptors(stompHandler);
        log.info("StompHandler registered as inbound channel interceptor.");
    }
}
