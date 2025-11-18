package org.baljaguk.domain.teamapplication.repository;

import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.teamapplication.entity.TeamApplication; // (4/5에서 만든 것)
import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// (class가 아닌 interface 입니다!)
public interface TeamApplicationRepository extends JpaRepository<TeamApplication, Long> {

    // [검사 3] 이미 '대기중'으로 신청했는지 확인
    boolean existsByTeamAndUserAndStatus(Team team, User user, String status);
}