package zgoo.cpos.domain.biz;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBizInfo is a Querydsl query type for BizInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBizInfo extends EntityPathBase<BizInfo> {

    private static final long serialVersionUID = 2083918104L;

    public static final QBizInfo bizInfo = new QBizInfo("bizInfo");

    public final StringPath bizName = createString("bizName");

    public final StringPath bizNo = createString("bizNo");

    public final StringPath cardNum = createString("cardNum");

    public final StringPath fnCode = createString("fnCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public final StringPath tid = createString("tid");

    public QBizInfo(String variable) {
        super(BizInfo.class, forVariable(variable));
    }

    public QBizInfo(Path<? extends BizInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBizInfo(PathMetadata metadata) {
        super(BizInfo.class, metadata);
    }

}

