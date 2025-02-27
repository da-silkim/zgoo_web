package zgoo.cpos.domain.tariff;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTariffPolicy is a Querydsl query type for TariffPolicy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTariffPolicy extends EntityPathBase<TariffPolicy> {

    private static final long serialVersionUID = 339208402L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTariffPolicy tariffPolicy = new QTariffPolicy("tariffPolicy");

    public final StringPath apply_code = createString("apply_code");

    public final DateTimePath<java.time.LocalDateTime> apply_date = createDateTime("apply_date", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final zgoo.cpos.domain.company.QCpPlanPolicy policy;

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public QTariffPolicy(String variable) {
        this(TariffPolicy.class, forVariable(variable), INITS);
    }

    public QTariffPolicy(Path<? extends TariffPolicy> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTariffPolicy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTariffPolicy(PathMetadata metadata, PathInits inits) {
        this(TariffPolicy.class, metadata, inits);
    }

    public QTariffPolicy(Class<? extends TariffPolicy> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.policy = inits.isInitialized("policy") ? new zgoo.cpos.domain.company.QCpPlanPolicy(forProperty("policy"), inits.get("policy")) : null;
    }

}

