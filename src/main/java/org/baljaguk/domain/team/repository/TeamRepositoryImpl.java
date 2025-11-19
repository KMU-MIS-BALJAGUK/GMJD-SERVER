package org.baljaguk.domain.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.entity.QTeam;
import org.baljaguk.domain.team.entity.TeamStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public long countByContestIdAndStatus(Long contestId, TeamStatus status) {
        QTeam t = QTeam.team;

        return queryFactory
                .select(t.count())
                .from(t)
                .where(
                        t.contest.id.eq(contestId),
                        t.status.eq(status)
                )
                .fetchOne();
    }
}
