package zgoo.cpos.domain.company;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompanyRelationInfo is a Querydsl query type for CompanyRelationInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyRelationInfo extends EntityPathBase<CompanyRelationInfo> {

    private static final long serialVersionUID = 195760520L;

    public static final QCompanyRelationInfo companyRelationInfo = new QCompanyRelationInfo("companyRelationInfo");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath parentCompanyName = createString("parentCompanyName");

    public QCompanyRelationInfo(String variable) {
        super(CompanyRelationInfo.class, forVariable(variable));
    }

    public QCompanyRelationInfo(Path<? extends CompanyRelationInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompanyRelationInfo(PathMetadata metadata) {
        super(CompanyRelationInfo.class, metadata);
    }

}

