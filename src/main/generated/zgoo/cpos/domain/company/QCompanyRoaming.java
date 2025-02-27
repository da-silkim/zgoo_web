package zgoo.cpos.domain.company;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompanyRoaming is a Querydsl query type for CompanyRoaming
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyRoaming extends EntityPathBase<CompanyRoaming> {

    private static final long serialVersionUID = 522535067L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompanyRoaming companyRoaming = new QCompanyRoaming("companyRoaming");

    public final QCompany company;

    public final StringPath institutionCode = createString("institutionCode");

    public final StringPath institutionEmail = createString("institutionEmail");

    public final StringPath institutionKey = createString("institutionKey");

    public final NumberPath<Long> romaing_id = createNumber("romaing_id", Long.class);

    public QCompanyRoaming(String variable) {
        this(CompanyRoaming.class, forVariable(variable), INITS);
    }

    public QCompanyRoaming(Path<? extends CompanyRoaming> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCompanyRoaming(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCompanyRoaming(PathMetadata metadata, PathInits inits) {
        this(CompanyRoaming.class, metadata, inits);
    }

    public QCompanyRoaming(Class<? extends CompanyRoaming> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company"), inits.get("company")) : null;
    }

}

