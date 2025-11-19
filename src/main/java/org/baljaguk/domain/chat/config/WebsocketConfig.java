package org.baljaguk.domain.chat.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//stomp 설정(채팅 broadcast)
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    //application.yaml 파일에서 load
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String userName;

    @Value("${spring.rabbitmq.password}")
    private String password;

    //websocket + stomp메시징 구성
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 메시지 브로커 설정 (메시지 중계)
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(host)
                .setRelayPort(port)
                .setClientLogin(userName)
                .setClientPasscode(password)
                .setSystemLogin(userName)
                .setSystemPasscode(password);

        // 클라이언트 -> 서버 메시지 발행(Publish) 접두사
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트 websocket 최초 접속(핸드셰이크) 엔드포인트
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("https://gmjd-web.vercel.app/", "localhost:8080");
    }
}