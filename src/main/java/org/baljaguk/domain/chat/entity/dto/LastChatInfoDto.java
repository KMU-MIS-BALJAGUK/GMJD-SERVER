package org.baljaguk.domain.chat.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LastChatInfoDto {
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private Integer unReadCount;
}
