package org.baljaguk.domain.chat.service;

import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.domain.user.service.UserServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.global.util.JWTUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * STOMP 메시지 인터셉터 (StompHandler)
 * WebSocket 연결(CONNECT) 및 구독(SUBSCRIBE) 시점을 가로채서
 * 세션 속성(sessionAttributes)에 userId와 roomId를 저장
 * 연결 해제(DISCONNECT) 시 WebSocketEventListener에서 사용
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {


    //세션에서 불러올 때 쓸 KEY 값
    private static final String USER_ID_KEY = "userId";
    private static final String ROOM_ID_KEY = "roomId";

    // 채팅방 구독 경로 패턴: /topic/chat.room.{roomId}
    private static final Pattern ROOM_ID_PATTERN = Pattern.compile("/topic/chat\\.room\\.(\\d+)");
    private final JWTUtil jwtUtil;
    @Lazy
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    /**
     * 클라이언트로부터 메시지가 송신되기 전에 가로채서 처리
     */
    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        // 세션 속성이 null인 경우 안전하게 맵을 생성하여 설정합니다.
        if (sessionAttributes == null) {
            accessor.setSessionAttributes(new java.util.HashMap<>());
            sessionAttributes = accessor.getSessionAttributes();
        }

        // 1. CONNECT 명령어 처리 (인증 및 userId 저장)
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            handleConnect(accessor, sessionAttributes);
        }
        // 2. SUBSCRIBE 명령어 처리 (roomId 저장)
        else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            handleSubscribe(accessor, sessionAttributes);
        }
        // 3. DISCONNECT 명령어 처리 (단순 로깅)
        else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            log.info("STOMP DISCONNECT command detected. Session ID: {}", accessor.getSessionId());
        }

        return message;
    }

    /**
     * CONNECT 시 JWT를 파싱하여 userId를 추출하고 stomp 세션에 저장
     * @param accessor 클라이언트의 stomp 프레임에 접근
     * @param sessionAttributes websocket 생명주기에 맞는 세션(사용자 정보를 저장)
     */
    private void handleConnect(@NonNull StompHeaderAccessor accessor, @NonNull Map<String, Object> sessionAttributes) {

        String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("STOMP CONNECT: Authorization header missing or malformed. Session ID: {}", accessor.getSessionId());
            throw new IllegalArgumentException("Authorization header missing or malformed");
        }

        String token = authorizationHeader.substring(7);

        try {
            // JWTUtil을 사용하여 토큰에서 userId를 추출
            Long userId = jwtUtil.getUserId(token);

            //User 객체 조회
            User user = userRepository.findById(userId).
                    orElseThrow(()->new IllegalArgumentException("user not found at stomphandler"+userId));

            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null, //
                    customUserDetails.getAuthorities()
            );

            accessor.setUser(authentication);

            sessionAttributes.put(USER_ID_KEY, userId);
            log.info("STOMP CONNECT: User successfully authenticated and UserId={} saved to session attributes.", userId);

        } catch (Exception e) {
            log.error("JWT validation failed for Session ID: {}", accessor.getSessionId(), e);
            throw new IllegalArgumentException("JWT validation failed", e);
        }
    }

    /**
     * SUBSCRIBE 시 구독 경로에서 roomId를 추출하여 세션에 저장
     * @param accessor 클라이언트의 stomp 프레임에 접근
     * @param sessionAttributes websocket 생명주기에 맞는 세션(사용자 정보를 저장)
     */
    private void handleSubscribe(@NonNull StompHeaderAccessor accessor, @NonNull Map<String, Object> sessionAttributes) {

        //destination => /chat.room/123
        String destination = accessor.getDestination();

        if (destination != null) {
            Matcher matcher = ROOM_ID_PATTERN.matcher(destination);

            if (matcher.matches() && matcher.groupCount() > 0) {
                try {
                    Long roomId = Long.parseLong(matcher.group(1));
                    sessionAttributes.put(ROOM_ID_KEY, roomId);
                    log.info("STOMP SUBSCRIBE: RoomId={} extracted and saved to session attributes. Session ID: {}", roomId, accessor.getSessionId());

                    // 클라이언트에게 구독 성공 메시지 전송
                    Map<String, String> confirmationMessage = new java.util.HashMap<>();
                    confirmationMessage.put("type", "SYSTEM");
                    confirmationMessage.put("message", "Successfully subscribed to chat room: " + roomId);
                    messagingTemplate.convertAndSend("/topic/chat.room." + roomId, confirmationMessage);
                    log.info("Sent subscription confirmation to /topic/chat.room.{}", roomId);

                } catch (NumberFormatException e) {
                    log.error("Failed to parse roomId from destination: {}", destination, e);
                } 
            } else {
                log.debug("STOMP SUBSCRIBE: Destination does not match chat room pattern. Destination: {}", destination);
            }
        }
    }
}