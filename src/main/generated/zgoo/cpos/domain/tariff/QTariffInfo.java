package zgoo.cpos.domain.tariff;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTariffInfo is a Querydsl query type for TariffInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTariffInfo extends EntityPathBase<TariffInfo> {

    private static final long serialVersionUID = -1510469554L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTariffInfo tariffInfo = new QTariffInfo("tariffInfo");

    public final NumberPath<Integer> hour = createNumber("hour", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> memFastUnitCost = createNumber("memFastUnitCost", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> memSlowUnitCost = createNumber("memSlowUnitCost", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> nomemFastUnitCost = createNumber("nomemFastUnitCost", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> nomemSlowUnitCost = createNumber("nomemSlowUnitCost", java.math.BigDecimal.class);

    public final QTariffPolicy tariffPolicy;

    public QTariffInfo(String variable) {
        this(TariffInfo.class, forVariable(variable), INITS);
    }

    public QTariffInfo(Path<? extends TariffInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTariffInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTariffInfo(PathMetadata metadata, PathInits inits) {
        this(TariffInfo.class, metadata, inits);
    }

    public QTariffInfo(Class<? extends TariffInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tariffPolicy = inits.isInitialized("tariffPolicy") ? new QTariffPolicy(forProperty("tariffPolicy"), inits.get("tariffPolicy")) : null;
    }

}

