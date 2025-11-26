package org.baljaguk.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import java.util.Optional;

/**
 * WebSocket 세션 이벤트 리스너
 * 클라이언트의 WebSocket 연결 끊김(Disconnect) 이벤트를 감지
 * StompHandler가 세션에 저장한 정보를 기반으로 최종 읽음 처리(Last Read Time 갱신)를 수행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final LastReadTimeService lastReadTimeService;

    // 키 값
    private static final String USER_ID_KEY = "userId";
    private static final String ROOM_ID_KEY = "roomId";

    /**
     * WebSocket 세션이 끊겼을 때 (Disconnect) 발생하는 이벤트
     * @param event disconnect 시의 모든 컨텍스트
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        // 이벤트 메시지 STOMP 헤더 정보 접근
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // 세션 속성(Session Attributes)에서 userId와 roomId를 추출
        Long userId = Optional.ofNullable(headerAccessor.getSessionAttributes())
                .map(attr -> attr.get(USER_ID_KEY))
                .filter(Long.class::isInstance)
                .map(Long.class::cast)
                .orElse(null);

        Long roomId = Optional.ofNullable(headerAccessor.getSessionAttributes())
                .map(attr -> attr.get(ROOM_ID_KEY))
                .filter(Long.class::isInstance)
                .map(Long.class::cast)
                .orElse(null);

        // 사용자 ID와 채팅방 ID가 유효한 경우에만 LastReadTime 갱신
        if (userId != null && roomId != null) {
            log.info("WebSocket Disconnect 감지: Session ID={}, UserId={}, RoomId={}",
                    headerAccessor.getSessionId(), userId, roomId);

            lastReadTimeService.markMessagesAsRead(roomId, userId);

            log.info("WebSocket Disconnect: 최종 읽음 처리 완료 [UserId={}, RoomId={}]", userId, roomId);
        } else {
            log.debug("WebSocket Disconnect: 세션 정보 부족. 읽음 처리 스킵. Session ID={}", headerAccessor.getSessionId());
        }
    }
}