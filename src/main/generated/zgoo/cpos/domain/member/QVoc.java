package zgoo.cpos.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVoc is a Querydsl query type for Voc
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVoc extends EntityPathBase<Voc> {

    private static final long serialVersionUID = 1650568804L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoc voc = new QVoc("voc");

    public final StringPath channel = createString("channel");

    public final StringPath content = createString("content");

    public final StringPath delYn = createString("delYn");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public final StringPath replyContent = createString("replyContent");

    public final DateTimePath<java.time.LocalDateTime> replyDt = createDateTime("replyDt", java.time.LocalDateTime.class);

    public final StringPath replyStat = createString("replyStat");

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final zgoo.cpos.domain.users.QUsers user;

    public QVoc(String variable) {
        this(Voc.class, forVariable(variable), INITS);
    }

    public QVoc(Path<? extends Voc> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVoc(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVoc(PathMetadata metadata, PathInits inits) {
        this(Voc.class, metadata, inits);
    }

    public QVoc(Class<? extends Voc> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.user = inits.isInitialized("user") ? new zgoo.cpos.domain.users.QUsers(forProperty("user"), inits.get("user")) : null;
    }

}

