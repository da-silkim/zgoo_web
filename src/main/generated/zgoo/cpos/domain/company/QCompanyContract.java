package zgoo.cpos.domain.company;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompanyContract is a Querydsl query type for CompanyContract
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyContract extends EntityPathBase<CompanyContract> {

    private static final long serialVersionUID = -974734128L;

    public static final QCompanyContract companyContract = new QCompanyContract("companyContract");

    public final StringPath asCompany = createString("asCompany");

    public final StringPath asNum = createString("asNum");

    public final DateTimePath<java.time.LocalDateTime> contractedAt = createDateTime("contractedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> contractEnd = createDateTime("contractEnd", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> contractStart = createDateTime("contractStart", java.time.LocalDateTime.class);

    public final StringPath contractStatus = createString("contractStatus");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QCompanyContract(String variable) {
        super(CompanyContract.class, forVariable(variable));
    }

    public QCompanyContract(Path<? extends CompanyContract> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompanyContract(PathMetadata metadata) {
        super(CompanyContract.class, metadata);
    }

}

