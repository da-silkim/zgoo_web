package zgoo.cpos.domain.charger;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCpModem is a Querydsl query type for CpModem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCpModem extends EntityPathBase<CpModem> {

    private static final long serialVersionUID = -466097505L;

    public static final QCpModem cpModem = new QCpModem("cpModem");

    public final DatePath<java.time.LocalDate> contractEnd = createDate("contractEnd", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> contractStart = createDate("contractStart", java.time.LocalDate.class);

    public final StringPath contractStatus = createString("contractStatus");

    public final StringPath dataCapacity = createString("dataCapacity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath modelName = createString("modelName");

    public final StringPath modemNo = createString("modemNo");

    public final StringPath pricePlan = createString("pricePlan");

    public final StringPath serialNo = createString("serialNo");

    public final StringPath telCorp = createString("telCorp");

    public QCpModem(String variable) {
        super(CpModem.class, forVariable(variable));
    }

    public QCpModem(Path<? extends CpModem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCpModem(PathMetadata metadata) {
        super(CpModem.class, metadata);
    }

}

