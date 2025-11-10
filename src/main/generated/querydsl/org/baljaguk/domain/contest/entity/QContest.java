package org.baljaguk.domain.contest.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QContest is a Querydsl query type for Contest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContest extends EntityPathBase<Contest> {

    private static final long serialVersionUID = 1151851074L;

    public static final QContest contest = new QContest("contest");

    public final StringPath category = createString("category");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath name = createString("name");

    public final StringPath organizationName = createString("organizationName");

    public final StringPath siteUrl = createString("siteUrl");

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public QContest(String variable) {
        super(Contest.class, forVariable(variable));
    }

    public QContest(Path<? extends Contest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContest(PathMetadata metadata) {
        super(Contest.class, metadata);
    }

}

