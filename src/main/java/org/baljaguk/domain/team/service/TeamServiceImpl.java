package org.baljaguk.domain.team.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.contest.repository.ContestRepository;
import org.baljaguk.domain.team.dto.request.CreateTeamRequest;
import org.baljaguk.domain.team.dto.response.AIQuestionJsonResponse;
import org.baljaguk.domain.team.dto.response.AIRecommendQuestionsResponse;
import org.baljaguk.domain.team.entity.Question;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.repository.QuestionRepository;
import org.baljaguk.domain.team.repository.TeamRepository;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;
import org.baljaguk.global.client.GptClient;
import org.baljaguk.global.util.PromptUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

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

    @Override
    @Transactional
    public void createTeam(Long userId, Long contestId, CreateTeamRequest request) {

        // 1. 팀장 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_USER));

        // 2. 공모전 조회
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_CONTEST));

        // 3. 팀 엔티티 생성
        Team team = Team.create(
                request.title(),
                request.introduction(),
                request.maxMember(),
                contest,
                user
        );

        // 4. 팀 저장
        teamRepository.save(team);

        // 5. 질문 저장
        if (request.questions() != null && !request.questions().isEmpty()) {
            List<Question> questionEntities = request.questions().stream()
                    .map(content -> Question.create(content, team))
                    .toList();

            questionRepository.saveAll(questionEntities);
        }
    }
}
