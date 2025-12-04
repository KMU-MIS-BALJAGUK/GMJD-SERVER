package org.baljaguk.domain.team.service;

import jakarta.validation.Valid;
import org.baljaguk.domain.team.dto.request.CreateTeamRequest;
import org.baljaguk.domain.team.dto.request.TeamApplyRequest;
import org.baljaguk.domain.team.dto.response.*;

public interface TeamService {
    AIRecommendQuestionsResponse getAIRecommendQuestions(Long contestId);

    void createTeam(Long userId, Long contestId, @Valid CreateTeamRequest request);

    ContestTeamListResponse getTeamList(Long contestId);

    TeamDetailResponse getTeamDetail(Long teamId);

    void applyTeam(Long userId, Long teamId, TeamApplyRequest request);

    MyTeamListResponse getMyTeamList(Long userId);

    MyRecruitListResponse getMyRecruitList(Long userId);

    MyApplyListResponse getMyApplyList(Long userId);

    MyTeamDetailResponse getMyTeamDetail(Long userId, Long teamId);

    TeamApplicantListResponse getTeamApplicants(Long teamId, Long userId);

    ApplicantDetailResponse getApplicantDetail(Long teamId, Long applicantUserId, Long userId);

    void approveApplicant(Long teamId, Long applicantUserId, Long userId);

    void rejectApplicant(Long teamId, Long applicantUserId, Long userId);

    void removeTeamMember(Long teamId, Long memberId, Long leaderId);

    void updateTeamMemo(Long userId, Long teamId, String memo);

    void cancelMyApply(Long userId, Long teamId);

    void closeTeamRecruit(Long teamId, Long userId);

    void expireTeamRecruit(Long teamId, Long userId);
}
