package org.baljaguk.domain.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.dto.request.CreateTeamRequest;
import org.baljaguk.domain.team.dto.request.TeamApplyRequest;
import org.baljaguk.domain.team.dto.request.UpdateTeamMemoRequest;
import org.baljaguk.domain.team.dto.response.*;
import org.baljaguk.domain.team.service.TeamService;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @Operation(
            summary = "AI 질문 추천",
            description = "공모전 ID를 기반으로 팀 생성 시 활용할 AI 질문 리스트를 생성합니다."
    )
    @PostMapping("/{contestId}/ai-question")
    public ResponseEntity<ApiResponse<AIRecommendQuestionsResponse>> getAIRecommendQuestions(
            @PathVariable Long contestId
    ) {
        AIRecommendQuestionsResponse response = teamService.getAIRecommendQuestions(contestId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(
            summary = "팀 생성",
            description = "로그인한 사용자가 특정 공모전에 대한 팀을 생성합니다. 동일 공모전에는 하나의 OPEN 팀만 생성할 수 있습니다."
    )
    @PostMapping("/{contestId}")
    public ResponseEntity<ApiResponse<Void>> createTeam(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long contestId,
            @RequestBody @Valid CreateTeamRequest request
    ) {
        Long userId = userDetails.getUserId();

        teamService.createTeam(userId, contestId, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(
            summary = "공모전별 팀 목록 조회",
            description = "특정 공모전에 등록된 OPEN 상태의 팀 목록을 조회합니다."
    )
    @GetMapping("/{contestId}")
    public ResponseEntity<ApiResponse<ContestTeamListResponse>> getTeamList(@PathVariable Long contestId) {
        ContestTeamListResponse response = teamService.getTeamList(contestId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(
            summary = "팀 상세 조회",
            description = "팀 신청하기 버튼을 눌러 팀 ID로 팀 상세 정보를 조회합니다. 팀 구성, 모집 정보 등을 확인할 수 있습니다."
    )
    @GetMapping("/{teamId}/detail")
    public ResponseEntity<ApiResponse<TeamDetailResponse>> getTeamDetail(
            @PathVariable Long teamId
    ) {
        TeamDetailResponse response = teamService.getTeamDetail(teamId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(
            summary = "팀 신청",
            description = "로그인한 사용자가 특정 팀에 신청합니다. 동일 공모전 내 중복 신청은 불가합니다."
    )
    @PostMapping("/apply/{teamId}")
    public ResponseEntity<ApiResponse<Void>> applyTeam(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @RequestBody @Valid TeamApplyRequest request
    ) {
        teamService.applyTeam(userDetails.getUserId(), teamId, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(
            summary = "나의 팀 목록 조회",
            description = "현재 로그인한 사용자가 속해 있는 팀(CLOSED 상태) 목록을 조회합니다."
    )
    @GetMapping("/my-teams")
    public ResponseEntity<ApiResponse<MyTeamListResponse>> getMyTeamList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        MyTeamListResponse response = teamService.getMyTeamList(userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(
            summary = "나의 모집 팀 목록",
            description = "현재 로그인한 사용자가 팀장으로 있는 팀 목록을 조회합니다. OPEN/CLOSED 상태 모두 조회합니다."
    )
    @GetMapping("/my-recruit")
    public ResponseEntity<ApiResponse<MyRecruitListResponse>> getMyRecruitList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MyRecruitListResponse response = teamService.getMyRecruitList(userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(
            summary = "나의 지원 목록 조회",
            description = "현재 로그인한 사용자가 지원한 팀 목록을 조회합니다."
    )
    @GetMapping("/my-applies")
    public ResponseEntity<ApiResponse<MyApplyListResponse>> getMyApplyList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MyApplyListResponse response = teamService.getMyApplyList(userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(
            summary = "나의 팀 상세 조회",
            description = "내가 팀원으로 참여 중인 특정 팀의 상세 정보를 조회합니다."
    )
    @GetMapping("/my-teams/{teamId}")
    public ResponseEntity<ApiResponse<MyTeamDetailResponse>> getMyTeamDetail(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MyTeamDetailResponse response = teamService.getMyTeamDetail(userDetails.getUserId(), teamId);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(
            summary = "팀 지원자 목록 조회",
            description = "팀장이 해당 팀에 지원한 지원자 목록을 조회합니다."
    )
    @GetMapping("/my-recruit/{teamId}")
    public ResponseEntity<ApiResponse<TeamApplicantListResponse>> getTeamApplicants(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TeamApplicantListResponse response = teamService.getTeamApplicants(teamId, userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(
            summary = "지원자 상세 조회",
            description = "팀장이 특정 지원자의 상세 정보를 조회합니다. 지원서 내용, 질문 답변, 사용자 정보 등이 포함됩니다."
    )
    @GetMapping("/my-recruit/{teamId}/applicant/{applicantUserId}")
    public ResponseEntity<ApiResponse<ApplicantDetailResponse>> getApplicantDetail(
            @PathVariable Long teamId,
            @PathVariable Long applicantUserId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ApplicantDetailResponse response =
                teamService.getApplicantDetail(teamId, applicantUserId, userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/my-recruit/{teamId}/applicant/{applicantUserId}/approve")
    @Operation(summary = "지원자 승인 API",
            description = "팀장이 지원자의 팀 신청을 승인합니다. 승인 시 팀멤버로 추가되고, 신청 상태는 APPROVED로 변경됩니다.")
    public ResponseEntity<ApiResponse<Void>> approveApplicant(
            @PathVariable Long teamId,
            @PathVariable Long applicantUserId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        teamService.approveApplicant(teamId, applicantUserId, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }


    @PostMapping("/my-recruit/{teamId}/applicant/{applicantUserId}/reject")
    @Operation(summary = "지원자 거절 API",
            description = "팀장이 지원자의 팀 신청을 거절합니다. 신청 상태는 REJECTED로 변경되며 팀멤버로 추가되지 않습니다.")
    public ResponseEntity<ApiResponse<Void>> rejectApplicant(
            @PathVariable Long teamId,
            @PathVariable Long applicantUserId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        teamService.rejectApplicant(teamId, applicantUserId, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @DeleteMapping("/{teamId}/members/{memberId}")
    @Operation(
            summary = "팀원 내보내기(강퇴) API",
            description = """
                팀장이 특정 팀원을 강퇴하는 API입니다.\n
                - PathVariable teamId: 팀 ID\n
                - PathVariable memberId: 내보낼 팀원 ID\n
                - 팀 리더만 강퇴가 가능합니다.
                """
    )
    public ResponseEntity<ApiResponse<Void>> removeTeamMember(
            @PathVariable Long teamId,
            @PathVariable Long memberId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        Long leaderId = userDetails.getUserId();

        teamService.removeTeamMember(teamId, memberId, leaderId);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/my-teams/{teamId}/memo")
    @Operation(
            summary = "나의 팀 메모 수정 API",
            description = "해당 팀의 팀장만 메모를 수정할 수 있습니다.\n" +
                    "아무 메모도 없다면, null이 아닌 빈문자열을 입력해야합니다."
    )
    public ResponseEntity<ApiResponse<Void>> updateTeamMemo(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateTeamMemoRequest request
    ) {

        teamService.updateTeamMemo(userDetails.getUserId(), teamId, request.memo());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/apply/{teamId}/cancel")
    @Operation(
            summary = "팀 신청 취소 API",
            description = "현재 로그인한 사용자가 특정 팀에 대해 했던 신청을 취소합니다.\n" +
                    "신청 상태는 CANCELED 로 변경됩니다."
    )
    public ResponseEntity<ApiResponse<Void>> cancelMyApply(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId
    ) {
        teamService.cancelMyApply(userDetails.getUserId(), teamId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/{teamId}/close")
    @Operation(
            summary = "팀 모집 마감 API",
            description = """
                팀장이 자신의 팀 모집 상태를 '모집완료(CLOSED)'로 변경합니다.\n
                팀장이 아닌 사용자가 호출할 경우 권한 오류가 발생합니다.
                """
    )
    public ResponseEntity<ApiResponse<Void>> closeTeamRecruit(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        teamService.closeTeamRecruit(teamId, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
