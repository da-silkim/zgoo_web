package zgoo.cpos.type;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommonCodeKey is a Querydsl query type for CommonCodeKey
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCommonCodeKey extends BeanPath<CommonCodeKey> {

    private static final long serialVersionUID = -597494321L;

    public static final QCommonCodeKey commonCodeKey = new QCommonCodeKey("commonCodeKey");

    public final StringPath commonCode = createString("commonCode");

    public final StringPath grpCode = createString("grpCode");

    public QCommonCodeKey(String variable) {
        super(CommonCodeKey.class, forVariable(variable));
    }

    public QCommonCodeKey(Path<? extends CommonCodeKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommonCodeKey(PathMetadata metadata) {
        super(CommonCodeKey.class, metadata);
    }

}

