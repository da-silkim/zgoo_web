package zgoo.cpos.repository.biz;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.biz.QBizInfo;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoListDto;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;

@Slf4j
@RequiredArgsConstructor
public class BizRepositoryCustomImpl implements BizRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QBizInfo biz = QBizInfo.bizInfo;
    QCommonCode fnCodeName = new QCommonCode("fnCode");

    @Override
    public Page<BizInfoListDto> findBizWithPagination(Pageable pageable) {
        List<BizInfoListDto> bizList = queryFactory.select(Projections.fields(BizInfoListDto.class,
            biz.id.as("bizId"),
            biz.bizNo.as("bizNo"),
            biz.bizName.as("bizName"),
            biz.tid.as("tid"),
            biz.cardNum.as("cardNum"),
            biz.fnCode.as("fnCode"),
            biz.regDt.as("regDt"),
            fnCodeName.name.as("fnCodeName")))
            .from(biz)
            .leftJoin(fnCodeName).on(biz.fnCode.eq(fnCodeName.commonCode))
            .orderBy(biz.regDt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .select(biz.count())
            .from(biz)
            .fetchOne();

         return new PageImpl<>(bizList, pageable, totalCount);
    }

    @Override
    public Page<BizInfoListDto> searchBizWithPagination(String searchOp, String searchContent,
            Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchOp != null && (searchContent != null && !searchContent.isEmpty())) {
            if (searchOp.equals("bizName")) {
                builder.and(biz.bizName.contains(searchContent));
            } else if (searchOp.equals("bizNo")) {
                builder.and(biz.bizNo.contains(searchContent));
            }
        }

        List<BizInfoListDto> bizList = queryFactory.select(Projections.fields(BizInfoListDto.class,
            biz.id.as("bizId"),
            biz.bizNo.as("bizNo"),
            biz.bizName.as("bizName"),
            biz.tid.as("tid"),
            biz.cardNum.as("cardNum"),
            biz.fnCode.as("fnCode"),
            biz.regDt.as("regDt"),
            fnCodeName.name.as("fnCodeName")))
            .from(biz)
            .leftJoin(fnCodeName).on(biz.fnCode.eq(fnCodeName.commonCode))
            .orderBy(biz.regDt.desc())
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .select(biz.count())
            .from(biz)
            .where(builder)
            .fetchOne();

        return new PageImpl<>(bizList, pageable, totalCount);
    }

    @Override
    public String maskCardNum(String cardNum) {
        if (cardNum == null || cardNum.length() != 16) {
            return cardNum;
        }
        return cardNum.replaceAll("(\\d{4})(\\d{2})(\\d{6})(\\d{4})", "$1-$2**-****-$4");
    }

    @Override
    public BizInfoRegDto findBizOne(Long bizId) {
        BizInfoRegDto bizDto = queryFactory.select(Projections.fields(BizInfoRegDto.class,
            biz.id.as("bizId"),
            biz.bizNo.as("bizNo"),
            biz.bizName.as("bizName"),
            biz.tid.as("tid"),
            biz.cardNum.as("cardNum"),
            biz.fnCode.as("fnCode")))
            .from(biz)
            .where(biz.id.eq(bizId))
            .fetchOne();

        return bizDto;
    }

    @Override
    public BizInfoRegDto findBizOneCustom(Long bizId) {
        BizInfoRegDto bizDto = queryFactory.select(Projections.bean(BizInfoRegDto.class,
            biz.id.as("bizId"),
            biz.bizNo.as("bizNo"),
            biz.bizName.as("bizName"),
            biz.tid.as("tid"),
            biz.cardNum.as("cardNum")))
            .from(biz)
            .where(biz.id.eq(bizId))
            .orderBy(biz.regDt.desc())
            .fetchOne();

        if (bizDto.getTid() != null && !bizDto.getTid().isEmpty()) {
            bizDto.setTidYn("Y");
        } else {
            bizDto.setTidYn("N");
        }
        if (bizDto.getCardNum() != null && !bizDto.getCardNum().isEmpty()) {
            bizDto.setCardYn("Y");
        } else {
            bizDto.setCardYn("N");
        }

        return bizDto;
    }

    @Override
    public List<BizInfoRegDto> findBizByBizName(String bizName) {
        List<BizInfoRegDto> bizList = queryFactory.select(Projections.fields(BizInfoRegDto.class,
            biz.id.as("bizId"),
            biz.bizNo.as("bizNo"),
            biz.bizName.as("bizName"),
            biz.tid.as("tid"),
            biz.cardNum.as("cardNum"),
            biz.fnCode.as("fnCode"),
            biz.regDt.as("regDt")))
            .from(biz)
            .where(biz.bizName.contains(bizName))
            .orderBy(biz.regDt.desc())
            .fetch();

        return bizList;
    }
}
