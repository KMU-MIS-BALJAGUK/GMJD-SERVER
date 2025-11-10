package org.baljaguk.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecommendationLog is a Querydsl query type for RecommendationLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecommendationLog extends EntityPathBase<RecommendationLog> {

    private static final long serialVersionUID = 1007354628L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecommendationLog recommendationLog = new QRecommendationLog("recommendationLog");

    public final QUser fromUser;

    public final NumberPath<Long> Id = createNumber("Id", Long.class);

    public final QUser toUser;

    public QRecommendationLog(String variable) {
        this(RecommendationLog.class, forVariable(variable), INITS);
    }

    public QRecommendationLog(Path<? extends RecommendationLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecommendationLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecommendationLog(PathMetadata metadata, PathInits inits) {
        this(RecommendationLog.class, metadata, inits);
    }

    public QRecommendationLog(Class<? extends RecommendationLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromUser = inits.isInitialized("fromUser") ? new QUser(forProperty("fromUser")) : null;
        this.toUser = inits.isInitialized("toUser") ? new QUser(forProperty("toUser")) : null;
    }

}

