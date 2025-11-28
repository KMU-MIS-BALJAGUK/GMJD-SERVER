package org.baljaguk.domain.chat.entity.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LastChatInfoDto {
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private Long unReadMessageCount;
}