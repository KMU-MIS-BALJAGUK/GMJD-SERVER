package org.baljaguk.domain.team.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.contest.repository.ContestRepository;
import org.baljaguk.domain.team.dto.request.CreateTeamRequest;
import org.baljaguk.domain.team.dto.request.TeamApplyRequest;
import org.baljaguk.domain.team.dto.response.*;
import org.baljaguk.domain.team.entity.*;
import org.baljaguk.domain.team.repository.QuestionRepository;
import org.baljaguk.domain.team.repository.TeamApplyRepository;
import org.baljaguk.domain.team.repository.TeamMemberRepository;
import org.baljaguk.domain.team.repository.TeamRepository;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;
import org.baljaguk.global.client.GptClient;
import org.baljaguk.global.util.PromptUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamApplyRepository teamApplyRepository;

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

        // 동일 유저가 동일 공모전에 OPEN 상태의 팀이 이미 존재하는지 확인
        boolean existsOpenTeam = teamRepository.existsByContestAndTeamLeaderAndStatus(
                contest,
                user,
                TeamStatus.OPEN
        );

        if (existsOpenTeam) {
            throw new GeneralException(ErrorCode.CONTEST_ALREADY_HAS_OPEN_TEAM);
        }

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

    @Override
    public ContestTeamListResponse getTeamList(Long contestId) {

        // 1. Contest 존재 여부 검증
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_CONTEST));

        // 2. 해당 Contest의 OPEN 상태 팀 목록 조회
        List<Team> teams = teamRepository.findByContestAndStatus(contest, TeamStatus.OPEN);

        // 3. 팀별 현재 멤버 수 조회 후 DTO 변환
        List<ContestTeamListResponse.TeamInfo> teamInfoList = teams.stream()
                .map(team -> {
                    Long memberCount = teamMemberRepository.countByTeam(team);

                    return ContestTeamListResponse.TeamInfo.of(
                            team.getId(),
                            team.getTitle(),
                            team.getMaxMember(),
                            memberCount,
                            team.getStatus().name()
                    );
                })
                .toList();

        // 4. 응답 DTO 반환
        return ContestTeamListResponse.of(teamInfoList);
    }

    @Override
    public TeamDetailResponse getTeamDetail(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        Long memberCount = teamMemberRepository.countByTeam(team);

        List<String> questionList = questionRepository.findByTeam(team)
                .stream()
                .map(Question::getContent)
                .toList();

        String createdAtFormatted = team.getCreatedAt()
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        String contestEndDateFormatted = team.getContest().getEndDate()
                .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        return TeamDetailResponse.of(
                team.getTitle(),
                team.getTeamLeader().getName(),
                createdAtFormatted,
                memberCount,
                team.getMaxMember(),
                contestEndDateFormatted,
                team.getIntroduction(),
                questionList
        );
    }

    @Override
    @Transactional
    public void applyTeam(Long userId, Long teamId, TeamApplyRequest request) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_USER));

        // 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        // 본인이 팀 리더일 때 신청 금지
        if (team.getTeamLeader().getId().equals(userId)) {
            throw new GeneralException(ErrorCode.CANNOT_APPLY_OWN_TEAM);
        }

        // 이미 해당 팀에 신청했는지 확인 (팀 단위 중복 신청 방지)
        boolean exists = teamApplyRepository.existsByUserAndTeam(user, team);
        if (exists) {
            throw new GeneralException(ErrorCode.ALREADY_APPLIED_TEAM);
        }

        // 동일 공모전에 이미 신청중인 상태인지 확인 (동일 공모전 단위 중복 신청 방지)
        boolean hasRequestedApply = teamApplyRepository.existsByUserAndTeamContestAndStatus(
                user,
                team.getContest(),
                RegisterStatus.REQUESTED
        );

        if (hasRequestedApply) {
            throw new GeneralException(ErrorCode.ALREADY_REQUESTED_IN_CONTEST);
        }

        // 동일 공모전에 이미 팀원으로 소속되어 있는지 확인
        boolean isAlreadyMember = teamMemberRepository.existsByMemberAndTeamContest(
                user,
                team.getContest()
        );

        if (isAlreadyMember) {
            throw new GeneralException(ErrorCode.ALREADY_JOINED_IN_CONTEST);
        }

        // TeamApply 생성
        TeamApply apply = TeamApply.create(
                user,
                team,
                request.answer(),
                request.skills()
        );

        // save
        teamApplyRepository.save(apply);
    }

    @Override
    @Transactional(readOnly = true)
    public MyTeamListResponse getMyTeamList(Long userId) {

        // 1) 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_USER));

        // 2) QueryDSL로 CLOSED 팀 조회
        List<Team> closedTeams = teamRepository.findClosedTeamsByUserId(userId);

        // 3) 응답 변환
        List<MyTeamListResponse.MyTeamInfoResponse> responseList = closedTeams.stream()
                .map(team -> {
                    Long memberCount = teamMemberRepository.countByTeam(team);

                    return MyTeamListResponse.MyTeamInfoResponse.of(
                            team.getId(),
                            team.getContest().getImageUrl(),
                            team.getContest().getName(),
                            team.getContest().getOrganizationName(),
                            team.getMaxMember(),
                            memberCount,
                            team.getStatus().getDisplayName()
                    );
                })
                .toList();

        return MyTeamListResponse.of(responseList);
    }

    @Override
    @Transactional(readOnly = true)
    public MyRecruitListResponse getMyRecruitList(Long userId) {

        // 1) 팀장 기준으로 팀 + 공모전 Fetch Join 조회
        List<Team> myTeams = teamRepository.findAllWithContestByTeamLeaderId(userId);

        List<MyRecruitListResponse.MyRecruitInfoResponse> result = myTeams.stream()
                .map(team -> {

                    // 2) 팀 멤버 수
                    Long memberCount = teamMemberRepository.countByTeam(team);

                    // 3) 팀 신청자 중 REQUESTED 상태 수 (QueryDSL)
                    Long requestedApplyCount =
                            teamApplyRepository.countRequestedApplyByTeamId(team.getId());

                    // 4) DTO 조립
                    return MyRecruitListResponse.MyRecruitInfoResponse.of(
                            team.getId(),
                            team.getContest().getImageUrl(),
                            team.getContest().getName(),
                            team.getContest().getOrganizationName(),
                            team.getMaxMember(),
                            memberCount,
                            requestedApplyCount,
                            team.getStatus().getDisplayName()
                            );
                })
                .toList();

        return MyRecruitListResponse.of(result);
    }

    @Override
    @Transactional(readOnly = true)
    public MyApplyListResponse getMyApplyList(Long userId) {

        // 1) 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_USER));

        // 2) fetch join으로 팀/공모전까지 한 번에 조회
        List<TeamApply> applies = teamApplyRepository.findAllWithTeamAndContestByUser(user);

        // 3) 응답 변환
        List<MyApplyListResponse.MyApplyInfoResponse> responseList = applies.stream()
                .map(apply -> {

                    Team team = apply.getTeam();
                    Contest contest = team.getContest();

                    // 팀 멤버 수 조회 (이건 count 쿼리 1개)
                    Long memberCount = teamMemberRepository.countByTeam(team);

                    return MyApplyListResponse.MyApplyInfoResponse.of(
                            team.getId(),
                            contest.getImageUrl(),
                            contest.getName(),
                            team.getTitle(),
                            team.getMaxMember(),
                            memberCount,
                            team.getStatus().getDisplayName()
                    );
                })
                .toList();

        return MyApplyListResponse.of(responseList);
    }

    @Override
    @Transactional(readOnly = true)
    public MyTeamDetailResponse getMyTeamDetail(Long userId, Long teamId) {

        // 팀 & 공모전 fetch join 조회 (N+1 방지)
        Team team = teamRepository.findTeamWithContestByTeamId(teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_USER));

        // 해당 팀에 userId가 속하는지 확인 + 내 팀원 타입 조회
        TeamMember myTeamMember = teamMemberRepository.findByTeamAndMember(team, user)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_MY_TEAM));

        String myType = myTeamMember.getType().getDisplayName();

        // 팀 전체 멤버 + User 페치조인 (N+1 방지)
        List<TeamMember> teamMembers = teamMemberRepository.findAllWithUserByTeamId(teamId);

        Long memberCount = (long) teamMembers.size();

        // DTO 변환 - 팀원 리스트
        List<MyTeamDetailResponse.MemberInfo> members = teamMembers.stream()
                .map(tm -> MyTeamDetailResponse.MemberInfo.of(
                        tm.getId(),
                        tm.getMember().getProfileImageUrl(),
                        tm.getMember().getName(),
                        tm.getType().getDisplayName()
                ))
                .toList();

        // 최종 응답 DTO 생성
        return MyTeamDetailResponse.of(
                team.getTitle(),
                team.getContest().getName(),
                team.getContest().getOrganizationName(),
                memberCount,
                myType,
                members
        );
    }
}
