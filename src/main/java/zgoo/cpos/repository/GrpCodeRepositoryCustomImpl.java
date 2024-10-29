package zgoo.cpos.repository;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import zgoo.cpos.domain.GrpCode;
import zgoo.cpos.domain.QGrpCode;

@RequiredArgsConstructor
public class GrpCodeRepositoryCustomImpl implements GrpCodeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QGrpCode grpCode = QGrpCode.grpCode1;

    @Override
    public List<GrpCode> findAll() {
        List<GrpCode> findList = queryFactory
                .selectFrom(grpCode)
                .fetch();

        return findList;
    }

    @Override
    public GrpCode findByGrpCode(String grpcode) {
        @Nullable
        GrpCode findOne = queryFactory
                .selectFrom(grpCode)
                .where(grpCode.grpCode.eq(grpcode))
                .fetchOne();

        return findOne;
    }

    @Override
    public GrpCode findByGrpCodeName(String name) {
        @Nullable
        GrpCode findOne = queryFactory
                .selectFrom(grpCode)
                .where(grpCode.grpcdName.eq(name))
                .fetchOne();

        return findOne;
    }

    @Override
    public Long deleteGrpCode(String grpcode) {
        return queryFactory
                .delete(grpCode)
                .where(grpCode.grpCode.eq(grpcode))
                .execute();
    }

}
