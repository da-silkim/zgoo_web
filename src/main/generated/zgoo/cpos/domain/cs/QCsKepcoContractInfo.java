package zgoo.cpos.domain.cs;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCsKepcoContractInfo is a Querydsl query type for CsKepcoContractInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCsKepcoContractInfo extends EntityPathBase<CsKepcoContractInfo> {

    private static final long serialVersionUID = -204340190L;

    public static final QCsKepcoContractInfo csKepcoContractInfo = new QCsKepcoContractInfo("csKepcoContractInfo");

    public final NumberPath<Integer> contPower = createNumber("contPower", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath KepcoCustNo = createString("KepcoCustNo");

    public final DatePath<java.time.LocalDate> openingDate = createDate("openingDate", java.time.LocalDate.class);

    public final NumberPath<Integer> rcvCapacity = createNumber("rcvCapacity", Integer.class);

    public final StringPath rcvCapacityMethod = createString("rcvCapacityMethod");

    public final StringPath voltageType = createString("voltageType");

    public QCsKepcoContractInfo(String variable) {
        super(CsKepcoContractInfo.class, forVariable(variable));
    }

    public QCsKepcoContractInfo(Path<? extends CsKepcoContractInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCsKepcoContractInfo(PathMetadata metadata) {
        super(CsKepcoContractInfo.class, metadata);
    }

}

