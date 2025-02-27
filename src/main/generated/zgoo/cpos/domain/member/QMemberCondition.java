package zgoo.cpos.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberCondition is a Querydsl query type for MemberCondition
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberCondition extends EntityPathBase<MemberCondition> {

    private static final long serialVersionUID = 625253947L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberCondition memberCondition = new QMemberCondition("memberCondition");

    public final StringPath agreeYn = createString("agreeYn");

    public final StringPath conditionCode = createString("conditionCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final StringPath section = createString("section");

    public QMemberCondition(String variable) {
        this(MemberCondition.class, forVariable(variable), INITS);
    }

    public QMemberCondition(Path<? extends MemberCondition> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberCondition(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberCondition(PathMetadata metadata, PathInits inits) {
        this(MemberCondition.class, metadata, inits);
    }

    public QMemberCondition(Class<? extends MemberCondition> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

