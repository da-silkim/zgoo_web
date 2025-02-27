package zgoo.cpos.domain.charger;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConnectorStatus is a Querydsl query type for ConnectorStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConnectorStatus extends EntityPathBase<ConnectorStatus> {

    private static final long serialVersionUID = 504344705L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConnectorStatus connectorStatus = new QConnectorStatus("connectorStatus");

    public final QConnectorStatusId id;

    public final StringPath status = createString("status");

    public final DateTimePath<java.time.LocalDateTime> timestamp = createDateTime("timestamp", java.time.LocalDateTime.class);

    public QConnectorStatus(String variable) {
        this(ConnectorStatus.class, forVariable(variable), INITS);
    }

    public QConnectorStatus(Path<? extends ConnectorStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConnectorStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConnectorStatus(PathMetadata metadata, PathInits inits) {
        this(ConnectorStatus.class, metadata, inits);
    }

    public QConnectorStatus(Class<? extends ConnectorStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QConnectorStatusId(forProperty("id")) : null;
    }

}

