package org.baljaguk.domain.chat.repository;

import org.baljaguk.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.team WHERE cr.team.id IN :teamIds")
    List<ChatRoom> findByTeamIdIn(@Param("teamIds") List<Long> teamIds);
}
