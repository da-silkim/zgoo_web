package zgoo.cpos.domain.cp;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCpConnector is a Querydsl query type for CpConnector
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCpConnector extends EntityPathBase<CpConnector> {

    private static final long serialVersionUID = -19303859L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCpConnector cpConnector = new QCpConnector("cpConnector");

    public final NumberPath<Integer> connectorId = createNumber("connectorId", Integer.class);

    public final StringPath connectorType = createString("connectorType");

    public final QCpModel cpModel;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QCpConnector(String variable) {
        this(CpConnector.class, forVariable(variable), INITS);
    }

    public QCpConnector(Path<? extends CpConnector> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCpConnector(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCpConnector(PathMetadata metadata, PathInits inits) {
        this(CpConnector.class, metadata, inits);
    }

    public QCpConnector(Class<? extends CpConnector> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cpModel = inits.isInitialized("cpModel") ? new QCpModel(forProperty("cpModel"), inits.get("cpModel")) : null;
    }

}

