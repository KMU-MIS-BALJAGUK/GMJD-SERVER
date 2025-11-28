package org.baljaguk.domain.chat.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.chat.entity.ChatMessage;
import org.baljaguk.domain.chat.entity.QChatMessage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, Long> getUnreadCounts(Map<Long, LocalDateTime> lastReadTimes) {
        if (lastReadTimes == null || lastReadTimes.isEmpty()) {
            return Collections.emptyMap();
        }

        QChatMessage chatMessage = QChatMessage.chatMessage;
        BooleanBuilder builder = new BooleanBuilder();

        // 각 채팅방마다 마지막으로 읽은 시간 이후의 메시지를 조회하는 조건을 동적으로 생성
        for (Map.Entry<Long, LocalDateTime> entry : lastReadTimes.entrySet()) {
            Long roomId = entry.getKey();
            LocalDateTime lastReadAt = entry.getValue();
            if (lastReadAt != null) {
                builder.or(chatMessage.chatRoom.id.eq(roomId).and(chatMessage.createdAt.gt(lastReadAt)));
            }
        }

        if (!builder.hasValue()) {
            return Collections.emptyMap();
        }

        // 조건에 맞는 메시지들을 채팅방 ID로 그룹화하여 개수를 계산
        return queryFactory
                .select(chatMessage.chatRoom.id, chatMessage.count())
                .from(chatMessage)
                .where(builder)
                .groupBy(chatMessage.chatRoom.id)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(chatMessage.chatRoom.id),
                        tuple -> tuple.get(chatMessage.count())
                ));
    }

    @Override
    public List<ChatMessage> findLatestMessagesByRoomIds(List<Long> roomIds) {
        if (roomIds == null || roomIds.isEmpty()) {
            return Collections.emptyList();
        }

        QChatMessage chatMessage = QChatMessage.chatMessage;
        QChatMessage subChatMessage = new QChatMessage("subChatMessage");

        // 각 채팅방 ID별로 가장 최근 메시지 ID(max(id))를 찾는 서브쿼리 사용
        return queryFactory
                .selectFrom(chatMessage)
                .where(chatMessage.id.in(
                        JPAExpressions.select(subChatMessage.id.max())
                                .from(subChatMessage)
                                .where(subChatMessage.chatRoom.id.in(roomIds))
                                .groupBy(subChatMessage.chatRoom.id)
                ))
                .fetch();
    }
}
