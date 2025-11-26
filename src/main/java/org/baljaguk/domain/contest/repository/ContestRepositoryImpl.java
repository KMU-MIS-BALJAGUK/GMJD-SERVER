package org.baljaguk.domain.contest.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.contest.entity.QContest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContestRepositoryImpl implements ContestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Contest> searchByKeyword(String keyword, Pageable pageable) {
        QContest c = QContest.contest;

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isBlank()) {

            var trimmedName = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.name);
            var trimmedOrgName = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.organizationName);
            var trimmedCompanyType = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.companyType);
            var trimmedCategories = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.categories);

            builder.or(trimmedName.lower().contains(keyword.toLowerCase()))
                    .or(trimmedOrgName.lower().contains(keyword.toLowerCase()))
                    .or(trimmedCompanyType.lower().contains(keyword.toLowerCase()))
                    .or(trimmedCategories.lower().contains(keyword.toLowerCase()));
        }

        // 전체 개수 조회
        Long totalCount = queryFactory
                .select(c.count())
                .from(c)
                .where(builder)
                .fetchOne();
        long total = totalCount != null ? totalCount : 0L;

        // 실제 데이터 조회 + 페이징
        List<Contest> results = queryFactory
                .selectFrom(c)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Page<Contest> findContestsWithFilterAndSort(
            List<String> categoryNames,
            String sortType,
            Pageable pageable
    ) {
        QContest c = QContest.contest;

        BooleanBuilder builder = new BooleanBuilder();

        // 카테고리 필터링
        if (categoryNames != null && !categoryNames.isEmpty()) {
            BooleanBuilder categoryBuilder = new BooleanBuilder();
            categoryNames.forEach(category ->
                    categoryBuilder.or(c.categories.containsIgnoreCase(category))
            );
            builder.and(categoryBuilder);
        }

        // 정렬 처리
        OrderSpecifier<?> orderSpecifier = switch (sortType) {
            case "popular" -> c.views.desc();
            case "deadline" -> c.endDate.asc();
            default -> c.startDate.desc();
        };

        // 데이터 조회
        List<Contest> results = queryFactory
                .selectFrom(c)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // total count 조회
        Long totalCount = queryFactory
                .select(c.count())
                .from(c)
                .where(builder)
                .fetchOne();
        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(results, pageable, totalCount);
    }
}
