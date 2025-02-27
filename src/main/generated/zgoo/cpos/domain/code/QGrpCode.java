package zgoo.cpos.domain.code;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGrpCode is a Querydsl query type for GrpCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGrpCode extends EntityPathBase<GrpCode> {

    private static final long serialVersionUID = 1010699487L;

    public static final QGrpCode grpCode1 = new QGrpCode("grpCode1");

    public final StringPath grpcdName = createString("grpcdName");

    public final StringPath grpCode = createString("grpCode");

    public final DateTimePath<java.time.LocalDateTime> modDt = createDateTime("modDt", java.time.LocalDateTime.class);

    public final StringPath modUserId = createString("modUserId");

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public final StringPath regUserId = createString("regUserId");

    public QGrpCode(String variable) {
        super(GrpCode.class, forVariable(variable));
    }

    public QGrpCode(Path<? extends GrpCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGrpCode(PathMetadata metadata) {
        super(GrpCode.class, metadata);
    }

}

