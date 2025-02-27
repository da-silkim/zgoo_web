package zgoo.cpos.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1251834176L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final StringPath address = createString("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final StringPath birth = createString("birth");

    public final zgoo.cpos.domain.biz.QBizInfo biz;

    public final StringPath bizType = createString("bizType");

    public final zgoo.cpos.domain.company.QCompany company;

    public final StringPath creditcardStat = createString("creditcardStat");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath idTag = createString("idTag");

    public final DateTimePath<java.time.LocalDateTime> joinedDt = createDateTime("joinedDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> loginDt = createDateTime("loginDt", java.time.LocalDateTime.class);

    public final StringPath memLoginId = createString("memLoginId");

    public final DateTimePath<java.time.LocalDateTime> modDt = createDateTime("modDt", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNo = createString("phoneNo");

    public final StringPath userState = createString("userState");

    public final StringPath zipCode = createString("zipCode");

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.biz = inits.isInitialized("biz") ? new zgoo.cpos.domain.biz.QBizInfo(forProperty("biz")) : null;
        this.company = inits.isInitialized("company") ? new zgoo.cpos.domain.company.QCompany(forProperty("company"), inits.get("company")) : null;
    }

}

