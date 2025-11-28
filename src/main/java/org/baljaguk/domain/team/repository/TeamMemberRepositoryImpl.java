package org.baljaguk.domain.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.entity.QTeamMember;
import org.baljaguk.domain.team.entity.TeamMember;
import org.baljaguk.domain.user.entity.QUser;

import java.util.List;

@RequiredArgsConstructor
public class TeamMemberRepositoryImpl implements TeamMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TeamMember> findAllWithUserByTeamId(Long teamId) {
        QTeamMember tm = QTeamMember.teamMember;
        QUser user = QUser.user;

        return queryFactory
                .select(tm)
                .from(tm)
                .join(tm.member, user).fetchJoin()
                .where(tm.team.id.eq(teamId))
                .fetch();
    }

    @Override
    public List<Long> findTeamIdsByMemberId(Long memberId) {

        QTeamMember teamMember = QTeamMember.teamMember;


        return queryFactory
                .select(teamMember.team.id)
                .from(teamMember)
                .where(teamMember.member.id.eq(memberId))
                .fetch(); //List 형태 반환
    }

}

