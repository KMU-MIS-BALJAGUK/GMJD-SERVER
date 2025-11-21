package org.baljaguk.domain.team.service;

import jakarta.validation.Valid;
import org.baljaguk.domain.team.dto.request.CreateTeamRequest;
import org.baljaguk.domain.team.dto.response.AIRecommendQuestionsResponse;
import org.baljaguk.domain.team.dto.response.ContestTeamListResponse;
import org.baljaguk.domain.team.dto.response.TeamDetailResponse;

public interface TeamService {
    AIRecommendQuestionsResponse getAIRecommendQuestions(Long contestId);

    void createTeam(Long userId, Long contestId, @Valid CreateTeamRequest request);

    ContestTeamListResponse getTeamList(Long contestId);

    TeamDetailResponse getTeamDetail(Long teamId);
}
