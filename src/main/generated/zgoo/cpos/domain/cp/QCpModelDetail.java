package zgoo.cpos.domain.cp;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCpModelDetail is a Querydsl query type for CpModelDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCpModelDetail extends EntityPathBase<CpModelDetail> {

    private static final long serialVersionUID = 1244805850L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCpModelDetail cpModelDetail = new QCpModelDetail("cpModelDetail");

    public final StringPath cableLength = createString("cableLength");

    public final StringPath charger = createString("charger");

    public final StringPath communicationInterface = createString("communicationInterface");

    public final StringPath coolingMethod = createString("coolingMethod");

    public final QCpModel cpModel;

    public final StringPath dimensions = createString("dimensions");

    public final StringPath emc = createString("emc");

    public final StringPath emergencyBtn = createString("emergencyBtn");

    public final StringPath grdType = createString("grdType");

    public final StringPath humidity = createString("humidity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath inputCurr = createString("inputCurr");

    public final StringPath inputFrequency = createString("inputFrequency");

    public final StringPath inputPower = createString("inputPower");

    public final StringPath inputType = createString("inputType");

    public final StringPath inputVoltage = createString("inputVoltage");

    public final StringPath ipNIk = createString("ipNIk");

    public final StringPath lang = createString("lang");

    public final StringPath material = createString("material");

    public final StringPath maxOutputCurr = createString("maxOutputCurr");

    public final StringPath opAltitude = createString("opAltitude");

    public final StringPath opFunc = createString("opFunc");

    public final StringPath opTemperature = createString("opTemperature");

    public final StringPath outputVoltage = createString("outputVoltage");

    public final NumberPath<Integer> peakEfficiency = createNumber("peakEfficiency", Integer.class);

    public final NumberPath<Double> powerFactor = createNumber("powerFactor", Double.class);

    public final StringPath powerModule = createString("powerModule");

    public final StringPath protection = createString("protection");

    public final StringPath ratedPower = createString("ratedPower");

    public final StringPath rfid = createString("rfid");

    public final StringPath screen = createString("screen");

    public final StringPath standard = createString("standard");

    public final StringPath storageTemperatureRange = createString("storageTemperatureRange");

    public final StringPath temperatureDerating = createString("temperatureDerating");

    public final StringPath thdi = createString("thdi");

    public final StringPath weight = createString("weight");

    public QCpModelDetail(String variable) {
        this(CpModelDetail.class, forVariable(variable), INITS);
    }

    public QCpModelDetail(Path<? extends CpModelDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCpModelDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCpModelDetail(PathMetadata metadata, PathInits inits) {
        this(CpModelDetail.class, metadata, inits);
    }

    public QCpModelDetail(Class<? extends CpModelDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cpModel = inits.isInitialized("cpModel") ? new QCpModel(forProperty("cpModel"), inits.get("cpModel")) : null;
    }

}

