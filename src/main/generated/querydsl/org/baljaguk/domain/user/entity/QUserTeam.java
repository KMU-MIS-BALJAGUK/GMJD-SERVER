package org.baljaguk.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserTeam is a Querydsl query type for UserTeam
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserTeam extends EntityPathBase<UserTeam> {

    private static final long serialVersionUID = 748868207L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserTeam userTeam = new QUserTeam("userTeam");

    public final StringPath answer = createString("answer");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<RegisterStatus> status = createEnum("status", RegisterStatus.class);

    public final org.baljaguk.domain.team.entity.QTeam team;

    public final QUser user;

    public QUserTeam(String variable) {
        this(UserTeam.class, forVariable(variable), INITS);
    }

    public QUserTeam(Path<? extends UserTeam> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserTeam(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserTeam(PathMetadata metadata, PathInits inits) {
        this(UserTeam.class, metadata, inits);
    }

    public QUserTeam(Class<? extends UserTeam> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.team = inits.isInitialized("team") ? new org.baljaguk.domain.team.entity.QTeam(forProperty("team"), inits.get("team")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

