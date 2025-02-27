package zgoo.cpos.domain.cs;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCsLandInfo is a Querydsl query type for CsLandInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCsLandInfo extends EntityPathBase<CsLandInfo> {

    private static final long serialVersionUID = 154106969L;

    public static final QCsLandInfo csLandInfo = new QCsLandInfo("csLandInfo");

    public final DatePath<java.time.LocalDate> billDate = createDate("billDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> contractDate = createDate("contractDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath institutionName = createString("institutionName");

    public final StringPath landType = createString("landType");

    public final NumberPath<Integer> landUseRate = createNumber("landUseRate", Integer.class);

    public final StringPath staffName = createString("staffName");

    public final StringPath staffPhone = createString("staffPhone");

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public QCsLandInfo(String variable) {
        super(CsLandInfo.class, forVariable(variable));
    }

    public QCsLandInfo(Path<? extends CsLandInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCsLandInfo(PathMetadata metadata) {
        super(CsLandInfo.class, metadata);
    }

}

