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
public class ChatMessageDto {

    private Long roomId;

    private String message;

    private Long userId;

    private LocalDateTime createdAt;

    private String profileImageUrl;
}
