package zgoo.cpos.repository.calc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.calc.QPurchaseInfo;
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.cs.QCsLandInfo;
import zgoo.cpos.domain.payment.QChargerPaymentInfo;
import zgoo.cpos.domain.payment.QPgTrxRecon;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseAccountDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseDetailDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseListDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseRegDto;
import zgoo.cpos.dto.statistics.PurchaseSalesDto.PurchaseSalesLineChartBaseDto;

@RequiredArgsConstructor
@Slf4j
public class PurchaseRepositoryCustomImpl implements PurchaseRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QCsInfo csInfo = QCsInfo.csInfo;
    QCpInfo cpInfo = QCpInfo.cpInfo;
    QCsLandInfo land = QCsLandInfo.csLandInfo;
    QPurchaseInfo purchase = QPurchaseInfo.purchaseInfo;
    QCompany company = QCompany.company;
    QChargerPaymentInfo chgPaymentInfo = QChargerPaymentInfo.chargerPaymentInfo;
    QPgTrxRecon pgTrxRecon = QPgTrxRecon.pgTrxRecon;
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
            purchase.unitPrice.as("unitPrice"),
            purchase.supplyPrice.as("supplyPrice"),
            purchase.vat.as("vat"),
            purchase.charge.as("charge"),
            purchase.surcharge.as("surcharge"),
            purchase.cutoffAmount.as("cutoffAmount"),
            purchase.unpaidAmount.as("unpaidAmount"),
            purchase.totalAmount.as("totalAmount"),
            purchase.power.as("power"),
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
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.charge).as("charge"),
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.surcharge).as("surcharge"),
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.cutoffAmount).as("cutoffAmount"),
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.unpaidAmount).as("unpaidAmount"),
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.power).as("power"),
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
    public PurchaseAccountDto searchAccountLandTypeRate(String stationId) {
        // 토지사용료(Type: RATE)
        Integer rate = queryFactory
                .select(land.landUseFee)
                .from(csInfo)
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .where(csInfo.id.eq(stationId))
                .fetchOne();

        LocalDateTime startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);

        // 관제 결제 데이터의 TID 목록 추출
        List<String> tids = queryFactory
                .select(chgPaymentInfo.preTid)
                .from(chgPaymentInfo)
                .leftJoin(cpInfo).on(chgPaymentInfo.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .where(csInfo.id.eq(stationId)
                    .and(chgPaymentInfo.timestamp.between(startDate, endDate)))
                .fetch();

        /* 
         * 결제금액 = 승인금액 (상태코드 0) - 취소금액 (상태코드 2)
         * 상태코드 1은 승인금액과 취소금액 둘 다 변영되므로 제외
         */
        // 승인금액
        BigDecimal approved = queryFactory
                .select(pgTrxRecon.goodsAmt.sum())
                .from(pgTrxRecon)
                .where(pgTrxRecon.tid.in(tids).and(pgTrxRecon.stateCd.in("0")))
                .fetchOne();

        if (approved == null)
            approved = BigDecimal.ZERO;

        
        // 취소금액
        BigDecimal canceled = queryFactory
                .select(pgTrxRecon.goodsAmt.sum())
                .from(pgTrxRecon)
                .where(pgTrxRecon.tid.in(tids).and(pgTrxRecon.stateCd.in("2"))
                    .or(pgTrxRecon.otid.in(tids).and(pgTrxRecon.stateCd.eq("2"))))
                .fetchOne();

        if (canceled == null)
            canceled = BigDecimal.ZERO;

        BigDecimal totalPrice = approved.subtract(canceled);

        /* 
         * 단가, 공급가액
         * totalPrice * rate * 0.01
         */
        BigDecimal percentage = totalPrice.multiply(BigDecimal.valueOf(rate)).divide(BigDecimal.valueOf(100));

        /* 
         * 부가세
         * vat = totalPrice * 0.1
         */
        BigDecimal vat = percentage.multiply(BigDecimal.valueOf(10).divide(BigDecimal.valueOf(100)));

        PurchaseAccountDto result = new PurchaseAccountDto();
        result.setUnitPrice(percentage.intValue());
        result.setSupplyPrice(percentage.intValue());
        result.setVat(vat.intValue());
        result.setTotalAmount(percentage.intValue() + vat.intValue());
        return result;
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

    @Override
    public PurchaseDetailDto findPurchaseDetailOne(Long id) {
        return queryFactory.select(Projections.fields(PurchaseDetailDto.class,
            purchase.accountCode.as("accountCode"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", purchase.item).as("item"),
            purchase.bizName.as("bizName"),
            purchase.bizNum.as("bizNum"),
            purchase.station.id.as("stationId"),
            purchase.purchaseDate.as("purchaseDate"),
            purchase.paymentMethod.as("paymentMethod"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", purchase.approvalNo).as("approvalNo"),
            purchase.unitPrice.as("unitPrice"),
            purchase.amount.as("amount"),
            purchase.supplyPrice.as("supplyPrice"),
            purchase.vat.as("vat"),
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.charge).as("charge"),
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.surcharge).as("surcharge"),
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.cutoffAmount).as("cutoffAmount"),
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.unpaidAmount).as("unpaidAmount"),
            Expressions.numberTemplate(Integer.class, "COALESCE({0}, 0)", purchase.power).as("power"),
            purchase.totalAmount.as("totalAmount"),
            accountCdName.name.as("accountCodeName"),
            paymentName.name.as("paymentMethodName")))
            .from(purchase)
            .leftJoin(accountCdName).on(purchase.accountCode.eq(accountCdName.commonCode))
            .leftJoin(paymentName).on(purchase.paymentMethod.eq(paymentName.commonCode))
            .where(purchase.id.eq(id))
            .fetchOne();
    }

    @Override
    public Integer findTotalAmountByYear(Long companyId, String searchOp, String searchContent, Integer year) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if ((searchOp != null && !searchOp.isEmpty()) && (searchContent != null && !searchContent.isEmpty())) {
            switch (searchOp) {
                case "stationId" -> builder.and(csInfo.id.contains(searchContent));
                case "stationName" -> {
                    builder.and(csInfo.stationName.contains(searchContent));
                }
                default -> {
                }
            }
        }

        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        builder.and(purchase.purchaseDate.between(startOfYear, endOfYear));

        return queryFactory
            .select(purchase.totalAmount.sum())
            .from(purchase)
            .leftJoin(csInfo).on(csInfo.eq(purchase.station))
            .where(builder)
            .fetchOne();
    }

    @Override
    public List<PurchaseSalesLineChartBaseDto> searchMonthlyTotalAmount(Long companyId, String searchOp, String searchContent,
            Integer year) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if ((searchOp != null && !searchOp.isEmpty()) && (searchContent != null && !searchContent.isEmpty())) {
            switch (searchOp) {
                case "stationId" -> builder.and(csInfo.id.contains(searchContent));
                case "stationName" -> {
                    builder.and(csInfo.stationName.contains(searchContent));
                }
                default -> {
                }
            }
        }

        builder.and(Expressions.numberTemplate(Integer.class, "YEAR({0})", purchase.purchaseDate).eq(year));

        List<PurchaseSalesLineChartBaseDto> monthlyResults = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> PurchaseSalesLineChartBaseDto.builder()
                        .month(month)
                        .year(year)
                        .totalPrice(BigDecimal.ZERO)
                        .build())
                .collect(Collectors.toList());
        
        List<Tuple> results = queryFactory
                .select(
                    Expressions.numberTemplate(Integer.class, "MONTH({0})", purchase.purchaseDate).as("month"),
                    purchase.totalAmount.sum())
                .from(purchase)
                .leftJoin(csInfo).on(csInfo.eq(purchase.station))
                .where(builder)
                .groupBy(Expressions.numberTemplate(Integer.class, "MONTH({0})", purchase.purchaseDate))
                .fetch();

        for (Tuple tuple : results) {
            Integer month = tuple.get(0, Integer.class);
            Integer amount = tuple.get(1, Integer.class);
            BigDecimal total = amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;
            monthlyResults.get(month - 1).setTotalPrice(total);
        }

        return monthlyResults;
    }
}
