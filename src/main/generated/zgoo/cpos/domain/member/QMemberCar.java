package zgoo.cpos.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberCar is a Querydsl query type for MemberCar
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberCar extends EntityPathBase<MemberCar> {

    private static final long serialVersionUID = -190838540L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberCar memberCar = new QMemberCar("memberCar");

    public final StringPath carNum = createString("carNum");

    public final StringPath carType = createString("carType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final StringPath model = createString("model");

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public QMemberCar(String variable) {
        this(MemberCar.class, forVariable(variable), INITS);
    }

    public QMemberCar(Path<? extends MemberCar> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberCar(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberCar(PathMetadata metadata, PathInits inits) {
        this(MemberCar.class, metadata, inits);
    }

    public QMemberCar(Class<? extends MemberCar> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

