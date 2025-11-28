package org.baljaguk.domain.team.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.dto.ContestInfoDto;
import org.baljaguk.domain.contest.entity.QContest;
import org.baljaguk.domain.team.dto.QContestInfoDto;
import org.baljaguk.domain.team.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Q-클래스 임포트
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
                .join(team.contest, contest).fetchJoin()
                .where(
                        teamMember.member.id.eq(userId),
                        team.status.eq(TeamStatus.CLOSED)
                )
                .fetch();
    }

    @Override
    public Optional<Team> findTeamWithContestByTeamId(Long teamId) {
        QTeam team = QTeam.team;
        QContest contest = QContest.contest;

        Team result = queryFactory
                .select(team)
                .from(team)
                .join(team.contest, contest).fetchJoin()
                .where(team.id.eq(teamId))
                .fetchOne();

        return Optional.ofNullable(result);
    }


    @Override
    public List<ContestInfoDto>findContestInfoByTeamIds(List<Long> teamIds){

        QTeam qTeam = QTeam.team;
        QContest qContest = QContest.contest;

        if (teamIds == null || teamIds.isEmpty()) return List.of();

        return queryFactory
                .from(qTeam)
                .join(qTeam.contest, qContest)
                .where(qTeam.id.in(teamIds))
                .select(new QContestInfoDto(
                        qContest.id,        // contestId
                        qTeam.id,      // TeamId
                        qContest.name,       // contestName
                        qContest.imageUrl            // contestUrl
                ))
                .fetch();
    }
}