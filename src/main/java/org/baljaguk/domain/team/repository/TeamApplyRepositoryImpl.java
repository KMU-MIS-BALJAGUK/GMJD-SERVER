package org.baljaguk.domain.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.entity.QTeamApply;
import org.baljaguk.domain.team.entity.RegisterStatus;
import org.baljaguk.domain.team.entity.TeamApply;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.baljaguk.domain.team.entity.QTeamApply.teamApply;

@Repository
@RequiredArgsConstructor
public class TeamApplyRepositoryImpl implements TeamApplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countRequestedApplyByTeamId(Long teamId) {

        return queryFactory
                .select(teamApply.id.count())
                .from(teamApply)
                .where(
                        teamApply.team.id.eq(teamId),
                        teamApply.status.eq(RegisterStatus.REQUESTED)
                )
                .fetchOne();
    }

    @Override
    public List<TeamApply> findByTeamIdWithUser(Long teamId) {
        return queryFactory
                .selectFrom(teamApply)
                .join(teamApply.user).fetchJoin()
                .where(
                        teamApply.team.id.eq(teamId),
                        teamApply.status.eq(RegisterStatus.REQUESTED)
                )
                .fetch();
    }
}
