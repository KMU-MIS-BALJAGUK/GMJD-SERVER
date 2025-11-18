package org.baljaguk.domain.teammember.repository;

import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.teammember.entity.TeamMember; // (TeamMember 엔티티 경로)
import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// (가정) TeamMember 엔티티의 ID 타입이 Long이라고 가정합니다.
// public class... 가 아니라 public interface... 입니다!
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    // [검사 2] 이미 팀원인지 확인
    boolean existsByTeamAndUser(Team team, User user);

    // [검사 5] 현재 팀원 수 카운트
    long countByTeam(Team team);
}