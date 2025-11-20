package org.baljaguk.domain.team.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.contest.repository.ContestRepository;
import org.baljaguk.domain.team.dto.request.TeamCreateRequest;
import org.baljaguk.domain.team.dto.request.TeamUpdateRequest;
import org.baljaguk.domain.team.dto.response.TeamApplyStatusResponse;
import org.baljaguk.domain.team.dto.response.TeamCreateResponse;
import org.baljaguk.domain.team.dto.response.TeamDetailResponse;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamMember;
import org.baljaguk.domain.team.entity.TeamRole;
import org.baljaguk.domain.team.exception.TeamException;
import org.baljaguk.domain.team.repository.TeamMemberRepository;
import org.baljaguk.domain.team.repository.TeamRepository;
import org.baljaguk.domain.teamapplication.entity.TeamApplication;
import org.baljaguk.domain.teamapplication.repository.TeamApplicationRepository;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.exception.UserException;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final TeamApplicationRepository teamApplicationRepository;

    // 2) 팀 신청 가능 여부 조회
    @Override
    public TeamApplyStatusResponse getApplyStatus(Long teamId, Long userId) {

        Team team = findTeam(teamId);
        User user = findUser(userId);

        // 팀장 여부
        boolean isLeader = team.getLeader().getId().equals(user.getId());

        // 팀원 여부
        boolean isMember = teamMemberRepository.existsByTeamAndUser(team, user);

        // 이미 신청했는지
        boolean isApplied = teamApplicationRepository.existsByTeamAndUser(team, user);

        // 정원 초과 여부
        int currentCount = teamMemberRepository.countByTeam(team);
        boolean isFull = currentCount >= team.getMaxMemberCount();

        // 모집 상태
        boolean recruiting = team.isRecruiting();

        String reasonCode = "OK";
        String message = "신청 가능합니다.";
        boolean canApply = true;

        if (isLeader) {
            canApply = false;
            reasonCode = "TEAM_LEADER";
            message = "팀장은 신청할 수 없습니다.";
        } else if (isMember) {
            canApply = false;
            reasonCode = "TEAM_MEMBER";
            message = "이미 팀원으로 참여 중입니다.";
        } else if (isApplied) {
            canApply = false;
            reasonCode = "ALREADY_APPLIED";
            message = "이미 신청한 팀입니다.";
        } else if (!recruiting) {
            canApply = false;
            reasonCode = "CLOSED";
            message = "모집이 종료된 팀입니다.";
        } else if (isFull) {
            canApply = false;
            reasonCode = "FULL";
            message = "모집 인원을 초과했습니다.";
        }

        return new TeamApplyStatusResponse(canApply, reasonCode, message);
    }

    // 4) 팀 상세 조회
    @Override
    public TeamDetailResponse getTeamDetail(Long teamId, Long userId) {

        Team team = findTeam(teamId);
        User user = findUser(userId);

        List<TeamMember> members = teamMemberRepository.findAllByTeamOrderByIdAsc(team);
        List<TeamApplication> applications = teamApplicationRepository.findAllByTeam(team);

        return TeamDetailResponse.of(team, user, members, applications);
    }

    // 1) 팀 생성
    @Override
    @Transactional
    public TeamCreateResponse createTeam(Long contestId, Long leaderId, TeamCreateRequest request) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new TeamException(ErrorCode.CONTEST_NOT_FOUND));

        User leader = findUser(leaderId);

        if (teamRepository.existsByContestAndLeader(contest, leader)) {
            throw new TeamException(ErrorCode.TEAM_ALREADY_EXISTS_FOR_CONTEST);
        }

        Team team = Team.builder()
                .contest(contest)
                .leader(leader)
                .title(request.getTitle())
                .description(request.getDescription())
                .maxMemberCount(request.getMaxMemberCount())
                .recruiting(true)
                .build();

        teamRepository.save(team);

        TeamMember leaderMember = TeamMember.builder()
                .team(team)
                .user(leader)
                .role(TeamRole.LEADER)
                .build();

        teamMemberRepository.save(leaderMember);

        // TODO: request.getTechStacks() 저장 로직 추가

        return TeamCreateResponse.builder()
                .teamId(team.getId())
                .contestId(contest.getId())
                .title(team.getTitle())
                .build();
    }

    // 3) 팀 수정
    @Override
    @Transactional
    public void updateTeam(Long teamId, Long leaderId, TeamUpdateRequest request) {

        Team team = findTeam(teamId);
        User leader = findUser(leaderId);

        if (!team.getLeader().getId().equals(leader.getId())) {
            throw new TeamException(ErrorCode.TEAM_FORBIDDEN);
        }

        if (request.getTitle() != null) {
            team.updateTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            team.updateDescription(request.getDescription());
        }
        if (request.getMaxMemberCount() != null) {
            team.updateMaxMemberCount(request.getMaxMemberCount());
        }

        // TODO: techStacks 수정 로직
    }

    // 팀원 내보내기
    @Override
    @Transactional
    public void kickMember(Long teamId, Long leaderId, Long targetUserId) {

        Team team = findTeam(teamId);
        User leader = findUser(leaderId);
        User target = findUser(targetUserId);

        if (!team.getLeader().getId().equals(leader.getId())) {
            throw new TeamException(ErrorCode.TEAM_FORBIDDEN);
        }

        if (leader.getId().equals(target.getId())) {
            throw new TeamException(ErrorCode.TEAM_CANNOT_KICK_LEADER);
        }

        TeamMember teamMember = teamMemberRepository.findAllByTeamOrderByIdAsc(team).stream()
                .filter(m -> m.getUser().getId().equals(target.getId()))
                .findFirst()
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_MEMBER_NOT_FOUND));

        teamMemberRepository.delete(teamMember);
    }

    // 공통 조회 메서드
    private Team findTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_NOT_FOUND));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }
}
