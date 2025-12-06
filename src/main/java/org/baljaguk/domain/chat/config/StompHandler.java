package org.baljaguk.domain.chat.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private static final String USER_ID_KEY = "userId";
    private static final String ROOM_ID_KEY = "roomId";

    // 구독 경로 패턴: /topic/chat.room.1
    private static final Pattern ROOM_ID_PATTERN = Pattern.compile("/topic/chat\\.room/(\\d+)");

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        if (sessionAttributes == null) {
            accessor.setSessionAttributes(new java.util.HashMap<>());
            sessionAttributes = accessor.getSessionAttributes();
        }

        // 1. 연결 (CONNECT) - JWT 인증 및 인증객체 저장
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            handleConnect(accessor, sessionAttributes);
        }
        // 2. 구독 (SUBSCRIBE) - 방 번호 세션 저장
        else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            handleSubscribe(accessor, sessionAttributes);
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor, Map<String, Object> sessionAttributes) {
        String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header missing or malformed");
        }

        String token = authorizationHeader.substring(7);
        try {
            Long userId = jwtUtil.getUserId(token);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, customUserDetails.getAuthorities()
            );
            accessor.setUser(authentication);

            sessionAttributes.put(USER_ID_KEY, userId);
            log.info("STOMP CONNECT: UserId={} authenticated.", userId);

        } catch (Exception e) {
            log.error("JWT validation failed", e);
            throw new IllegalArgumentException("JWT validation failed");
        }
    }

    private void handleSubscribe(@NonNull StompHeaderAccessor accessor, @NonNull Map<String, Object> sessionAttributes) {

        String destination = accessor.getDestination();

        if (destination != null) {
            Matcher matcher = ROOM_ID_PATTERN.matcher(destination);

            if (matcher.matches() && matcher.groupCount() > 0) {

                try {
                    Long roomId = Long.parseLong(matcher.group(1));
                    sessionAttributes.put(ROOM_ID_KEY, roomId);
                    log.info("STOMP SUBSCRIBE: User verified and RoomId={} saved to session.", roomId);
                } catch (NumberFormatException e) {
                    log.error("Invalid Room ID format: {}", destination);
                }
            }
        }
    }
}