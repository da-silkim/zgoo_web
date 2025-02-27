package zgoo.cpos.domain.charger;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCpStatus is a Querydsl query type for CpStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCpStatus extends EntityPathBase<CpStatus> {

    private static final long serialVersionUID = -1387802851L;

    public static final QCpStatus cpStatus = new QCpStatus("cpStatus");

    public final StringPath chargerId = createString("chargerId");

    public final StringPath connectionYn = createString("connectionYn");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastBootTime = createDateTime("lastBootTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastFwupdateTime = createDateTime("lastFwupdateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastHeartbeatRcvTime = createDateTime("lastHeartbeatRcvTime", java.time.LocalDateTime.class);

    public QCpStatus(String variable) {
        super(CpStatus.class, forVariable(variable));
    }

    public QCpStatus(Path<? extends CpStatus> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCpStatus(PathMetadata metadata) {
        super(CpStatus.class, metadata);
    }

}

