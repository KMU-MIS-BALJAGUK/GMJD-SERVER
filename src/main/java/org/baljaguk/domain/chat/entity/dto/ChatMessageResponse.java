package org.baljaguk.domain.chat.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChatMessageResponse {

    private Long roomId;

    private List<ChatMessageDto> messages;

    private List<TeamMemberInfoDto> teamMembers;

    private Long lastMessageId;

    private LocalDateTime lastMessageAt;

}
