package org.baljaguk.domain.team.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeam is a Querydsl query type for Team
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeam extends EntityPathBase<Team> {

    private static final long serialVersionUID = -1012775146L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeam team = new QTeam("team");

    public final org.baljaguk.global.entity.QBaseEntity _super = new org.baljaguk.global.entity.QBaseEntity(this);

    public final org.baljaguk.domain.contest.entity.QContest contest;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduction = createString("introduction");

    public final NumberPath<Integer> maxMember = createNumber("maxMember", Integer.class);

    public final StringPath memo = createString("memo");

    public final StringPath question = createString("question");

    public final EnumPath<TeamStatus> status = createEnum("status", TeamStatus.class);

    public final org.baljaguk.domain.user.entity.QUser teamLeader;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTeam(String variable) {
        this(Team.class, forVariable(variable), INITS);
    }

    public QTeam(Path<? extends Team> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeam(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeam(PathMetadata metadata, PathInits inits) {
        this(Team.class, metadata, inits);
    }

    public QTeam(Class<? extends Team> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contest = inits.isInitialized("contest") ? new org.baljaguk.domain.contest.entity.QContest(forProperty("contest")) : null;
        this.teamLeader = inits.isInitialized("teamLeader") ? new org.baljaguk.domain.user.entity.QUser(forProperty("teamLeader")) : null;
    }

}

