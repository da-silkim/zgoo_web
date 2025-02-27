package zgoo.cpos.domain.cs;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCsInfo is a Querydsl query type for CsInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCsInfo extends EntityPathBase<CsInfo> {

    private static final long serialVersionUID = 2087252942L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCsInfo csInfo = new QCsInfo("csInfo");

    public final StringPath address = createString("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final StringPath asNum = createString("asNum");

    public final zgoo.cpos.domain.company.QCompany company;

    public final QCsKepcoContractInfo csKepcoContractInfo;

    public final QCsLandInfo csLandInfo;

    public final StringPath facilityType = createString("facilityType");

    public final StringPath id = createString("id");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longtude = createNumber("longtude", Double.class);

    public final TimePath<java.time.LocalTime> openEndTime = createTime("openEndTime", java.time.LocalTime.class);

    public final TimePath<java.time.LocalTime> openStartTime = createTime("openStartTime", java.time.LocalTime.class);

    public final StringPath opStatus = createString("opStatus");

    public final StringPath parkingFeeYn = createString("parkingFeeYn");

    public final StringPath stationName = createString("stationName");

    public final StringPath stationType = createString("stationType");

    public final StringPath zipcode = createString("zipcode");

    public QCsInfo(String variable) {
        this(CsInfo.class, forVariable(variable), INITS);
    }

    public QCsInfo(Path<? extends CsInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCsInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCsInfo(PathMetadata metadata, PathInits inits) {
        this(CsInfo.class, metadata, inits);
    }

    public QCsInfo(Class<? extends CsInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new zgoo.cpos.domain.company.QCompany(forProperty("company"), inits.get("company")) : null;
        this.csKepcoContractInfo = inits.isInitialized("csKepcoContractInfo") ? new QCsKepcoContractInfo(forProperty("csKepcoContractInfo")) : null;
        this.csLandInfo = inits.isInitialized("csLandInfo") ? new QCsLandInfo(forProperty("csLandInfo")) : null;
    }

}

