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
import org.baljaguk.domain.user.dto.response.UserSkillsResponse;
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
    @Operation(
            summary = "자체 회원가입 API",
            description = """
                    소셜 로그인 직후 진행되는 자체 회원가입입니다.\n
                    • 유저의 기본 프로필 정보(학교, 전공, 소개 등)를 최초로 저장합니다.\n
                    • 요청 필드 중 스킬셋은 List<String>을 받아 콤마(,)로 구분된 문자열 형태로 저장됩니다.
                    """
    )
    public ResponseEntity<ApiResponse<Void>> localSignUp(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        userService.updateUserProfile(userDetails.getUserId(), request);
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
        userService.updateMySkills(userDetails.getUserId(), request);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/education")
    @Operation(
            summary = "학력 정보 수정 API",
            description = """
                    로그인한 사용자의 학력 정보를 수정합니다.\n
                    • 대학교명\n
                    • 전공\n
                    • education (ENUM)\n
                    • recognizedDegree (ENUM)\n
                    
                    위 네 가지 정보를 한 번에 업데이트합니다.
                    """
    )
    public ResponseEntity<ApiResponse<String>> updateEducation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody EducationUpdateRequest request
    ) {
        userService.updateEducation(userDetails.getUserId(), request);

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
        userService.updateInterests(userDetails.getUserId(), request);

        return ResponseEntity.ok(ApiResponse.ok("관심 분야가 수정되었습니다."));
    }

    @GetMapping("/my-profile")
    @Operation(summary = "유저 마이프로필 조회",
            description = "마이프로필 조회합니다.")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ProfileResponse result = userService.getMyProfile(userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @Operation(
            summary = "내 스킬셋 조회 API",
            description = """
                    로그인한 사용자의 스킬셋을 조회합니다.\n
                    스킬은 List<String> 형태로 응답됩니다.\n
                    해당 api는 팀 신청하기시 스킬셋이 디폴트로 입력이 되어있기 위함입니다.
                    """
    )
    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<UserSkillsResponse>> getMySkills(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserSkillsResponse response = userService.getMySkills(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
