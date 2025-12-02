package org.baljaguk.domain.chat.dto.response;

public record ChatRoomIdResponse(
        Long chatRoomId
) {
    public static ChatRoomIdResponse of(Long chatRoomId) {
        return new ChatRoomIdResponse(chatRoomId);
    }
}
