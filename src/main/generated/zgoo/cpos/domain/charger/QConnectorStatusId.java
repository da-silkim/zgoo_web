package zgoo.cpos.domain.charger;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QConnectorStatusId is a Querydsl query type for ConnectorStatusId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QConnectorStatusId extends BeanPath<ConnectorStatusId> {

    private static final long serialVersionUID = -656040580L;

    public static final QConnectorStatusId connectorStatusId = new QConnectorStatusId("connectorStatusId");

    public final StringPath chargerId = createString("chargerId");

    public final NumberPath<Integer> connectorId = createNumber("connectorId", Integer.class);

    public QConnectorStatusId(String variable) {
        super(ConnectorStatusId.class, forVariable(variable));
    }

    public QConnectorStatusId(Path<? extends ConnectorStatusId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConnectorStatusId(PathMetadata metadata) {
        super(ConnectorStatusId.class, metadata);
    }

}

