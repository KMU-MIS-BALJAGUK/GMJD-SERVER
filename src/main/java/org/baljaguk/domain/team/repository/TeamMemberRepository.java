package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamMember;
import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, TeamMemberRepositoryCustom {

    Long countByTeam(Team team);

    boolean existsByMemberAndTeamContest(User user, Contest contest);

    Optional<TeamMember> findByTeamAndMember(Team team, User member);

    Optional<TeamMember> findByTeamIdAndMemberId(Long teamId, Long memberId);

    List<TeamMember> findAllByTeam_Id(Long teamId);
  
    Long countByTeamId(Long teamId);
}
