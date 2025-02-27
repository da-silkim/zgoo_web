package zgoo.cpos.domain.code;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommonCode is a Querydsl query type for CommonCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommonCode extends EntityPathBase<CommonCode> {

    private static final long serialVersionUID = 1636439787L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommonCode commonCode1 = new QCommonCode("commonCode1");

    public final StringPath commonCode = createString("commonCode");

    public final QGrpCode group;

    public final DateTimePath<java.time.LocalDateTime> modDt = createDateTime("modDt", java.time.LocalDateTime.class);

    public final StringPath modUserId = createString("modUserId");

    public final StringPath name = createString("name");

    public final StringPath refCode1 = createString("refCode1");

    public final StringPath refCode2 = createString("refCode2");

    public final StringPath refCode3 = createString("refCode3");

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public final StringPath regUserId = createString("regUserId");

    public final StringPath reserved = createString("reserved");

    public QCommonCode(String variable) {
        this(CommonCode.class, forVariable(variable), INITS);
    }

    public QCommonCode(Path<? extends CommonCode> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommonCode(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommonCode(PathMetadata metadata, PathInits inits) {
        this(CommonCode.class, metadata, inits);
    }

    public QCommonCode(Class<? extends CommonCode> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QGrpCode(forProperty("group")) : null;
    }

}

