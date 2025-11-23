package org.baljaguk.domain.chat.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.chat.entity.LastReadTime;
import org.baljaguk.domain.chat.repository.LastReadTimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * 주로 사용자의 최종 읽음 시각(LastReadTime)을 관리
 * WebSocketEventListener의 DISCONNECT 이벤트 처리 로직을 제공
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LastReadTimeService {

    private final LastReadTimeRepository lastReadTimeRepository;

    /**
     * 사용자의 채팅방 참여 상태 레코드를 조회
     * @param userId 사용자 ID
     * @param roomId 채팅방 ID
     * @return LastReadTime 엔티티 save
     */
    @Transactional
    public LastReadTime findOrCreateParticipant(Long userId, Long roomId) {
        return lastReadTimeRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseGet(() -> {
                    log.info("Creating new LastReadTime record for User {} in Room {}", userId, roomId);
                    LastReadTime newRecord = LastReadTime.builder()
                            .userId(userId)
                            .roomId(roomId)
                            .lastReadAt(LocalDateTime.now())
                            .build();
                    return lastReadTimeRepository.save(newRecord);
                });
    }

    /**
     * 특정 채팅방에서 특정 사용자의 최종 읽음 시각을 현재 시각으로 갱신
     * 이 메서드는 StompHandler를 거쳐 WebSocketEventListener에서 DISCONNECT 이벤트 발생 시 호출됩니다.
     *
     * @param userId 최종 읽음 처리를 수행할 사용자 ID
     * @param roomId 최종 읽음 처리를 수행할 채팅방 ID
     */
    @Transactional
    public void markMessagesAsRead(Long roomId, Long userId) {

        LastReadTime lastReadTime = lastReadTimeRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseGet(() -> findOrCreateParticipant(userId, roomId));

        // lastReadAt 갱신
        lastReadTime.updateLastReadTime();
    }

    /**
     * @param userId 사용자 ID
     * @param roomId 채팅방 ID
     * @return 최종 읽음 시각 (LocalDateTime) 또는 레코드가 없으면 null
     */
    public LocalDateTime getLastReadTime(Long userId, Long roomId) {
        return lastReadTimeRepository.findByUserIdAndRoomId(userId, roomId)
                .map(LastReadTime::getLastReadAt)
                .orElse(null);
    }
}