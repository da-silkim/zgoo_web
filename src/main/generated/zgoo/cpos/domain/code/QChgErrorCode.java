package zgoo.cpos.domain.code;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChgErrorCode is a Querydsl query type for ChgErrorCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChgErrorCode extends EntityPathBase<ChgErrorCode> {

    private static final long serialVersionUID = -2027795130L;

    public static final QChgErrorCode chgErrorCode = new QChgErrorCode("chgErrorCode");

    public final StringPath errCode = createString("errCode");

    public final StringPath errName = createString("errName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath menufCode = createString("menufCode");

    public final DateTimePath<java.time.LocalDateTime> modDt = createDateTime("modDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public QChgErrorCode(String variable) {
        super(ChgErrorCode.class, forVariable(variable));
    }

    public QChgErrorCode(Path<? extends ChgErrorCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChgErrorCode(PathMetadata metadata) {
        super(ChgErrorCode.class, metadata);
    }

}

