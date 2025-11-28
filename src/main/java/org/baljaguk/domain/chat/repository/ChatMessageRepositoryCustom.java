package org.baljaguk.domain.chat.repository;

import org.baljaguk.domain.chat.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ChatMessageRepositoryCustom {
    Map<Long, Long> getUnreadCounts(Map<Long, LocalDateTime> lastReadTimes);
    List<ChatMessage> findLatestMessagesByRoomIds(List<Long> roomIds);
}
