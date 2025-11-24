package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.team.entity.RegisterStatus;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamApply;
import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamApplyRepository extends JpaRepository<TeamApply, Long>, TeamApplyRepositoryCustom {
    boolean existsByUserAndTeam(User user, Team team);

    boolean existsByUserAndTeamContestAndStatus(User user, Contest contest, RegisterStatus status);

    @Query("""
    select ta 
    from TeamApply ta
    join fetch ta.team t
    join fetch t.contest c
    join fetch t.teamLeader tl
    where ta.user = :user
""")
    List<TeamApply> findAllWithTeamAndContestByUser(@Param("user") User user);

    @Query("""
    select distinct ta
    from TeamApply ta
    join fetch ta.user u
    left join fetch ta.answer ans
    left join fetch ans.question q
    where ta.team.id = :teamId
      and ta.user.id = :applicantUserId
""")
    Optional<TeamApply> findApplyDetail(
            @Param("teamId") Long teamId,
            @Param("applicantUserId") Long applicantUserId
    );
}
