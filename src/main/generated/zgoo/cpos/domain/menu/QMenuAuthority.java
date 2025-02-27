package zgoo.cpos.domain.menu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMenuAuthority is a Querydsl query type for MenuAuthority
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMenuAuthority extends EntityPathBase<MenuAuthority> {

    private static final long serialVersionUID = 1008120643L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMenuAuthority menuAuthority = new QMenuAuthority("menuAuthority");

    public final StringPath authority = createString("authority");

    public final zgoo.cpos.domain.company.QCompany company;

    public final StringPath excelYn = createString("excelYn");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lowMenu = createString("lowMenu");

    public final StringPath midMenu = createString("midMenu");

    public final DateTimePath<java.time.LocalDateTime> modAt = createDateTime("modAt", java.time.LocalDateTime.class);

    public final StringPath modUserId = createString("modUserId");

    public final StringPath modYn = createString("modYn");

    public final StringPath readYn = createString("readYn");

    public final DateTimePath<java.time.LocalDateTime> regAt = createDateTime("regAt", java.time.LocalDateTime.class);

    public final StringPath topMenu = createString("topMenu");

    public QMenuAuthority(String variable) {
        this(MenuAuthority.class, forVariable(variable), INITS);
    }

    public QMenuAuthority(Path<? extends MenuAuthority> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMenuAuthority(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMenuAuthority(PathMetadata metadata, PathInits inits) {
        this(MenuAuthority.class, metadata, inits);
    }

    public QMenuAuthority(Class<? extends MenuAuthority> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new zgoo.cpos.domain.company.QCompany(forProperty("company"), inits.get("company")) : null;
    }

}

