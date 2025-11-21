package org.baljaguk.domain.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.entity.QTeamApply;
import org.baljaguk.domain.team.entity.RegisterStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TeamApplyRepositoryImpl implements TeamApplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countRequestedApplyByTeamId(Long teamId) {
        QTeamApply ta = QTeamApply.teamApply;

        return queryFactory
                .select(ta.id.count())
                .from(ta)
                .where(
                        ta.team.id.eq(teamId),
                        ta.status.eq(RegisterStatus.REQUESTED)
                )
                .fetchOne();
    }
}
