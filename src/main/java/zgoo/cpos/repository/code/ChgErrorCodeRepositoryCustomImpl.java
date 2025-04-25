package zgoo.cpos.repository.code;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QChgErrorCode;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.dto.code.ChgErrorCodeDto.ChgErrorCodeListDto;
import zgoo.cpos.dto.code.ChgErrorCodeDto.ChgErrorCodeRegDto;

@Slf4j
@RequiredArgsConstructor
public class ChgErrorCodeRepositoryCustomImpl implements ChgErrorCodeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QChgErrorCode errorCode = QChgErrorCode.chgErrorCode;
    QCommonCode manufCdName = new QCommonCode("manufCd");

    @Override
    public Page<ChgErrorCodeListDto> findErrorCodeWithPagination(Pageable pageable) {
        List<ChgErrorCodeListDto> errcdList = queryFactory.select(Projections.fields(ChgErrorCodeListDto.class,
            errorCode.id.as("errcdId"),
            errorCode.errCode.as("errCode"),
            errorCode.errName.as("errName"),
            errorCode.menufCode.as("menufCode"),
            errorCode.regDt.as("regDt"),
            Expressions.stringTemplate("IF({0} = 'DFT', '없음', {1})", errorCode.menufCode, manufCdName.name).as("menufCodeName")))
            .from(errorCode)
            .leftJoin(manufCdName).on(errorCode.menufCode.eq(manufCdName.commonCode))
            .orderBy(errorCode.regDt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .select(errorCode.count())
            .from(errorCode)
            .fetchOne();

        return new PageImpl<>(errcdList, pageable, totalCount);
    }
    
    @Override
    public Page<ChgErrorCodeListDto> searchErrorCodeWithPagination(String manuf, String searchOp, String searchContent,
            Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        log.info("manuf: {}, searchOp: {}, searchContent:{}", manuf, searchOp, searchContent);
        
        if (manuf != null && !manuf.isEmpty()) {
            builder.and(errorCode.menufCode.eq(manuf));
        }

        if (searchOp != null && (searchContent != null && !searchContent.isEmpty())) {
            if (searchOp.equals("errCode")) {
                builder.and(errorCode.errCode.contains(searchContent));
            } else if (searchOp.equals("errName")) {
                builder.and(errorCode.errName.contains(searchContent));
            }
        }

        List<ChgErrorCodeListDto> errcdList = queryFactory.select(Projections.fields(ChgErrorCodeListDto.class,
            errorCode.id.as("errcdId"),
            errorCode.errCode.as("errCode"),
            errorCode.errName.as("errName"),
            errorCode.menufCode.as("menufCode"),
            errorCode.regDt.as("regDt"),
            Expressions.stringTemplate("IF({0} = 'DFT', '없음', {1})", errorCode.menufCode, manufCdName.name).as("menufCodeName")))
            .from(errorCode)
            .leftJoin(manufCdName).on(errorCode.menufCode.eq(manufCdName.commonCode))
            .orderBy(errorCode.regDt.desc())
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .select(errorCode.count())
            .from(errorCode)
            .where(builder)
            .fetchOne();

        return new PageImpl<>(errcdList, pageable, totalCount);
    }

    @Override
    public ChgErrorCodeRegDto findErrorCodeOne(Long errcdId) {
        ChgErrorCodeRegDto errcd = queryFactory.select(Projections.fields(ChgErrorCodeRegDto.class,
            errorCode.id.as("errcdId"),
            errorCode.errCode.as("errCode"),
            errorCode.errName.as("errName"),
            errorCode.menufCode.as("menufCode"),
            errorCode.regDt.as("regDt")))
            .from(errorCode)
            .where(errorCode.id.eq(errcdId))
            .fetchOne();

        return errcd;
    }
}
