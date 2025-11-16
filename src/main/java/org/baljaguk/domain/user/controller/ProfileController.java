package org.baljaguk.domain.user.controller;

import org.baljaguk.domain.user.dto.ProfileResponseDto;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.user.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users") // User 전용
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService; // DI

    @GetMapping("/myprofile") // URL : /api/v1/users/myprofile
    public ResponseEntity<ApiResponse<ProfileResponseDto>> getMyProfile(

            // Spring Security가 토큰에서 사용자 ID를 넣어줌
            //
            @AuthenticationPrincipal Long currentUserId
    ) {

        // 1. Service의 getMyProfile 메서드를 호출합니다.
        ProfileResponseDto profileDto = profileService.getMyProfile(currentUserId);

        // 2. 공통 응답(ApiResponse.onSuccess)에 담아서 반환합니다.
        return ResponseEntity.ok(ApiResponse.ok(profileDto));
    }
}
