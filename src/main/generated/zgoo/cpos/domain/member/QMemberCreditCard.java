package zgoo.cpos.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberCreditCard is a Querydsl query type for MemberCreditCard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberCreditCard extends EntityPathBase<MemberCreditCard> {

    private static final long serialVersionUID = -1725929335L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberCreditCard memberCreditCard = new QMemberCreditCard("memberCreditCard");

    public final StringPath cardNum = createString("cardNum");

    public final StringPath fnCode = createString("fnCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public final StringPath representativeCard = createString("representativeCard");

    public final StringPath tid = createString("tid");

    public QMemberCreditCard(String variable) {
        this(MemberCreditCard.class, forVariable(variable), INITS);
    }

    public QMemberCreditCard(Path<? extends MemberCreditCard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberCreditCard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberCreditCard(PathMetadata metadata, PathInits inits) {
        this(MemberCreditCard.class, metadata, inits);
    }

    public QMemberCreditCard(Class<? extends MemberCreditCard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

