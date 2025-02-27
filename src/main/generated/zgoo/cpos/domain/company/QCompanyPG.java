package zgoo.cpos.domain.company;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompanyPG is a Querydsl query type for CompanyPG
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyPG extends EntityPathBase<CompanyPG> {

    private static final long serialVersionUID = 1247076373L;

    public static final QCompanyPG companyPG = new QCompanyPG("companyPG");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath merchantKey = createString("merchantKey");

    public final StringPath mid = createString("mid");

    public final DateTimePath<java.time.LocalDateTime> modDt = createDateTime("modDt", java.time.LocalDateTime.class);

    public final StringPath modUserId = createString("modUserId");

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public final StringPath regUserId = createString("regUserId");

    public final StringPath sspMallId = createString("sspMallId");

    public QCompanyPG(String variable) {
        super(CompanyPG.class, forVariable(variable));
    }

    public QCompanyPG(Path<? extends CompanyPG> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompanyPG(PathMetadata metadata) {
        super(CompanyPG.class, metadata);
    }

}

