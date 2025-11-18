package org.baljaguk.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.domain.user.dto.request.UserUpdateRequest;
import org.baljaguk.domain.user.service.UserService;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "자체 회원가입 API",
            description = "소셜 로그인 직후 진행되는 자체 회원가입입니다.\n" +
                    "값들을 받아 유저 레코드에 반영합니다.")
    public ResponseEntity<ApiResponse<Void>> localSignUp(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        userService.updateUserProfile(userDetails.getUser().getId(), request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
