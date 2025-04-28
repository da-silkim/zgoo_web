package zgoo.cpos.repository.history;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.cp.QCpModel;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.history.QChargingHist;
import zgoo.cpos.domain.member.QMember;
import zgoo.cpos.dto.history.ChargingHistDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwBaseDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwLineChartBaseDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageBaseDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageLineChartBaseDto;

@RequiredArgsConstructor
@Slf4j
public class ChargingHistRepositoryCustomImpl implements ChargingHistRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QChargingHist hist = QChargingHist.chargingHist;
    QCpInfo cpInfo = QCpInfo.cpInfo;
    QCsInfo csInfo = QCsInfo.csInfo;
    QMember member = QMember.member;
    QCpModel model = QCpModel.cpModel;
    QCommonCode cpTypeName = new QCommonCode("cpType");
    QCommonCode memberTypeName = new QCommonCode("bizType");

    @Override
    public Page<ChargingHistDto> findAllChargingHist(Pageable pageable) {
        List<ChargingHistDto> chargingHistList = queryFactory.select(Projections.fields(ChargingHistDto.class,
                csInfo.company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                csInfo.id.as("stationId"),
                hist.chargerID.as("chargerId"),
                hist.connectorId.as("connectorId"),
                cpTypeName.name.as("cpType"),
                member.name.as("memberName"),
                hist.idTag.as("idTag"),
                memberTypeName.name.as("memberType"),
                hist.startTime.as("chgStartTime"),
                hist.endTime.as("chgEndTime"),
                hist.chargingTime.as("chgTime"),
                hist.stopReason.as("chgEndReason"),
                hist.soc.as("soc"),
                hist.chargeAmount.as("chgAmount"),
                hist.chargePrice.as("chgPrice"),
                hist.unitCost.as("unitCost"),
                hist.preAmount.as("prepayCost"),
                hist.cancelAmount.as("cancelCost"),
                hist.realAmount.as("realCost"),
                hist.approvalNum.as("approvalNum"),
                hist.paymentYn.as("paymentYn")))
                .from(hist)
                .leftJoin(cpInfo).on(hist.chargerID.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .leftJoin(member).on(hist.idTag.eq(member.idTag))
                .leftJoin(memberTypeName).on(member.bizType.eq(memberTypeName.commonCode))
                .orderBy(hist.startTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.info("===ChargingHist Query Result Size: {}", chargingHistList.size());
        if (!chargingHistList.isEmpty()) {
            log.info("===First ChargingHist: {}", chargingHistList.get(0));
        }

        long totalCount = queryFactory.select(hist.count())
                .from(hist)
                .leftJoin(cpInfo).on(hist.chargerID.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .leftJoin(member).on(hist.idTag.eq(member.idTag))
                .leftJoin(memberTypeName).on(member.bizType.eq(memberTypeName.commonCode))
                .fetchOne();

        log.info("===ChargingHist Total Count: {}", totalCount);

        return new PageImpl<>(chargingHistList, pageable, totalCount);
    }

    @Override
    public Page<ChargingHistDto> findChargingHist(Long companyId, String startTimeFrom, String startTimeTo,
            String searchOp, String searchContent, Pageable pageable) {

        log.info(
                "=== >> findChargingHist with search condition: companyId: {}, startTimeFrom: {}, startTimeTo: {}, searchOp: {}, searchContent: {}",
                companyId, startTimeFrom, startTimeTo, searchOp, searchContent);

        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (startTimeFrom != null && !startTimeFrom.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime fromDateTime;
            if (startTimeFrom.length() <= 10) { // yyyy-MM-dd 형식인 경우
                fromDateTime = LocalDateTime.parse(startTimeFrom + " 00:00:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                fromDateTime = LocalDateTime.parse(startTimeFrom,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            builder.and(hist.startTime.after(fromDateTime));
        }

        if (startTimeTo != null && !startTimeTo.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime toDateTime;
            if (startTimeTo.length() <= 10) { // yyyy-MM-dd 형식인 경우
                toDateTime = LocalDateTime.parse(startTimeTo + " 23:59:59",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                toDateTime = LocalDateTime.parse(startTimeTo,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            builder.and(hist.startTime.before(toDateTime));
        }

        if (searchOp != null && !searchOp.isEmpty()) {
            switch (searchOp.toLowerCase()) {
                case "stationname":
                    builder.and(csInfo.stationName.containsIgnoreCase(searchContent));
                    break;
                case "stationid":
                    builder.and(csInfo.id.containsIgnoreCase(searchContent));
                    break;
                case "chargerid":
                    builder.and(hist.chargerID.containsIgnoreCase(searchContent));
                    break;
                case "idtag":
                    builder.and(hist.idTag.containsIgnoreCase(searchContent));
                    break;
                default:
                    break;
            }
        }

        List<ChargingHistDto> chargingHistList = queryFactory.select(Projections.fields(ChargingHistDto.class,
                csInfo.company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                csInfo.id.as("stationId"),
                hist.chargerID.as("chargerId"),
                hist.connectorId.as("connectorId"),
                cpTypeName.name.as("cpType"),
                member.name.as("memberName"),
                hist.idTag.as("idTag"),
                memberTypeName.name.as("memberType"),
                hist.startTime.as("chgStartTime"),
                hist.endTime.as("chgEndTime"),
                hist.chargingTime.as("chgTime"),
                hist.stopReason.as("chgEndReason"),
                hist.soc.as("soc"),
                hist.chargeAmount.as("chgAmount"),
                hist.chargePrice.as("chgPrice"),
                hist.unitCost.as("unitCost"),
                hist.preAmount.as("prepayCost"),
                hist.cancelAmount.as("cancelCost"),
                hist.realAmount.as("realCost"),
                hist.approvalNum.as("approvalNum"),
                hist.paymentYn.as("paymentYn")))
                .from(hist)
                .leftJoin(cpInfo).on(hist.chargerID.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .leftJoin(member).on(hist.idTag.eq(member.idTag))
                .leftJoin(memberTypeName).on(member.bizType.eq(memberTypeName.commonCode))
                .where(builder)
                .orderBy(hist.startTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.info("===ChargingHist Query Result Size: {}", chargingHistList.size());
        if (!chargingHistList.isEmpty()) {
            log.info("===First ChargingHist: {}", chargingHistList.get(0));
        }

        long totalCount = queryFactory.select(hist.count())
                .from(hist)
                .leftJoin(cpInfo).on(hist.chargerID.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .leftJoin(member).on(hist.idTag.eq(member.idTag))
                .leftJoin(memberTypeName).on(member.bizType.eq(memberTypeName.commonCode))
                .where(builder)
                .fetchOne();

        log.info("===ChargingHist Total Count: {}", totalCount);

        return new PageImpl<>(chargingHistList, pageable, totalCount);
    }

    @Override
    public List<ChargingHistDto> findAllChargingHistListWithoutPagination(Long companyId, String startTimeFrom,
            String startTimeTo, String searchOp, String searchContent) {
        log.info(
                "=== >> exceldownload: companyId: {}, startTimeFrom: {}, startTimeTo: {}, searchOp: {}, searchContent: {}",
                companyId, startTimeFrom, startTimeTo, searchOp, searchContent);

        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (startTimeFrom != null && !startTimeFrom.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime fromDateTime;
            if (startTimeFrom.length() <= 10) { // yyyy-MM-dd 형식인 경우
                fromDateTime = LocalDateTime.parse(startTimeFrom + " 00:00:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                fromDateTime = LocalDateTime.parse(startTimeFrom,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            builder.and(hist.startTime.after(fromDateTime));
        }

        if (startTimeTo != null && !startTimeTo.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime toDateTime;
            if (startTimeTo.length() <= 10) { // yyyy-MM-dd 형식인 경우
                toDateTime = LocalDateTime.parse(startTimeTo + " 23:59:59",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                toDateTime = LocalDateTime.parse(startTimeTo,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            builder.and(hist.startTime.before(toDateTime));
        }

        if (searchOp != null && !searchOp.isEmpty()) {
            switch (searchOp.toLowerCase()) {
                case "stationname":
                    builder.and(csInfo.stationName.containsIgnoreCase(searchContent));
                    break;
                case "stationid":
                    builder.and(csInfo.id.containsIgnoreCase(searchContent));
                    break;
                case "chargerid":
                    builder.and(hist.chargerID.containsIgnoreCase(searchContent));
                    break;
                case "idtag":
                    builder.and(hist.idTag.containsIgnoreCase(searchContent));
                    break;
                default:
                    break;
            }
        }

        return queryFactory.select(Projections.fields(ChargingHistDto.class,
                csInfo.company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                csInfo.id.as("stationId"),
                hist.chargerID.as("chargerId"),
                hist.connectorId.as("connectorId"),
                cpTypeName.name.as("cpType"),
                member.name.as("memberName"),
                hist.idTag.as("idTag"),
                memberTypeName.name.as("memberType"),
                hist.startTime.as("chgStartTime"),
                hist.endTime.as("chgEndTime"),
                hist.chargingTime.as("chgTime"),
                hist.stopReason.as("chgEndReason"),
                hist.soc.as("soc"),
                hist.chargeAmount.as("chgAmount"),
                hist.chargePrice.as("chgPrice"),
                hist.unitCost.as("unitCost"),
                hist.preAmount.as("prepayCost"),
                hist.cancelAmount.as("cancelCost"),
                hist.realAmount.as("realCost"),
                hist.approvalNum.as("approvalNum"),
                hist.paymentYn.as("paymentYn")))
                .from(hist)
                .leftJoin(cpInfo).on(hist.chargerID.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .leftJoin(member).on(hist.idTag.eq(member.idTag))
                .leftJoin(memberTypeName).on(member.bizType.eq(memberTypeName.commonCode))
                .where(builder)
                .orderBy(hist.startTime.desc())
                .fetch();
    }

    @Override
    public TotalkwBaseDto searchYearChargeAmount(Long companyId, String searchOp, String searchContent, Integer year) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (searchOp != null && (searchContent != null && !searchContent.isEmpty())) {
            switch (searchOp) {
                case "stationId" -> builder.and(csInfo.id.contains(searchContent));
                case "stationName" -> {
                    builder.and(csInfo.stationName.contains(searchContent));
                }
                case "chargerId" -> builder.and(cpInfo.id.contains(searchContent));
                default -> {
                }
            }
        }

        LocalDateTime startOfYear = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime endOfYear = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);
        builder.and(hist.startTime.between(startOfYear, endOfYear));

        TotalkwBaseDto dto = queryFactory.select(Projections.fields(TotalkwBaseDto.class,
                Expressions.stringTemplate("MAX(CASE WHEN {0} = 'SPEEDFAST' THEN {0} END)", model.cpType)
                        .as("speedLow"),
                Expressions.numberTemplate(BigDecimal.class,
                        "SUM(CASE WHEN {0} = 'SPEEDFAST' THEN {1} ELSE 0 END)", model.cpType, hist.chargeAmount)
                        .as("lowChgAmount"),
                Expressions.stringTemplate("MAX(CASE WHEN {0} = 'SPEEDLOW' THEN {0} END)", model.cpType)
                        .as("speedFast"),
                Expressions.numberTemplate(BigDecimal.class,
                        "SUM(CASE WHEN {0} = 'SPEEDLOW' THEN {1} ELSE 0 END)", model.cpType, hist.chargeAmount)
                        .as("fastChgAmount"),
                Expressions.stringTemplate("MAX(CASE WHEN {0} = 'SPEEDDESPN' THEN {0} END)", model.cpType)
                        .as("speedDespn"),
                Expressions.numberTemplate(BigDecimal.class,
                        "SUM(CASE WHEN {0} = 'SPEEDDESPN' THEN {1} ELSE 0 END)", model.cpType, hist.chargeAmount)
                        .as("despnChgAmount"),
                Expressions.numberTemplate(BigDecimal.class,
                        "SUM((CASE WHEN {0} = 'SPEEDFAST' THEN {1} ELSE 0 END) + (CASE WHEN {0} = 'SPEEDLOW' THEN {1} ELSE 0 END) + (CASE WHEN {0} = 'SPEEDDESPN' THEN {1} ELSE 0 END))",
                        model.cpType, hist.chargeAmount).as("totalkw")))
                .from(hist)
                .leftJoin(cpInfo).on(hist.chargerID.eq(cpInfo.id))
                .leftJoin(csInfo).on(csInfo.eq(cpInfo.stationId))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .where(builder)
                .fetchOne();
        dto.setYear(year);

        return dto;
    }

    @Override
    public List<TotalkwLineChartBaseDto> searchMonthlyChargeAmount(Long companyId, String searchOp,
            String searchContent, Integer year, String cpType) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (searchOp != null && (searchContent != null && !searchContent.isEmpty())) {
            switch (searchOp) {
                case "stationId" -> builder.and(csInfo.id.contains(searchContent));
                case "stationName" -> {
                    builder.and(csInfo.stationName.contains(searchContent));
                }
                case "chargerId" -> builder.and(cpInfo.id.contains(searchContent));
                default -> {
                }
            }
        }

        builder.and(model.cpType.eq(cpType));
        builder.and(Expressions.numberTemplate(Integer.class, "YEAR({0})", hist.startTime).eq(year));

        List<TotalkwLineChartBaseDto> monthlyResults = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> TotalkwLineChartBaseDto.builder()
                        .month(month)
                        .year(year)
                        .totalkw(BigDecimal.ZERO)
                        .build())
                .collect(Collectors.toList());

        // 월별 충전량 조회
        List<Tuple> results = queryFactory
                .select(
                        Expressions.numberTemplate(Integer.class, "MONTH({0})", hist.startTime).as("month"),
                        hist.chargeAmount.sum())
                .from(hist)
                .leftJoin(cpInfo).on(hist.chargerID.eq(cpInfo.id))
                .leftJoin(csInfo).on(csInfo.eq(cpInfo.stationId))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .where(builder)
                .groupBy(Expressions.numberTemplate(Integer.class, "MONTH({0})", hist.startTime))
                .fetch();

        for (Tuple tuple : results) {
            Integer month = tuple.get(0, Integer.class);
            BigDecimal amount = tuple.get(1, BigDecimal.class);
            monthlyResults.get(month - 1).setTotalkw(amount);
        }

        return monthlyResults;
    }

    @Override
    public UsageBaseDto searchYearUsage(Long companyId, String searchOp, String searchContent, Integer year) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (searchOp != null && (searchContent != null && !searchContent.isEmpty())) {
            switch (searchOp) {
                case "stationId" -> builder.and(csInfo.id.contains(searchContent));
                case "stationName" -> {
                    builder.and(csInfo.stationName.contains(searchContent));
                }
                case "chargerId" -> builder.and(cpInfo.id.contains(searchContent));
                default -> {
                }
            }
        }

        LocalDateTime startOfYear = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime endOfYear = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);
        builder.and(hist.startTime.between(startOfYear, endOfYear));

        UsageBaseDto dto = queryFactory.select(Projections.fields(UsageBaseDto.class,
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'SPEEDFAST' THEN 1 END)", model.cpType)
                        .as("fastCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'SPEEDLOW' THEN 1 END)", model.cpType)
                        .as("lowCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'SPEEDDESPN' THEN 1 END)", model.cpType)
                        .as("despnCount"),
                Expressions.numberTemplate(Long.class, "COUNT(*)").as("totalUsage")))
                .from(hist)
                .leftJoin(cpInfo).on(hist.chargerID.eq(cpInfo.id))
                .leftJoin(csInfo).on(csInfo.eq(cpInfo.stationId))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .where(builder)
                .fetchOne();
        dto.setYear(year);

        return dto;
    }

    @Override
    public List<UsageLineChartBaseDto> searchMonthlyUsage(Long companyId, String searchOp, String searchContent,
            Integer year, String cpType) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (searchOp != null && (searchContent != null && !searchContent.isEmpty())) {
            switch (searchOp) {
                case "stationId" -> builder.and(csInfo.id.contains(searchContent));
                case "stationName" -> {
                    builder.and(csInfo.stationName.contains(searchContent));
                }
                case "chargerId" -> builder.and(cpInfo.id.contains(searchContent));
                default -> {
                }
            }
        }

        builder.and(model.cpType.eq(cpType));
        builder.and(Expressions.numberTemplate(Integer.class, "YEAR({0})", hist.startTime).eq(year));

        List<UsageLineChartBaseDto> monthlyResults = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> UsageLineChartBaseDto.builder()
                        .month(month)
                        .year(year)
                        .totalUsage(0L)
                        .build())
                .collect(Collectors.toList());

        List<Tuple> results = queryFactory
                .select(
                        Expressions.numberTemplate(Integer.class, "MONTH({0})", hist.startTime).as("month"),
                        hist.id.count())
                .from(hist)
                .leftJoin(cpInfo).on(hist.chargerID.eq(cpInfo.id))
                .leftJoin(csInfo).on(csInfo.eq(cpInfo.stationId))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .where(builder)
                .groupBy(Expressions.numberTemplate(Integer.class, "MONTH({0})", hist.startTime))
                .fetch();

        for (Tuple tuple : results) {
            Integer month = tuple.get(0, Integer.class);
            Long count = tuple.get(1, Long.class);
            monthlyResults.get(month - 1).setTotalUsage(count);
        }

        return monthlyResults;
    }
}