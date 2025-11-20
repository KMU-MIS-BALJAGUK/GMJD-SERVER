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
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.exception.UserException;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;


    // 2) 팀 신청 가능 여부 조회
    @Override
    public TeamApplyStatusResponse getApplyStatus(Long teamId, Long userId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_NOT_FOUND));

        userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));


        boolean canApply = true;
        String message = "신청 가능합니다.";

        return new TeamApplyStatusResponse(canApply, message);
    }

    // 4) 팀 상세 조회
    @Override
    public TeamDetailResponse getTeamDetail(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_NOT_FOUND));



        return null;
    }

    // 1) 팀 생성
    @Override
    @Transactional
    public TeamCreateResponse createTeam(Long contestId, Long leaderId, TeamCreateRequest request) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new TeamException(ErrorCode.CONTEST_NOT_FOUND));

        User leader = userRepository.findById(leaderId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

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

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_NOT_FOUND));

        if (!team.getLeader().getId().equals(leaderId)) {
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

        //
    }
}
