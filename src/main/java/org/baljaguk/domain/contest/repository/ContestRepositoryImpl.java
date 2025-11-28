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
    public Page<Contest> findContestsUnified(
            String keyword,
            List<String> categoryNames,
            String sortType,
            Pageable pageable
    ) {
        QContest c = QContest.contest;
        BooleanBuilder builder = new BooleanBuilder();

        // 검색 조건
        if (keyword != null && !keyword.isBlank()) {

            // 공백 제거 버전으로 검색하기 위해 stringTemplate 사용
            var trimmedName = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.name);
            var trimmedOrgName = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.organizationName);
            var trimmedCompanyType = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.companyType);
            var trimmedCategories = Expressions.stringTemplate("REPLACE({0}, ' ', '')", c.categories);

            String k = keyword.toLowerCase();

            builder.and(
                    trimmedName.lower().contains(k)
                            .or(trimmedOrgName.lower().contains(k))
                            .or(trimmedCompanyType.lower().contains(k))
                            .or(trimmedCategories.lower().contains(k))
            );
        }

        // 카테고리 필터링
        if (categoryNames != null && !categoryNames.isEmpty()) {

            BooleanBuilder categoryBuilder = new BooleanBuilder();

            for (String category : categoryNames) {
                categoryBuilder.or(c.categories.containsIgnoreCase(category));
            }

            builder.and(categoryBuilder);
        }

        // 정렬조건
        OrderSpecifier<?> orderSpecifier = switch (sortType) {
            case "popular" -> c.views.desc();      // 인기순
            case "deadline" -> c.endDate.asc();    // 마감임박
            default -> c.startDate.desc();         // 최신순
        };

        // 공모전 조회
        List<Contest> content = queryFactory
                .selectFrom(c)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 조회
        Long totalCount = queryFactory
                .select(c.count())
                .from(c)
                .where(builder)
                .fetchOne();

        long total = (totalCount != null) ? totalCount : 0L;

        return new PageImpl<>(content, pageable, total);
    }
}
