package zgoo.cpos.repository.history;

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
import zgoo.cpos.domain.code.QChgErrorCode;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.cp.QCpModel;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.history.QErrorHist;
import zgoo.cpos.dto.history.ErrorHistDto;
import zgoo.cpos.dto.statistics.ErrorDto.ErrorBaseDto;
import zgoo.cpos.dto.statistics.ErrorDto.ErrorLineChartBaseDto;
import zgoo.cpos.util.QueryUtils;

@RequiredArgsConstructor
@Slf4j
public class ErrorHistRepositoryCustomImpl implements ErrorHistRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ErrorHistDto> findAllErrorHist(Pageable pageable, String levelPath, boolean isSuperAdmin) {

        QErrorHist errorHist = QErrorHist.errorHist;
        QCompany company = QCompany.company;
        QCpInfo cpInfo = QCpInfo.cpInfo;
        QCpModel model = QCpModel.cpModel;
        QChgErrorCode errcd = QChgErrorCode.chgErrorCode;
        QCsInfo csInfo = QCsInfo.csInfo;
        QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
        QCommonCode manufCdName = new QCommonCode("manfName");

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<ErrorHistDto> errorHistList = queryFactory.select(Projections.fields(ErrorHistDto.class,
                csInfo.company.companyName.as("companyName"),
                manufCdName.name.as("manfName"),
                csInfo.stationName.as("stationName"),
                errorHist.chargerId.as("chargerId"),
                errorHist.connectorId.as("connectorId"),
                errorHist.vendorErrorCode.as("errcd"),
                errorHist.errorName.as("errName"),
                errorHist.occurDate.as("occurDateTime")))
                .from(errorHist)
                .leftJoin(cpInfo).on(errorHist.chargerId.eq(cpInfo.id))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(errcd).on(model.manufCd.eq(errcd.menufCode))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .where(builder)
                .orderBy(errorHist.occurDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory.select(errorHist.count())
                .from(errorHist)
                .leftJoin(cpInfo).on(errorHist.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        log.info("===ErrorHist Total Count: {}", totalCount);

        return new PageImpl<>(errorHistList, pageable, totalCount);
    }

    @Override
    public Page<ErrorHistDto> findErrorHist(Long companyId, String manfCode, String startTimeFrom, String startTimeTo,
            String searchOp,
            String searchContent, Pageable pageable, String levelPath, boolean isSuperAdmin) {

        QErrorHist errorHist = QErrorHist.errorHist;
        QCompany company = QCompany.company;
        QCpInfo cpInfo = QCpInfo.cpInfo;
        QCpModel model = QCpModel.cpModel;
        QChgErrorCode errcd = QChgErrorCode.chgErrorCode;
        QCsInfo csInfo = QCsInfo.csInfo;
        QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
        QCommonCode manufCdName = new QCommonCode("manfName");

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (manfCode != null && !manfCode.isEmpty()) {
            builder.and(model.manufCd.eq(manfCode));
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
            builder.and(errorHist.occurDate.after(fromDateTime));
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
            builder.and(errorHist.occurDate.before(toDateTime));
        }

        if (searchOp != null && !searchOp.isEmpty()) {
            switch (searchOp.toLowerCase()) {
                case "stationname":
                    builder.and(csInfo.stationName.containsIgnoreCase(searchContent));
                    break;
                case "chargerid":
                    builder.and(errorHist.chargerId.containsIgnoreCase(searchContent));
                    break;
                case "errcd":
                    builder.and(errorHist.vendorErrorCode.containsIgnoreCase(searchContent));
                    break;
                default:
                    break;
            }
        }

        List<ErrorHistDto> errorHistList = queryFactory.select(Projections.fields(ErrorHistDto.class,
                csInfo.company.companyName.as("companyName"),
                manufCdName.name.as("manfName"),
                csInfo.stationName.as("stationName"),
                errorHist.chargerId.as("chargerId"),
                errorHist.connectorId.as("connectorId"),
                errorHist.vendorErrorCode.as("errcd"),
                errorHist.errorName.as("errName"),
                errorHist.occurDate.as("occurDateTime")))
                .from(errorHist)
                .leftJoin(cpInfo).on(errorHist.chargerId.eq(cpInfo.id))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(errcd).on(model.manufCd.eq(errcd.menufCode))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .where(builder)
                .orderBy(errorHist.occurDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory.select(errorHist.count())
                .from(errorHist)
                .leftJoin(cpInfo).on(errorHist.chargerId.eq(cpInfo.id))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        log.info("===ErrorHist Total Count: {}", totalCount);

        return new PageImpl<>(errorHistList, pageable, totalCount);

    }

    @Override
    public List<ErrorHistDto> findLatestErrorHist(String levelPath, boolean isSuperAdmin) {
        QErrorHist errorHist = QErrorHist.errorHist;
        QCompany company = QCompany.company;
        QCpInfo cpInfo = QCpInfo.cpInfo;
        QCpModel model = QCpModel.cpModel;
        QCsInfo csInfo = QCsInfo.csInfo;
        QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<ErrorHistDto> errorHistList = queryFactory.select(Projections.fields(ErrorHistDto.class,
                csInfo.stationName.as("stationName"),
                errorHist.chargerId.as("chargerId"),
                errorHist.connectorId.as("connectorId"),
                errorHist.vendorErrorCode.as("errcd"),
                errorHist.errorName.as("errName"),
                errorHist.occurDate.as("occurDateTime")))
                .from(errorHist)
                .leftJoin(cpInfo).on(errorHist.chargerId.eq(cpInfo.id))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .orderBy(errorHist.occurDate.desc())
                .limit(4)
                .fetch();
        return errorHistList;
    }

    @Override
    public ErrorBaseDto findTotalErrorHistByYear(Long companyId, String searchOp, String searchContent, Integer year,
            String levelPath, boolean isSuperAdmin) {
        QErrorHist errorHist = QErrorHist.errorHist;
        QCompany company = QCompany.company;
        QCpInfo cpInfo = QCpInfo.cpInfo;
        QCpModel model = QCpModel.cpModel;
        QCsInfo csInfo = QCsInfo.csInfo;
        QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if ((searchOp != null && !searchOp.isEmpty()) && (searchContent != null && !searchContent.isEmpty())) {
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
        builder.and(errorHist.occurDate.between(startOfYear, endOfYear));

        ErrorBaseDto dto = queryFactory.select(Projections.fields(ErrorBaseDto.class,
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'SPEEDFAST' THEN 1 END)", model.cpType)
                    .as("fastCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'SPEEDLOW' THEN 1 END)", model.cpType)
                    .as("lowCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'SPEEDDESPN' THEN 1 END)", model.cpType)
                    .as("despnCount"),
                Expressions.numberTemplate(Long.class, "COUNT(*)").as("total")))
                .from(errorHist)
                .leftJoin(cpInfo).on(errorHist.chargerId.eq(cpInfo.id))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();
        if(dto != null) dto.setYear(year);

        return dto;
    }

    @Override
    public List<ErrorLineChartBaseDto> searchMonthlyTotalErrorHist(Long companyId, String searchOp, String searchContent,
            Integer year, String cpType, String levelPath, boolean isSuperAdmin) {
        QErrorHist errorHist = QErrorHist.errorHist;
        QCompany company = QCompany.company;
        QCpInfo cpInfo = QCpInfo.cpInfo;
        QCpModel model = QCpModel.cpModel;
        QCsInfo csInfo = QCsInfo.csInfo;
        QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if ((searchOp != null && !searchOp.isEmpty()) && (searchContent != null && !searchContent.isEmpty())) {
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
        builder.and(Expressions.numberTemplate(Integer.class, "YEAR({0})", errorHist.occurDate).eq(year));

        List<ErrorLineChartBaseDto> monthlyResults = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> ErrorLineChartBaseDto.builder()
                    .month(month)
                    .total(0L)
                    .build())
                .collect(Collectors.toList());

        List<Tuple> results = queryFactory
                .select(
                    Expressions.numberTemplate(Integer.class, "MONTH({0})", errorHist.occurDate).as("month"),
                    errorHist.vendorErrorCode.count())
                .from(errorHist)
                .leftJoin(cpInfo).on(errorHist.chargerId.eq(cpInfo.id))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .groupBy(Expressions.numberTemplate(Integer.class, "MONTH({0})", errorHist.occurDate))
                .fetch();

        for (Tuple tuple : results) {
            Integer month = tuple.get(0, Integer.class);
            Long count = tuple.get(1, Long.class);
            monthlyResults.get(month - 1).setTotal(count);
        }

        return monthlyResults;
    }
}
