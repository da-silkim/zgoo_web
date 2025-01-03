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
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoListDto;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;

@Slf4j
@RequiredArgsConstructor
public class BizRepositoryCustomImpl implements BizRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QBizInfo biz = QBizInfo.bizInfo;
    QCompany company = QCompany.company;
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
            company.companyName.as("companyName"),
            fnCodeName.name.as("fnCodeName")))
            .from(biz)
            .leftJoin(company).on(biz.company.eq(company))
            .leftJoin(fnCodeName).on(biz.fnCode.eq(fnCodeName.commonCode))
            .orderBy(biz.regDt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        for (BizInfoListDto bizDto : bizList) {
            bizDto.setCardNum(maskCardNum(bizDto.getCardNum()));
        }

        long totalCount = queryFactory
            .select(biz.count())
            .from(biz)
            .fetchOne();

         return new PageImpl<>(bizList, pageable, totalCount);
    }

    @Override
    public Page<BizInfoListDto> searchBizWithPagination(Long companyId, String searchOp, String searchContent,
            Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(biz.company.id.eq(companyId));
        }

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
            company.companyName.as("companyName"),
            fnCodeName.name.as("fnCodeName")))
            .from(biz)
            .leftJoin(company).on(biz.company.eq(company))
            .leftJoin(fnCodeName).on(biz.fnCode.eq(fnCodeName.commonCode))
            .orderBy(biz.regDt.desc())
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        for (BizInfoListDto bizDto : bizList) {
            bizDto.setCardNum(maskCardNum(bizDto.getCardNum()));
        }

        long totalCount = queryFactory
            .select(biz.count())
            .from(biz)
            .where(builder)
            .fetchOne();

        return new PageImpl<>(bizList, pageable, totalCount);
    }

    private String maskCardNum(String cardNum) {
        if (cardNum == null || cardNum.length() != 16) {
            return cardNum;
        }
        return cardNum.replaceAll("(\\d{4})(\\d{2})(\\d{6})(\\d{4})", "$1-$2**-****-$4");
    }

    @Override
    public BizInfoRegDto findBizOne(Long bizId) {
        BizInfoRegDto bizDto = queryFactory.select(Projections.fields(BizInfoRegDto.class,
            biz.id.as("bizId"),
            biz.company.id.as("companyId"),
            biz.bizNo.as("bizNo"),
            biz.bizName.as("bizName"),
            biz.tid.as("tid"),
            biz.cardNum.as("cardNum"),
            biz.fnCode.as("fnCode")))
            .from(biz)
            .where(biz.id.eq(bizId))
            .fetchOne();

        if (bizDto.getCardNum() != null && bizDto.getCardNum().length() == 16) {
            bizDto.setCardNum1(bizDto.getCardNum().substring(0, 4));
            bizDto.setCardNum2(bizDto.getCardNum().substring(4, 8));
            bizDto.setCardNum3(bizDto.getCardNum().substring(8, 12));
            bizDto.setCardNum4(bizDto.getCardNum().substring(12, 16));
        }

        return bizDto;
    }
}
