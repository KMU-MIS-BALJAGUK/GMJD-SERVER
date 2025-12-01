package org.baljaguk.domain.chat.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.chat.entity.dto.ChatMessageResponse;
import org.baljaguk.domain.chat.entity.dto.ChatRoomListResponse;
import org.baljaguk.domain.chat.service.ChatMessageService;
import org.baljaguk.domain.chat.service.ChatRoomService;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    private final ChatMessageService chatMessageService;

    @GetMapping("/rooms")
    @Operation(summary = "채팅방 목록 조회",
            description = "사용자가 속한 모든 채팅방 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<ChatRoomListResponse>> getAllChatRooms(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ChatRoomListResponse response = chatRoomService.getChatRooms(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{roomId}")
    @Operation(
            summary = "채팅 내역 조회",
            description = "roomId에 해당하는 채팅 내역을 커서(messageId, messageAt) 기반으로 조회합니다."
    )
    public ResponseEntity<ApiResponse<ChatMessageResponse>> getChatHistory(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long roomId,
            @RequestParam(required = false) Long cursorMessageId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursorMessageAt,
            @RequestParam(defaultValue = "30") Integer size
    ){
        ChatMessageResponse chatMessageResponse = chatMessageService.getChatHistory(customUserDetails,roomId, cursorMessageId, cursorMessageAt, size);

        return ResponseEntity.ok(ApiResponse.ok(chatMessageResponse));
    }
}
