package org.baljaguk.domain.chat.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.dto.ContestInfoDto;

@Getter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class ChatRoomResponse {

    private Long chatroomId;

    private ContestInfoDto contestInfo;

    private LastChatInfoDto lastChatInfo;
}
