package org.baljaguk.domain.team.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.dto.request.AIRecommendQuestionRequest;
import org.baljaguk.domain.team.dto.response.AIRecommendQuestionsResponse;
import org.baljaguk.domain.team.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public AIRecommendQuestionsResponse getAIRecommendQuestions(AIRecommendQuestionRequest request) {
        Long contestId = request.contestId();
        String teamTitle = request.teamTitle();
        String teamIntroduction = request.teamIntroduction();



        return AIRecommendQuestionsResponse.of(new ArrayList<>());
    }
}
