package org.baljaguk.domain.chat.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.chat.entity.dto.ChatRoomListResponse;
import org.baljaguk.domain.chat.service.ChatRoomService;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/rooms")
    @Operation(summary = "채팅방 목록 조회",
            description = "사용자가 속한 모든 채팅방 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<ChatRoomListResponse>> getAllChatRooms(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ChatRoomListResponse response = chatRoomService.getChatRooms(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
