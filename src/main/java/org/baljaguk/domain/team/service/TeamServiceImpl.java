package org.baljaguk.domain.team.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.contest.repository.ContestRepository;
import org.baljaguk.domain.team.dto.response.AIQuestionJsonResponse;
import org.baljaguk.domain.team.dto.response.AIRecommendQuestionsResponse;
import org.baljaguk.domain.team.repository.TeamRepository;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;
import org.baljaguk.global.client.GptClient;
import org.baljaguk.global.util.PromptUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ContestRepository contestRepository;
    private final GptClient gptClient;

    @Override
    @Transactional
    public AIRecommendQuestionsResponse getAIRecommendQuestions(Long contestId) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_CONTEST));

        String prompt = PromptUtil.generatePrompt(
                contest.getName(),
                contest.getEndDate().toString()
        );

        String aiResponse = PromptUtil.extractJson(gptClient.callOpenAI(prompt));

        ObjectMapper objectMapper = new ObjectMapper();
        AIQuestionJsonResponse parsed;

        try {
            parsed = objectMapper.readValue(aiResponse, AIQuestionJsonResponse.class);
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.GPT_RESPONSE_PARSE_ERROR);
        }

        return AIRecommendQuestionsResponse.of(parsed.questionList());
    }
}
