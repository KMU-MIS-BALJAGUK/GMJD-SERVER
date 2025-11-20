package org.baljaguk.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.domain.user.dto.request.CategoryUpdateRequest;
import org.baljaguk.domain.user.dto.request.EducationUpdateRequest;
import org.baljaguk.domain.user.dto.request.SkillUpdateRequest;
import org.baljaguk.domain.user.dto.request.UserUpdateRequest;
import org.baljaguk.domain.user.dto.response.ProfileResponse;
import org.baljaguk.domain.user.service.UserService;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/skills")
    @Operation(summary = "스킬셋 수정 API",
            description = "로그인한 사용자의 스킬셋 정보를 수정합니다.\n" +
                    "스킬들을 리스트 형태(['Java', 'Spring'])로 요청하면, DB에는 콤마로 구분된 문자열로 저장됩니다.\n" +
                    "빈 리스트([])를 보내면 스킬셋이 초기화됩니다.")
    public ResponseEntity<ApiResponse<Void>> updateMySkills(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SkillUpdateRequest request
    ) {
        userService.updateMySkills(userDetails.getUser().getId(), request);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/education")
    @Operation(summary = "학력 정보 수정 API",
            description = "로그인한 사용자의 **대학교명, 전공, 학력 상태(Enum), 학위(Enum)** 네 가지 정보를 모두 수정합니다."
    )
    public ResponseEntity<ApiResponse<String>> updateEducation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody EducationUpdateRequest request
    ) {
        userService.updateEducation(userDetails.getUser().getId(), request);

        return ResponseEntity.ok(ApiResponse.ok("학력 정보가 수정되었습니다."));
    }

    @PatchMapping("/categories")
    @Operation(summary = "관심 분야(Category) 수정 API",
            description = "로그인한 사용자의 관심 분야를 수정합니다.\n" +
                    "카테고리 ID 리스트(예: `[1, 3, 5]`)를 보내면 기존 관심사는 삭제되고 새로 저장됩니다.\n" +
                    "빈 리스트 `[]`를 보내면 관심사가 모두 삭제(초기화)됩니다."
    )
    public ResponseEntity<ApiResponse<String>> updateInterests(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CategoryUpdateRequest request
    ) {
        userService.updateInterests(userDetails.getUser().getId(), request);

        return ResponseEntity.ok(ApiResponse.ok("관심 분야가 수정되었습니다."));
    }
}

    @GetMapping("/my-profile")
    @Operation(summary = "유저 마이프로필 조회",
            description = "마이프로필 조회합니다.")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ProfileResponse result = userService.getMyProfile(userDetails.getUser().getId());

        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
