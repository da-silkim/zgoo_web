package zgoo.cpos.type;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTariffId is a Querydsl query type for TariffId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QTariffId extends BeanPath<TariffId> {

    private static final long serialVersionUID = 792453975L;

    public static final QTariffId tariffId1 = new QTariffId("tariffId1");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final NumberPath<Long> tariffId = createNumber("tariffId", Long.class);

    public QTariffId(String variable) {
        super(TariffId.class, forVariable(variable));
    }

    public QTariffId(Path<? extends TariffId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTariffId(PathMetadata metadata) {
        super(TariffId.class, metadata);
    }

}

