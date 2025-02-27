package zgoo.cpos.type;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTariffPolicyId is a Querydsl query type for TariffPolicyId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QTariffPolicyId extends BeanPath<TariffPolicyId> {

    private static final long serialVersionUID = -1827511511L;

    public static final QTariffPolicyId tariffPolicyId = new QTariffPolicyId("tariffPolicyId");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final NumberPath<Long> policyId = createNumber("policyId", Long.class);

    public final NumberPath<Long> tariffId = createNumber("tariffId", Long.class);

    public QTariffPolicyId(String variable) {
        super(TariffPolicyId.class, forVariable(variable));
    }

    public QTariffPolicyId(Path<? extends TariffPolicyId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTariffPolicyId(PathMetadata metadata) {
        super(TariffPolicyId.class, metadata);
    }

}

