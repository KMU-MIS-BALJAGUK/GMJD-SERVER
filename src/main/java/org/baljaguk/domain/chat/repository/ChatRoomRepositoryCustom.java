package org.baljaguk.domain.chat.repository;


import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<Long> findChatRoomIdsByTeamIds (List<Long> teamId);
}
