package zgoo.cpos.domain.company;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCpPlanPolicy is a Querydsl query type for CpPlanPolicy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCpPlanPolicy extends EntityPathBase<CpPlanPolicy> {

    private static final long serialVersionUID = 1582981511L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCpPlanPolicy cpPlanPolicy = new QCpPlanPolicy("cpPlanPolicy");

    public final QCompany company;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QCpPlanPolicy(String variable) {
        this(CpPlanPolicy.class, forVariable(variable), INITS);
    }

    public QCpPlanPolicy(Path<? extends CpPlanPolicy> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCpPlanPolicy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCpPlanPolicy(PathMetadata metadata, PathInits inits) {
        this(CpPlanPolicy.class, metadata, inits);
    }

    public QCpPlanPolicy(Class<? extends CpPlanPolicy> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company"), inits.get("company")) : null;
    }

}

