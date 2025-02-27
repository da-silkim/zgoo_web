package zgoo.cpos.domain.charger;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCpInfo is a Querydsl query type for CpInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCpInfo extends EntityPathBase<CpInfo> {

    private static final long serialVersionUID = 954675865L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCpInfo cpInfo = new QCpInfo("cpInfo");

    public final StringPath anydeskId = createString("anydeskId");

    public final StringPath chargerName = createString("chargerName");

    public final StringPath commonType = createString("commonType");

    public final QCpModem cpmodemInfo;

    public final StringPath fwVersion = createString("fwVersion");

    public final StringPath id = createString("id");

    public final DatePath<java.time.LocalDate> installDate = createDate("installDate", java.time.LocalDate.class);

    public final StringPath location = createString("location");

    public final StringPath modelCode = createString("modelCode");

    public final zgoo.cpos.domain.company.QCpPlanPolicy planInfo;

    public final StringPath protocol = createString("protocol");

    public final StringPath reason = createString("reason");

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public final StringPath serialNo = createString("serialNo");

    public final zgoo.cpos.domain.cs.QCsInfo stationId;

    public final StringPath useYn = createString("useYn");

    public QCpInfo(String variable) {
        this(CpInfo.class, forVariable(variable), INITS);
    }

    public QCpInfo(Path<? extends CpInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCpInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCpInfo(PathMetadata metadata, PathInits inits) {
        this(CpInfo.class, metadata, inits);
    }

    public QCpInfo(Class<? extends CpInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cpmodemInfo = inits.isInitialized("cpmodemInfo") ? new QCpModem(forProperty("cpmodemInfo")) : null;
        this.planInfo = inits.isInitialized("planInfo") ? new zgoo.cpos.domain.company.QCpPlanPolicy(forProperty("planInfo"), inits.get("planInfo")) : null;
        this.stationId = inits.isInitialized("stationId") ? new zgoo.cpos.domain.cs.QCsInfo(forProperty("stationId"), inits.get("stationId")) : null;
    }

}

