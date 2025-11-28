package org.baljaguk.domain.chat.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRoomListResponse {
    private List<ChatRoomResponse> chatRooms;
}
