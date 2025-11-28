package org.baljaguk.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.chat.entity.QChatRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Long> findChatRoomIdsByTeamIds(List<Long> teamIds) {

        QChatRoom chatRoom = QChatRoom.chatRoom;

        //null 확인
        if (teamIds == null || teamIds.isEmpty()) return List.of();

        return queryFactory.select(chatRoom.id)
                .from(chatRoom)
                .where(chatRoom.team.id.in(teamIds))
                .fetch();
    }
}
