package org.baljaguk.domain.team.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.contest.repository.ContestRepository;
import org.baljaguk.domain.team.dto.request.CreateTeamRequest;
import org.baljaguk.domain.team.dto.request.TeamApplyRequest;
import org.baljaguk.domain.team.dto.response.*;
import org.baljaguk.domain.team.entity.*;
import org.baljaguk.domain.team.repository.*;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;
import org.baljaguk.global.client.GeminiClient;
import org.baljaguk.global.util.PromptUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private final AnswerRepository answerRepository;

    private final GeminiClient geminiClient;


    @Override
    @Transactional
    public AIRecommendQuestionsResponse getAIRecommendQuestions(Long contestId) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_CONTEST));

        String prompt = PromptUtil.generateQuestionPrompt(
                contest.getEndDate().toString(),
                contest.getName()
        );

        String aiResponse = PromptUtil.extractJson(geminiClient.generate(prompt));

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

        // 본인을 팀멤버에 저장
        TeamMember teamLeaderMember = TeamMember.create(
                TeamMemberType.LEADER, // 팀장 타입
                team,
                user
        );

        teamMemberRepository.save(teamLeaderMember);

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
        boolean existsRequested = teamApplyRepository.existsByUserAndTeamAndStatus(
                user,
                team,
                RegisterStatus.REQUESTED
        );
        if (existsRequested) {
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

        // skills 저장 형태: "Java,SpringBoot,React"
        String skillsStr = String.join(",", request.skills());

        // 1) TeamApply 생성
        TeamApply apply = TeamApply.create(user, team, skillsStr);
        teamApplyRepository.save(apply);

        // 2) teamId로 question 조회
        List<Question> questions = questionRepository.findByTeam(team);

        if (questions.size() != request.answer().size()) {
            throw new GeneralException(ErrorCode.INVALID_ANSWER_COUNT);
        }

        // 3) Answer 테이블에 질문 개수만큼 저장
        for (int i = 0; i < questions.size(); i++) {
            Answer answer = Answer.create(
                    request.answer().get(i),
                    apply,
                    questions.get(i)
            );
            answerRepository.save(answer);
        }

        try {
            // Prompt 생성
            String prompt = PromptUtil.generateSummaryTagPrompt(
                    user.getLevel(),
                    request
            );

            // Gemini 호출
            String aiResponse = geminiClient.generate(prompt);

            // JSON만 추출
            String json = PromptUtil.extractJson(aiResponse);

            // JSON 파싱 (tags 배열 추출)
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            List<String> tags = new ArrayList<>();
            JsonNode tagsNode = root.get("tags");
            if (tagsNode == null || !tagsNode.isArray()) {
                throw new IllegalStateException("AI 응답에 'tags' 배열이 없습니다");
            }
            tagsNode.forEach(tagNode -> tags.add(tagNode.asText()));

            // 엔티티에 저장
            apply.updateAiTags(tags);

            log.info("✨ AI 요약 태그 저장 완료: {}", tags);

        } catch (Exception e) {
            log.error("❌ AI 요약 태그 생성 실패 — 계속 진행 (지원은 정상 처리됨)", e);
            // 실패해도 팀 신청 자체는 성공해야 하므로 예외 던지지 않음
        }
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
                            team.getContest().getId(),
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

        // 1) 조회할 TeamStatus 목록 정의
        List<TeamStatus> statuses = List.of(TeamStatus.OPEN, TeamStatus.CLOSED);

        // 2) Fetch Join 조회
        List<Team> myTeams = teamRepository.findAllWithContestByTeamLeaderId(userId, statuses);

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
                            team.getContest().getId(),
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
                            contest.getId(),
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
                team.getMemo(),
                members,
                team.getContest().getId()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TeamApplicantListResponse getTeamApplicants(Long teamId, Long userId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        if (!team.getTeamLeader().getId().equals(userId)) {
            throw new GeneralException(
                    ErrorCode.NO_AUTHORITY
            );
        }

        List<TeamApply> applies = teamApplyRepository.findByTeamIdWithUser(teamId);

        List<TeamApplicantListResponse.ApplicantInfo> applicants = applies.stream()
                .map(apply ->
                        TeamApplicantListResponse.ApplicantInfo.of(
                                apply.getUser().getId(),
                                apply.getUser().getProfileImageUrl(),
                                apply.getUser().getName(),
                                apply.getAiTags(),
                                apply.getStatus()
                        )
                )
                .toList();

        return TeamApplicantListResponse.of(teamId, applicants);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicantDetailResponse getApplicantDetail(Long teamId, Long applicantUserId, Long userId) {

        // 1) 팀 조회 + 팀장 여부 검증
        Team team = teamRepository.findTeamWithLeaderById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        if (!team.getTeamLeader().getId().equals(userId)) {
            throw new GeneralException(ErrorCode.NOT_TEAM_LEADER);
        }

        // 2) 지원자 조회 (TeamApply + User + Answer + Question fetch join)
        // REQUESTED 상태인 지원만 조회
        TeamApply apply = teamApplyRepository.findApplyDetail(teamId, applicantUserId, RegisterStatus.REQUESTED)
                .orElseThrow(() -> new GeneralException(ErrorCode.APPLY_NOT_FOUND));

        User applicant = apply.getUser();

        // 3) Answer → QuestionAnswerInfo 매핑
        List<ApplicantDetailResponse.QuestionAnswerInfo> qaList =
                apply.getAnswer().stream()
                        .map(answer ->
                                ApplicantDetailResponse.QuestionAnswerInfo.of(
                                        answer.getQuestion().getContent(),
                                        answer.getAnswer()
                                )
                        )
                        .toList();

        // 4) 응답 생성
        return ApplicantDetailResponse.of(
                applicant.getId(),
                applicant.getProfileImageUrl(),
                applicant.getName(),
                applicant.getLevel(),
                apply.getAiTags(),
                apply.getSkills(),
                qaList
        );
    }

    @Override
    @Transactional
    public void approveApplicant(Long teamId, Long applicantUserId, Long leaderId) {

        // 1) 팀 조회 + 팀장 검증
        Team team = teamRepository.findTeamWithLeaderById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        if (!team.getTeamLeader().getId().equals(leaderId)) {
            throw new GeneralException(ErrorCode.NOT_TEAM_LEADER);
        }

        // 2) 지원 데이터 조회 (TeamApply + User fetch join)
        // REQUESTED 상태인 지원만 조회
        TeamApply apply = teamApplyRepository.findApplyDetail(teamId, applicantUserId, RegisterStatus.REQUESTED)
                .orElseThrow(() -> new GeneralException(ErrorCode.APPLY_NOT_FOUND));

        // 이미 처리된 신청은 재승인 불가
        if (apply.getStatus() != RegisterStatus.REQUESTED) {
            throw new GeneralException(ErrorCode.APPLY_ALREADY_PROCESSED);
        }

        // 3) 팀 인원 초과 체크
        Long memberCount = teamMemberRepository.countByTeam(team);
        if (memberCount >= team.getMaxMember()) {
            throw new GeneralException(ErrorCode.TEAM_MEMBER_FULL);
        }

        // 4) 승인 처리
        apply.setStatus(RegisterStatus.ACCEPTED);

        // 5) 팀 멤버 추가
        TeamMember member = TeamMember.create(TeamMemberType.MEMBER, team, apply.getUser());
        teamMemberRepository.save(member);
    }


    @Override
    @Transactional
    public void rejectApplicant(Long teamId, Long applicantUserId, Long leaderId) {

        // 1) 팀 조회 + 팀장 검증
        Team team = teamRepository.findTeamWithLeaderById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        if (!team.getTeamLeader().getId().equals(leaderId)) {
            throw new GeneralException(ErrorCode.NOT_TEAM_LEADER);
        }

        // 2) 지원 데이터 조회
        // REQUESTED 상태인 지원만 조회
        TeamApply apply = teamApplyRepository.findApplyDetail(teamId, applicantUserId, RegisterStatus.REQUESTED)
                .orElseThrow(() -> new GeneralException(ErrorCode.APPLY_NOT_FOUND));

        if (apply.getStatus() != RegisterStatus.REQUESTED) {
            throw new GeneralException(ErrorCode.APPLY_ALREADY_PROCESSED);
        }

        // 3) 거절 처리
        apply.setStatus(RegisterStatus.REJECTED);
    }

    @Override
    @Transactional
    public void removeTeamMember(Long teamId, Long memberId, Long leaderId) {

        // 1) 팀 + 팀장 조회 (fetch join)
        Team team = teamRepository.findTeamWithLeaderById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        // 2) 팀장 본인인지 검증
        if (!team.getTeamLeader().getId().equals(leaderId)) {
            throw new GeneralException(ErrorCode.NOT_TEAM_LEADER);
        }

        // 3) 팀장은 본인을 내보낼 수 없음
        if (team.getTeamLeader().getId().equals(memberId)) {
            throw new GeneralException(ErrorCode.CANNOT_REMOVE_TEAM_LEADER);
        }

        // 4) 팀원 조회
        TeamMember teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.TEAM_MEMBER_NOT_FOUND));

        // 5) 팀원 삭제
        teamMemberRepository.delete(teamMember);
    }

    @Override
    @Transactional
    public void updateTeamMemo(Long leaderId, Long teamId, String memo) {

        // 1) 팀 조회 (팀장까지 fetch join)
        Team team = teamRepository.findTeamWithLeaderById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        // 2) 팀장 검증
        if (!team.getTeamLeader().getId().equals(leaderId)) {
            throw new GeneralException(ErrorCode.NOT_TEAM_LEADER);
        }

        // 3) 메모 업데이트
        team.updateMemo(memo);
    }

    @Override
    @Transactional
    public void cancelMyApply(Long userId, Long teamId) {

        // 1) 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_USER));

        // 2) TeamApply 조회 (user + team 기반)
        TeamApply apply = teamApplyRepository.findByUserAndTeamIdWithFetch(userId, teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.APPLY_NOT_FOUND));

        // 3) 본인이 아닌 경우 — 이론상 발생하지 않지만 안전하게 검사
        if (!apply.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorCode.NOT_MY_APPLY);
        }

        // 4) 이미 처리된 신청은 취소할 수 없음
        if (apply.getStatus() != RegisterStatus.REQUESTED) {
            throw new GeneralException(ErrorCode.APPLY_ALREADY_PROCESSED);
        }

        // 5) 상태 변경 → CANCELED
        apply.setStatus(RegisterStatus.CANCELED);
    }

    @Override
    @Transactional
    public void closeTeamRecruit(Long teamId, Long userId) {

        // 1) 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        // 2) 팀장인지 검증
        if (!team.getTeamLeader().getId().equals(userId)) {
            throw new GeneralException(ErrorCode.NOT_TEAM_LEADER);
        }

        // 3) 이미 마감된 상태인지 확인
        if (team.getStatus() == TeamStatus.CLOSED) {
            throw new GeneralException(ErrorCode.ALREADY_CLOSED_TEAM);
        }

        // 4) 만료된 팀이면 마감 불가
        if (team.getStatus() == TeamStatus.EXPIRED) {
            throw new GeneralException(ErrorCode.ALREADY_EXPIRED_TEAM);
        }

        // 5) 모집 마감 처리
        team.updateStatus(TeamStatus.CLOSED);
    }

    @Override
    @Transactional
    public void expireTeamRecruit(Long teamId, Long userId) {

        // 1) 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        // 2) 요청자가 팀장인지 확인
        if (!team.getTeamLeader().getId().equals(userId)) {
            throw new GeneralException(ErrorCode.NOT_TEAM_LEADER);
        }

        // 3) 현재 상태가 CLOSED가 아니라면 예외
        if (team.getStatus() != TeamStatus.CLOSED) {
            throw new GeneralException(ErrorCode.TEAM_NOT_CLOSED);
        }

        // 4) 상태 변경: CLOSED → EXPIRED
        team.updateStatus(TeamStatus.EXPIRED);
    }
}
