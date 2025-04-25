package zgoo.cpos.repository.calc;

import java.time.LocalDate;
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
import zgoo.cpos.domain.calc.QPurchaseInfo;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.cs.QCsLandInfo;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseAccountDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseListDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseRegDto;

@RequiredArgsConstructor
@Slf4j
public class PurchaseRepositoryCustomImpl implements PurchaseRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QCsInfo csInfo = QCsInfo.csInfo;
    QCsLandInfo land = QCsLandInfo.csLandInfo;
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
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.charge).as("charge"),
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
            .where(purchase.delYn.eq("N"))
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
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.charge).as("charge"),
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

    @Override
    public Long deletePurchaseOne(Long id) {
        return queryFactory
            .update(purchase)
            .set(purchase.delYn, "Y")
            .where(purchase.id.eq(id))
            .execute();
    }

    @Override
    public List<PurchaseListDto> findAllPurchaseWithoutPagination(String searchOp, String searchContent,
            LocalDate startDate, LocalDate endDate) {
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

        return queryFactory.select(Projections.fields(PurchaseListDto.class,
            purchase.id.as("purchaseId"),
            purchase.purchaseDate.as("purchaseDate"),
            purchase.bizNum.as("bizNum"),
            purchase.bizName.as("bizName"),
            purchase.approvalNo.as("approvalNo"),
            purchase.item.as("item"),
            purchase.supplyPrice.as("supplyPrice"),
            purchase.vat.as("vat"),
            purchase.totalAmount.as("totalAmount"),
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.charge).as("charge"),
            csInfo.stationName.as("stationName"),
            accountCdName.name.as("accountCodeName"),
            paymentName.name.as("paymentMethodName")))
            .from(purchase)
            .leftJoin(csInfo).on(purchase.station.eq(csInfo))
            .leftJoin(accountCdName).on(purchase.accountCode.eq(accountCdName.commonCode))
            .leftJoin(paymentName).on(purchase.paymentMethod.eq(paymentName.commonCode))
            .orderBy(purchase.regDt.desc(), purchase.id.desc())
            .where(builder)
            .fetch();
    }

    @Override
    public PurchaseRegDto findPurchaseOne(Long id) {
        return queryFactory.select(Projections.fields(PurchaseRegDto.class,
            purchase.id.as("purchaseId"),
            purchase.station.id.as("stationId"),
            purchase.approvalNo.as("approvalNo"),
            purchase.accountCode.as("accountCode"),
            purchase.purchaseDate.as("purchaseDate"),
            purchase.bizNum.as("bizNum"),
            purchase.bizName.as("bizName"),
            purchase.item.as("item"),
            purchase.paymentMethod.as("paymentMethod"),
            purchase.unitPrice.as("unitPrice"),
            purchase.amount.as("amount"),
            purchase.supplyPrice.as("supplyPrice"),
            purchase.vat.as("vat"),
            purchase.charge.as("charge"),
            purchase.totalAmount.as("totalAmount")))
            .from(purchase)
            .where(purchase.id.eq(id))
            .fetchOne();
    }

    @Override
    public PurchaseAccountDto searchAccountLand(String stationId) {
        // 토지사용료
        return queryFactory.select(Projections.fields(PurchaseAccountDto.class,
                land.landUseType.as("landUseType"),
                Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", land.landUseFee).as("unitPrice"),
                Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", land.landUseFee).as("supplyPrice"),
                Expressions.numberTemplate(Integer.class, "ROUND(COALESCE({0}, 0) * 0.1)", land.landUseFee).as("vat"),
                Expressions.numberTemplate(Integer.class, "ROUND(COALESCE({0}, 0) * 1.1)", land.landUseFee).as("totalAmount")))
                .from(csInfo)
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .where(csInfo.id.eq(stationId))
                .fetchOne();
    }

    @Override
    public PurchaseAccountDto searchAccountSafety(String stationId) {
        // 안전점검관리비
        return queryFactory.select(Projections.fields(PurchaseAccountDto.class,
                Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", csInfo.safetyManagementFee).as("unitPrice"),
                Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", csInfo.safetyManagementFee).as("supplyPrice"),
                Expressions.numberTemplate(Integer.class, "ROUND(COALESCE({0}, 0) * 0.1)", csInfo.safetyManagementFee).as("vat"),
                Expressions.numberTemplate(Integer.class, "ROUND(COALESCE({0}, 0) * 1.1)", csInfo.safetyManagementFee).as("totalAmount")))
                .from(csInfo)
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .where(csInfo.id.eq(stationId))
                .fetchOne();
    }

}
