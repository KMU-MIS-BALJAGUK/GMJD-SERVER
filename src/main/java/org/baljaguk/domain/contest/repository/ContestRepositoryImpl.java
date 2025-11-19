package org.baljaguk.domain.contest.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.contest.entity.QContest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContestRepositoryImpl implements ContestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Contest> searchByKeyword(String keyword) {
        QContest c = QContest.contest;

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isBlank()) {
            builder.or(c.name.containsIgnoreCase(keyword))
                    .or(c.organizationName.containsIgnoreCase(keyword))
                    .or(c.companyType.containsIgnoreCase(keyword));
        }

        return queryFactory
                .selectFrom(c)
                .where(builder)
                .fetch();
    }
}
