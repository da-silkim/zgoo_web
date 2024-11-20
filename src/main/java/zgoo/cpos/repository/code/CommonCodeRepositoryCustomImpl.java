package zgoo.cpos.repository.code;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zgoo.cpos.domain.code.CommonCode;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.code.QGrpCode;
import zgoo.cpos.dto.code.CodeDto.CommCdBaseDto;

@RequiredArgsConstructor
public class CommonCodeRepositoryCustomImpl implements CommonCodeRepositoryCustom {
        private final JPAQueryFactory queryFactory;
        QGrpCode grpCode = QGrpCode.grpCode1;
        QCommonCode commonCode = QCommonCode.commonCode1;

        @Override
        public List<CommonCode> findAll() {
                return queryFactory
                                .selectFrom(commonCode)
                                .fetch();
        }

        @Override
        public CommonCode findCommonCodeOne(String grpcode, String commoncode) {
                return queryFactory
                                .selectFrom(commonCode)
                                .where(
                                                commonCode.group.grpCode.eq(grpcode)
                                                                .and(commonCode.commonCode.eq(commoncode)))
                                .fetchOne();
        }

        @Override
        public List<CommonCode> findAllByGrpCode(String grpcode) {
                return queryFactory
                                .selectFrom(commonCode)
                                .where(commonCode.group.grpCode.eq(grpcode))
                                .fetch();
        }

        // @Override
        // public List<GrpAndCommCdDto> findGrpAndCommCodeByGrpcode(String grpcode) {
        // return queryFactory
        // .select(Projections.constructor(GrpAndCommCdDto.class,
        // grpCode.grpCode,
        // grpCode.grpcdName,
        // commonCode.id.commonCode,
        // commonCode.name))
        // .from(grpCode)
        // .innerJoin(commonCode).on(grpCode.grpCode.eq(commonCode.id.grpCode))
        // .where(grpCode.grpCode.eq(grpcode))
        // .fetch();
        // }

        @Override
        public Long deleteCommonCodeOne(String commoncode) {
                return queryFactory
                                .delete(commonCode)
                                .where(commonCode.commonCode.eq(commoncode))
                                .execute();
        }

        @Override
        public Long deleteAllCommonCodeByGrpCode(String grpcode) {
                return queryFactory
                                .delete(commonCode)
                                .where(commonCode.group.grpCode.eq(grpcode))
                                .execute();
        }

        @Override
        public List<CommCdBaseDto> findCommonCdNamesByGrpCode(String grpcode) {
                return queryFactory
                                .select(Projections.fields(CommCdBaseDto.class,
                                                commonCode.group.grpCode.as("grpCode"),
                                                commonCode.commonCode.as("commonCode"),
                                                commonCode.name.as("commonCodeName")))
                                .from(commonCode)
                                .where(commonCode.group.grpCode.eq(grpcode))
                                .fetch();
        }

        // @Override
        // public Page<GrpAndCommCdDto> findAllByGrpCodePaging(String grpcode, Pageable
        // pageable) {
        // /* data 조회 */
        // List<GrpAndCommCdDto> contents = queryFactory
        // .select(Projections.constructor(GrpAndCommCdDto.class,
        // grpCode.grpCode,
        // grpCode.grpcdName,
        // commonCode.id.commonCode,
        // commonCode.name))
        // .from(grpCode)
        // .innerJoin(commonCode).on(grpCode.grpCode.eq(commonCode.id.grpCode))
        // .where(grpCode.grpCode.eq(grpcode))
        // .orderBy(commonCode.id.commonCode.asc())
        // .offset(pageable.getOffset()) // 몇번쨰 페이지부터 시작할 것인지
        // .limit(pageable.getPageSize()) // 페이지당 몇개의 데이터를 보여줄 것인지
        // .fetch();

        // /* data수 카운트 */
        // JPAQuery<Long> countQuery = queryFactory
        // .select(commonCode.count())
        // .from(commonCode)
        // .leftJoin(commonCode.group, grpCode)
        // .where(grpCode.grpCode.eq(grpcode));

        // /*
        // * PageableExcutionUtils
        // * 페이지의 시작이면서 컨텐츠 사이즈가 페이지 사이즈보다 작거나 마지막 페이지 일 때
        // * 카운트 쿼리를 실행하지 않는다 -> 성능 최척화 발생
        // */
        // return PageableExecutionUtils.getPage(contents, pageable, () ->
        // countQuery.fetchOne());

        // }

        @Override
        public String findCommonCodeName(String commoncode) {
                return queryFactory
                                .select(commonCode.name)
                                .from(commonCode)
                                .where(commonCode.commonCode.eq(commoncode))
                                .fetchOne();
        }

        @Override
        public List<CommCdBaseDto> commonCodeStringSort(String grpcode) {
                return queryFactory
                                .select(Projections.fields(CommCdBaseDto.class,
                                        commonCode.group.grpCode.as("grpCode"),
                                        commonCode.commonCode.as("commonCode"),
                                        commonCode.name.as("commonCodeName")))
                                .from(commonCode)
                                .where(commonCode.group.grpCode.eq(grpcode))
                                .orderBy(
                                        Expressions.numberTemplate(Integer.class, "LENGTH({0})", commonCode.commonCode).asc(),
                                        commonCode.commonCode.asc()
                                )
                                .fetch();
        }
}
