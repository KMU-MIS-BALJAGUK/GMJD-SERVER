package org.baljaguk.domain.team.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.dto.response.TeamApplyStatusResponse;
import org.baljaguk.domain.team.dto.response.TeamDetailResponse;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamStatus;
import org.baljaguk.domain.team.exception.TeamException;
import org.baljaguk.domain.team.repository.TeamRepository;
import org.baljaguk.domain.teamapplication.repository.TeamApplicationRepository;
import org.baljaguk.domain.teammember.repository.TeamMemberRepository;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamApplicationRepository teamApplicationRepository;

    private static final String STATUS_PENDING = "대기중";

    /** 팀 신청 가능 여부 조회 */


    @Override
    public TeamApplyStatusResponse getApplyStatus(Long teamId, Long userId) {

        // 1) 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TeamException(ErrorCode.USER_NOT_FOUND));

        // 2) 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_NOT_FOUND));

        // 3) 팀장 본인은 신청 불가
        if (team.getTeamLeader().getId().equals(user.getId())) {
            return new TeamApplyStatusResponse(false, ErrorCode.CANNOT_APPLY_TO_OWN_TEAM.getMessage());
        }

        // 4) 이미 팀원인 경우
        if (teamMemberRepository.existsByTeamAndUser(team, user)) {
            return new TeamApplyStatusResponse(false, ErrorCode.ALREADY_TEAM_MEMBER.getMessage());
        }

        // 5) 이미 대기중 신청이 있는 경우
        if (teamApplicationRepository.existsByTeamAndUserAndStatus(team, user, STATUS_PENDING)) {
            return new TeamApplyStatusResponse(false, ErrorCode.ALREADY_APPLIED.getMessage());
        }

        // 6) 팀 상태가 모집 중이 아닌 경우
        if (!TeamStatus.OPEN.equals(team.getStatus())) {
            return new TeamApplyStatusResponse(false, ErrorCode.TEAM_NOT_RECRUITING.getMessage());
        }

        // 7) 팀의 인원 초과인 경우
        long currentMembers = teamMemberRepository.countByTeam(team);
        if (currentMembers >= team.getMaxMember()) {
            return new TeamApplyStatusResponse(false, ErrorCode.TEAM_IS_FULL.getMessage());
        }

        // 8) 신청 가능
        return new TeamApplyStatusResponse(true, "신청 가능합니다.");
    }

    /** 팀 상세 조회 */

    @Override
    public TeamDetailResponse getTeamDetail(Long teamId) {


        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_NOT_FOUND));


        long currentMembers = teamMemberRepository.countByTeam(team);


        return new TeamDetailResponse(
                team.getId(),               //팀 ID
                team.getTitle(),            //팀 제목
                team.getIntroduction(),     //팀 소개
                team.getMaxMember(),        //최대 인원
                (int) currentMembers,       //현재 인원
                team.getStatus().name()     //팀 상태
        );
    }
}
