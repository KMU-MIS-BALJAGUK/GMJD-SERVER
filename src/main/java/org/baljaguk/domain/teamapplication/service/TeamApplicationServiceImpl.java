package org.baljaguk.domain.teamapplication.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.exception.TeamException;
import org.baljaguk.domain.team.repository.TeamMemberRepository;
import org.baljaguk.domain.team.repository.TeamRepository;
import org.baljaguk.domain.teamapplication.dto.request.TeamApplyRequest;
import org.baljaguk.domain.teamapplication.dto.response.TeamApplyResponse;
import org.baljaguk.domain.teamapplication.entity.TeamApplication;
import org.baljaguk.domain.teamapplication.repository.TeamApplicationRepository;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.exception.UserException;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamApplicationServiceImpl implements TeamApplicationService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamApplicationRepository teamApplicationRepository;

    @Override
    @Transactional
    public TeamApplyResponse applyToTeam(Long teamId, Long userId, TeamApplyRequest request) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        // 정원, 모집 상태 체크
        int currentCount = teamMemberRepository.countByTeam(team);
        if (!team.isRecruiting() || currentCount >= team.getMaxMemberCount()) {
            throw new TeamException(ErrorCode.TEAM_CANNOT_APPLY);
        }

        // 이미 팀원인지
        if (teamMemberRepository.existsByTeamAndUser(team, user)) {
            throw new TeamException(ErrorCode.TEAM_ALREADY_MEMBER);
        }

        // 이미 신청했는지
        if (teamApplicationRepository.existsByTeamAndUser(team, user)) {
            throw new TeamException(ErrorCode.TEAM_ALREADY_APPLIED);
        }

        TeamApplication application = TeamApplication.builder()
                .team(team)
                .user(user)

                .build();

        teamApplicationRepository.save(application);

        return TeamApplyResponse.from(application);
    }

    @Override
    @Transactional
    public void cancelApplication(Long teamId, Long userId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        TeamApplication application = teamApplicationRepository.findAllByTeam(team).stream()
                .filter(app -> app.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_APPLICATION_NOT_FOUND));

        teamApplicationRepository.delete(application);
    }
}
