package org.baljaguk.domain.chat.entity.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baljaguk.domain.chat.entity.ChatMessage;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {

    private Long roomId;

    private String message;

    private Long userId;

    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
  
    private String profileImageUrl;

    public static ChatMessageDto from(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .message(chatMessage.getMessage())
                .roomId(chatMessage.getChatRoom().getId())
                .createdAt(chatMessage.getCreatedAt())
                .userId(chatMessage.getUserId())
                .build();
    }
    
}
