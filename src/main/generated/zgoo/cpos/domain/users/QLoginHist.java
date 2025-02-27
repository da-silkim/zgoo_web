package zgoo.cpos.domain.users;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLoginHist is a Querydsl query type for LoginHist
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLoginHist extends EntityPathBase<LoginHist> {

    private static final long serialVersionUID = -413405353L;

    public static final QLoginHist loginHist = new QLoginHist("loginHist");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> loginDate = createDateTime("loginDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> logoutDate = createDateTime("logoutDate", java.time.LocalDateTime.class);

    public final StringPath userId = createString("userId");

    public QLoginHist(String variable) {
        super(LoginHist.class, forVariable(variable));
    }

    public QLoginHist(Path<? extends LoginHist> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLoginHist(PathMetadata metadata) {
        super(LoginHist.class, metadata);
    }

}

