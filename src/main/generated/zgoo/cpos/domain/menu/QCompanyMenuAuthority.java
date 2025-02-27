package zgoo.cpos.domain.menu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompanyMenuAuthority is a Querydsl query type for CompanyMenuAuthority
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyMenuAuthority extends EntityPathBase<CompanyMenuAuthority> {

    private static final long serialVersionUID = -1098011736L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompanyMenuAuthority companyMenuAuthority = new QCompanyMenuAuthority("companyMenuAuthority");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMenu menu;

    public final StringPath menuCode = createString("menuCode");

    public final StringPath useYn = createString("useYn");

    public QCompanyMenuAuthority(String variable) {
        this(CompanyMenuAuthority.class, forVariable(variable), INITS);
    }

    public QCompanyMenuAuthority(Path<? extends CompanyMenuAuthority> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCompanyMenuAuthority(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCompanyMenuAuthority(PathMetadata metadata, PathInits inits) {
        this(CompanyMenuAuthority.class, metadata, inits);
    }

    public QCompanyMenuAuthority(Class<? extends CompanyMenuAuthority> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.menu = inits.isInitialized("menu") ? new QMenu(forProperty("menu")) : null;
    }

}

