package zgoo.cpos.domain.company;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = 1462748574L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompany company = new QCompany("company");

    public final StringPath address = createString("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final StringPath bizKind = createString("bizKind");

    public final StringPath bizNum = createString("bizNum");

    public final StringPath bizType = createString("bizType");

    public final StringPath ceoName = createString("ceoName");

    public final StringPath companyCode = createString("companyCode");

    public final QCompanyContract companyContract;

    public final StringPath companyLv = createString("companyLv");

    public final StringPath companyName = createString("companyName");

    public final QCompanyPG companyPG;

    public final QCompanyRelationInfo companyRelationInfo;

    public final StringPath companyType = createString("companyType");

    public final StringPath consignmentPayment = createString("consignmentPayment");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath headPhone = createString("headPhone");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath logoUrl = createString("logoUrl");

    public final StringPath staffEmail = createString("staffEmail");

    public final StringPath staffName = createString("staffName");

    public final StringPath staffPhone = createString("staffPhone");

    public final StringPath staffTel = createString("staffTel");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath zipcode = createString("zipcode");

    public QCompany(String variable) {
        this(Company.class, forVariable(variable), INITS);
    }

    public QCompany(Path<? extends Company> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCompany(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCompany(PathMetadata metadata, PathInits inits) {
        this(Company.class, metadata, inits);
    }

    public QCompany(Class<? extends Company> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.companyContract = inits.isInitialized("companyContract") ? new QCompanyContract(forProperty("companyContract")) : null;
        this.companyPG = inits.isInitialized("companyPG") ? new QCompanyPG(forProperty("companyPG")) : null;
        this.companyRelationInfo = inits.isInitialized("companyRelationInfo") ? new QCompanyRelationInfo(forProperty("companyRelationInfo")) : null;
    }

}

