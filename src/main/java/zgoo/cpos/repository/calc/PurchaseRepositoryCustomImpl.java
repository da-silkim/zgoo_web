package zgoo.cpos.repository.calc;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.calc.QPurchaseInfo;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseListDto;

@RequiredArgsConstructor
@Slf4j
public class PurchaseRepositoryCustomImpl implements PurchaseRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QCsInfo csInfo = QCsInfo.csInfo;
    QPurchaseInfo purchase = QPurchaseInfo.purchaseInfo;
    QCommonCode accountCdName = new QCommonCode("accountCode");
    QCommonCode paymentName = new QCommonCode("paymentMethod");

    @Override
    public Page<PurchaseListDto> findPurchaseWithPagination(Pageable pageable) {
        List<PurchaseListDto> purList = queryFactory.select(Projections.fields(PurchaseListDto.class,
            purchase.id.as("purchaseId"),
            purchase.purchaseDate.as("purchaseDate"),
            purchase.bizNum.as("bizNum"),
            purchase.bizName.as("bizName"),
            purchase.approvalNo.as("approvalNo"),
            purchase.item.as("item"),
            purchase.supplyPrice.as("supplyPrice"),
            purchase.vat.as("vat"),
            purchase.totalAmount.as("totalAmount"),
            csInfo.stationName.as("stationName"),
            accountCdName.name.as("accountCodeName"),
            paymentName.name.as("paymentMethodName")))
            .from(purchase)
            .leftJoin(csInfo).on(purchase.station.eq(csInfo))
            .leftJoin(accountCdName).on(purchase.accountCode.eq(accountCdName.commonCode))
            .leftJoin(paymentName).on(purchase.paymentMethod.eq(paymentName.commonCode))
            .orderBy(purchase.regDt.desc(), purchase.id.desc())
            .where(purchase.delYn.eq("N"))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();


        long totalCount = queryFactory
            .select(purchase.count())
            .from(purchase)
            .fetchOne();

        return new PageImpl<>(purList, pageable, totalCount);
    }

    @Override
    public Page<PurchaseListDto> searchPurchaseWithPagination(String searchOp, String searchContent,
            LocalDate startDate, LocalDate endDate, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(purchase.delYn.eq("N"));

        if (searchOp != null & (searchContent != null && !searchContent.isEmpty())) {
            if (searchOp.equals("approvalNo")) {
                builder.and(purchase.approvalNo.contains(searchContent));
            } else if (searchOp.equals("bizName")) {
                builder.and(purchase.bizName.contains(searchContent));
            }
        }

        if (startDate != null) {
            builder.and(purchase.purchaseDate.goe(startDate));
        }

        if (endDate != null) {
            builder.and(purchase.purchaseDate.loe(endDate));
        }

        List<PurchaseListDto> purList = queryFactory.select(Projections.fields(PurchaseListDto.class,
            purchase.id.as("purchaseId"),
            purchase.purchaseDate.as("purchaseDate"),
            purchase.bizNum.as("bizNum"),
            purchase.bizName.as("bizName"),
            purchase.approvalNo.as("approvalNo"),
            purchase.item.as("item"),
            purchase.supplyPrice.as("supplyPrice"),
            purchase.vat.as("vat"),
            purchase.totalAmount.as("totalAmount"),
            csInfo.stationName.as("stationName"),
            accountCdName.name.as("accountCodeName"),
            paymentName.name.as("paymentMethodName")))
            .from(purchase)
            .leftJoin(csInfo).on(purchase.station.eq(csInfo))
            .leftJoin(accountCdName).on(purchase.accountCode.eq(accountCdName.commonCode))
            .leftJoin(paymentName).on(purchase.paymentMethod.eq(paymentName.commonCode))
            .where(builder)
            .orderBy(purchase.regDt.desc(), purchase.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();


        long totalCount = queryFactory
            .select(purchase.count())
            .from(purchase)
            .leftJoin(csInfo).on(purchase.station.eq(csInfo))
            .leftJoin(accountCdName).on(purchase.accountCode.eq(accountCdName.commonCode))
            .leftJoin(paymentName).on(purchase.paymentMethod.eq(paymentName.commonCode))
            .where(builder)
            .fetchOne();

        return new PageImpl<>(purList, pageable, totalCount);
    }
}
