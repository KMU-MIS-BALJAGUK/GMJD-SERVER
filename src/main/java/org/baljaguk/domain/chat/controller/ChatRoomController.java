package org.baljaguk.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.chat.entity.dto.request.ChatRoomCreateRequest;
import org.baljaguk.domain.chat.entity.dto.ChatRoomListResponse;
import org.baljaguk.domain.chat.service.ChatRoomService;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

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

    @PostMapping("/rooms")
    @Operation(summary = "채팅방 생성",
            description = "팀 모집이 마감되면 해당 팀의 채팅방을 생성하고 팀원들을 입장시킵니다.")
    public ResponseEntity<ApiResponse<Long>> createChatRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ChatRoomCreateRequest request
    ) {
        Long roomId = chatRoomService.createChatRoom(userDetails.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.ok(roomId));
    }
}
