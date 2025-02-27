package zgoo.cpos.domain.cp;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCpModel is a Querydsl query type for CpModel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCpModel extends EntityPathBase<CpModel> {

    private static final long serialVersionUID = 1325562153L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCpModel cpModel = new QCpModel("cpModel");

    public final zgoo.cpos.domain.company.QCompany company;

    public final StringPath cpType = createString("cpType");

    public final StringPath dualYn = createString("dualYn");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath installationType = createString("installationType");

    public final StringPath manufCd = createString("manufCd");

    public final DateTimePath<java.time.LocalDateTime> modDt = createDateTime("modDt", java.time.LocalDateTime.class);

    public final StringPath modelCode = createString("modelCode");

    public final StringPath modelName = createString("modelName");

    public final NumberPath<Integer> powerUnit = createNumber("powerUnit", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public final StringPath userId = createString("userId");

    public QCpModel(String variable) {
        this(CpModel.class, forVariable(variable), INITS);
    }

    public QCpModel(Path<? extends CpModel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCpModel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCpModel(PathMetadata metadata, PathInits inits) {
        this(CpModel.class, metadata, inits);
    }

    public QCpModel(Class<? extends CpModel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new zgoo.cpos.domain.company.QCompany(forProperty("company"), inits.get("company")) : null;
    }

}

