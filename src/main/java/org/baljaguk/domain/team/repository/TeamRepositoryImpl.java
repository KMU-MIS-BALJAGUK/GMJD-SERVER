package org.baljaguk.domain.team.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.entity.QContest;
import org.baljaguk.domain.team.dto.response.MyRecruitListResponse;
import org.baljaguk.domain.team.entity.*;
import org.springframework.stereotype.Repository;
import org.baljaguk.domain.team.entity.RegisterStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.baljaguk.domain.team.entity.QTeam.team;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, Long> countByContestIdsGrouped(List<Long> contestIds, TeamStatus status) {

        if (contestIds == null || contestIds.isEmpty()) {
                return Map.of();
        }

        List<Tuple> results = queryFactory
                .select(team.contest.id, team.count())
                .from(team)
                .where(
                        team.contest.id.in(contestIds),
                        team.status.eq(status)
                )
                .groupBy(team.contest.id)
                .fetch();

        // 결과를 Map<Long, Long> 으로 변환
        return results.stream().collect(
                Collectors.toMap(
                        tuple -> tuple.get(team.contest.id),
                        tuple -> tuple.get(team.count())
                )
        );
    }

    @Override
    public List<Team> findClosedTeamsByUserId(Long userId) {

        QTeam team = QTeam.team;
        QTeamMember teamMember = QTeamMember.teamMember;
        QContest contest = QContest.contest;

        return queryFactory
                .select(team)
                .from(teamMember)
                .join(teamMember.team, team)
                .join(team.contest, contest).fetchJoin() // ⭐ Contest fetch join
                .where(
                        teamMember.member.id.eq(userId),
                        team.status.eq(TeamStatus.CLOSED)
                )
                .fetch();
    }
}
