package org.baljaguk.domain.contest.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
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

            // QueryDSL stringTemplate 로 공백 제거한 컬럼 생성
            var trimmedName = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.name);
            var trimmedOrgName = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.organizationName);
            var trimmedCompanyType = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.companyType);
            var trimmedCategories = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.categories);

            builder.or(trimmedName.lower().contains(keyword.toLowerCase()))
                    .or(trimmedOrgName.lower().contains(keyword.toLowerCase()))
                    .or(trimmedCompanyType.lower().contains(keyword.toLowerCase()))
                    .or(trimmedCategories.lower().contains(keyword.toLowerCase()));
        }

        return queryFactory
                .selectFrom(c)
                .where(builder)
                .fetch();
    }
}
