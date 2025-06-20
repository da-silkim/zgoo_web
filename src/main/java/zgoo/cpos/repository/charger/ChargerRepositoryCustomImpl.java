package zgoo.cpos.repository.charger;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.charger.CpInfo;
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.domain.charger.QCpModem;
import zgoo.cpos.domain.charger.QCpStatus;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.company.QCpPlanPolicy;
import zgoo.cpos.domain.cp.QCpModel;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.history.QChargingHist;
import zgoo.cpos.dto.cp.ChargerDto.ChargerCountBySidoDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerListDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerSearchDto;
import zgoo.cpos.dto.cp.ChargerDto.FacilityCountDto;
import zgoo.cpos.util.QueryUtils;

@RequiredArgsConstructor
@Slf4j
public class ChargerRepositoryCustomImpl implements ChargerRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QCompany company = QCompany.company;

    @Override
    public Page<ChargerListDto> findChargerListByCompanyFw(Pageable page, Long companyId) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(csInfo.company.id.eq(companyId));

        List<ChargerListDto> chargerList = queryFactory.select(Projections.fields(ChargerListDto.class,
                company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                csInfo.id.as("stationId"),
                cpInfo.chargerName.as("chargerName"),
                cpInfo.id.as("chargerId"),
                commonTypeName.name.as("commonTypeName"),
                model.modelName.as("modelName"),
                cpplan.name.as("policyName"),
                cpInfo.installDate.as("installDate"), // LocalDate 타입 그대로 사용
                manufCdName.name.as("manufCdName"),
                cpInfo.fwVersion.as("fwVersion"),
                cpStatus.lastFwupdateTime.as("lastUpdateDt")))
                .from(cpInfo)
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(cpStatus).on(cpInfo.id.eq(cpStatus.chargerId))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(commonTypeName).on(cpInfo.commonType.eq(commonTypeName.commonCode))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpplan).on(cpInfo.planInfo.id.eq(cpplan.id))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .where(builder)
                .orderBy(cpInfo.regDt.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(cpInfo.count())
                .from(cpInfo)
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(cpStatus).on(cpInfo.id.eq(cpStatus.chargerId))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(chargerList, page, totalCount);
    }

    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
    QCsInfo csInfo = QCsInfo.csInfo;
    QCpInfo cpInfo = QCpInfo.cpInfo;
    QCpStatus cpStatus = QCpStatus.cpStatus;
    QCpModel model = QCpModel.cpModel;
    // QConnectorStatus connector = QConnectorStatus.connectorStatus;
    QCpPlanPolicy cpplan = QCpPlanPolicy.cpPlanPolicy;
    QChargingHist hist = QChargingHist.chargingHist;
    QChargingHist histSub = new QChargingHist("histSub");
    QCommonCode commonTypeName = new QCommonCode("commonTypeCode");
    QCommonCode manufCdName = new QCommonCode("manufCd");
    QCommonCode cpTypeName = new QCommonCode("cpType");
    QCommonCode facilityTypeName = new QCommonCode("facilityType");

    @Override
    public Page<ChargerListDto> findAllChargerListPaging(Pageable pageable, String levelPath, boolean isSuperAdmin) {

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<ChargerListDto> chargerList = queryFactory.select(Projections.fields(ChargerListDto.class,
                company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                cpInfo.chargerName.as("chargerName"),
                cpInfo.id.as("chargerId"),
                csInfo.id.as("stationId"),
                commonTypeName.name.as("commonTypeName"),
                model.modelName.as("modelName"),
                cpplan.name.as("policyName"),
                cpInfo.installDate.as("installDate"), // LocalDate 타입 그대로 사용
                manufCdName.name.as("manufCdName"),
                cpInfo.modelCode.as("modelCode"),
                cpInfo.protocol.as("protocol")))
                .from(cpInfo)
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(commonTypeName).on(cpInfo.commonType.eq(commonTypeName.commonCode))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpplan).on(cpInfo.planInfo.id.eq(cpplan.id))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .where(builder)
                .orderBy(cpInfo.regDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(cpInfo.count())
                .from(cpInfo)
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(chargerList, pageable, totalCount);
    }

    @Override
    public Page<ChargerListDto> findChargerListPaging(Long companyId, String manufCd, String searchOp,
            String searchContent, Pageable pageable, String levelPath, boolean isSuperAdmin) {

        BooleanBuilder builder = new BooleanBuilder();

        // 조건 추가: companyId
        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        // 조건 추가: manufCd (model 대신 직접 cpInfo.modelCode.manufCd 사용)
        if (manufCd != null && !manufCd.isEmpty()) {
            builder.and(model.manufCd.eq(manufCd));
        }

        // 조건 추가: searchOp와 searchContent
        if (searchContent != null && !searchContent.isEmpty()) {
            if (searchOp != null) {
                switch (searchOp.toLowerCase()) {
                    case "stationname":
                        builder.and(csInfo.stationName.containsIgnoreCase(searchContent));
                        break;
                    case "stationid":
                        builder.and(csInfo.id.containsIgnoreCase(searchContent));
                        break;
                    case "chargerid":
                        builder.and(cpInfo.id.containsIgnoreCase(searchContent));
                        break;
                    default:
                        // searchOp가 정의되지 않은 값이면 기본 동작을 설정 (예: 모든 검색 건너뛰기 또는 로그 기록)
                        break;
                }
            }
        }

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        // 동적 쿼리를 사용하여 조회
        List<ChargerListDto> chargerList = queryFactory.select(Projections.fields(ChargerListDto.class,
                company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                cpInfo.chargerName.as("chargerName"),
                cpInfo.id.as("chargerId"),
                csInfo.id.as("stationId"),
                commonTypeName.name.as("commonTypeName"),
                model.modelName.as("modelName"),
                cpplan.name.as("policyName"),
                cpInfo.installDate.as("installDate"),
                manufCdName.name.as("manufCdName"),
                cpInfo.modelCode.as("modelCode"),
                cpInfo.protocol.as("protocol")))
                .from(cpInfo)
                .join(csInfo).on(cpInfo.stationId.eq(csInfo))
                .join(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(commonTypeName).on(cpInfo.commonType.eq(commonTypeName.commonCode))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpplan).on(cpInfo.planInfo.eq(cpplan))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .where(builder)
                .orderBy(cpInfo.regDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회 쿼리도 동일한 조건 사용
        long totalCount = queryFactory
                .select(cpInfo.count())
                .from(cpInfo)
                .join(csInfo).on(cpInfo.stationId.eq(csInfo))
                .join(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode)) // manufCd 조건을 위해 model 조인 추가
                .where(builder)
                .fetchOne();

        return new PageImpl<>(chargerList, pageable, totalCount);
    }

    @Override
    public Optional<CpInfo> findCpByStationId(String stationId) {
        CpInfo result = queryFactory.select(cpInfo)
                .from(cpInfo)
                .where(cpInfo.stationId.id.eq(stationId))
                .orderBy(cpInfo.id.desc())
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<ChargerSearchDto> findChargerListByStationId(String stationId, String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (stationId != null && !stationId.isEmpty()) {
            builder.and(cpInfo.stationId.id.eq(stationId));
        }

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<ChargerSearchDto> chargerList = queryFactory.select(Projections.fields(ChargerSearchDto.class,
                cpInfo.id.as("chargerId"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '-', {0})", cpInfo.chargerName)
                        .as("chargerName"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '-', {0})", cpInfo.fwVersion).as("fwVersion"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '-', {0})", cpInfo.location).as("location"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '-', {0})", model.modelName).as("modelName"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '-', {0})", manufCdName.name).as("manufCdName"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '-', {0})", cpTypeName.name).as("cpTypeName"),
                hist.startTime.as("recentDt")))
                .from(cpInfo)
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .leftJoin(hist).on(
                        hist.chargerID.eq(cpInfo.id)
                                .and(hist.startTime.eq(
                                        JPAExpressions
                                                .select(histSub.startTime.max())
                                                .from(histSub)
                                                .where(histSub.chargerID.eq(cpInfo.id)))))
                .where(builder)
                .fetch();
        return chargerList;
    }

    @Override
    public long countByStationId(String stationId, String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (stationId != null && !stationId.isEmpty()) {
            builder.and(cpInfo.stationId.id.eq(stationId));
        }

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        return queryFactory
                .select(cpInfo.count())
                .from(cpInfo)
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();
    }

    @Override
    public CpInfo findCpInfoByChargerId(String chargerId) {
        QCpInfo cpInfo = QCpInfo.cpInfo;
        QCsInfo csInfo = QCsInfo.csInfo;
        QCpModem cpModem = QCpModem.cpModem;
        QCpPlanPolicy planPolicy = QCpPlanPolicy.cpPlanPolicy;

        return queryFactory
                .selectFrom(cpInfo)
                .leftJoin(cpInfo.stationId, csInfo).fetchJoin() // CsInfo 함께 로딩
                .leftJoin(cpInfo.cpmodemInfo, cpModem).fetchJoin() // CpModem 함께 로딩
                .leftJoin(cpInfo.planInfo, planPolicy).fetchJoin() // CpPlanPolicy 함께 로딩
                .where(cpInfo.id.eq(chargerId))
                .fetchOne();
    }

    @Override
    public List<ChargerListDto> findAllChargerListWithoutPagination(Long companyId, String manufCd, String searchOp,
            String searchContent, String levelPath, boolean isSuperAdmin) {
        log.info(
                "=== Repository(findAllChargerListWithoutPagination): Finding all charger list >> companyId:{}, manufCd:{}, searchOp:{}, searchContent:{} ===",
                companyId, manufCd, searchOp, searchContent);

        BooleanBuilder builder = new BooleanBuilder();

        // 조건 추가: companyId
        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        // 조건 추가: manufCd
        if (manufCd != null && !manufCd.isEmpty()) {
            builder.and(model.manufCd.eq(manufCd));
        }

        // 조건 추가: searchOp와 searchContent
        if (searchContent != null && !searchContent.isEmpty()) {
            if (searchOp != null) {
                switch (searchOp.toLowerCase()) {
                    case "stationname":
                        builder.and(csInfo.stationName.containsIgnoreCase(searchContent));
                        break;
                    case "stationid":
                        builder.and(csInfo.id.containsIgnoreCase(searchContent));
                        break;
                    case "chargerid":
                        builder.and(cpInfo.id.containsIgnoreCase(searchContent));
                        break;
                    default:
                        break;
                }
            }
        }

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        // 대용량 데이터 처리를 위한 최적화된 쿼리
        // 필요한 필드만 선택하여 메모리 사용량 최소화
        return queryFactory.select(Projections.fields(ChargerListDto.class,
                company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                cpInfo.chargerName.as("chargerName"),
                cpInfo.id.as("chargerId"),
                commonTypeName.name.as("commonTypeName"),
                model.modelName.as("modelName"),
                cpplan.name.as("policyName"),
                cpInfo.installDate.as("installDate"),
                manufCdName.name.as("manufCdName")))
                .from(cpInfo)
                .join(csInfo).on(cpInfo.stationId.eq(csInfo))
                .join(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(commonTypeName).on(cpInfo.commonType.eq(commonTypeName.commonCode))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpplan).on(cpInfo.planInfo.eq(cpplan))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .where(builder)
                .orderBy(cpInfo.regDt.desc())
                .fetch();
    }

    @Override
    public long countCharger(String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        return queryFactory
                .select(cpInfo.count())
                .from(cpInfo)
                .join(csInfo).on(cpInfo.stationId.eq(csInfo))
                .join(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();
    }

    @Override
    public List<ChargerCountBySidoDto> countChargerBySidoAndType(String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(csInfo.sido.isNotNull());

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        return queryFactory
                .select(Projections.fields(ChargerCountBySidoDto.class,
                        csInfo.sido.as("sido"),

                        // SPEEDFAST
                        ExpressionUtils.as(
                                Expressions.numberTemplate(Long.class,
                                        "SUM(CASE WHEN {0} = {1} THEN 1 ELSE 0 END)",
                                        model.cpType,
                                        Expressions.constant("SPEEDFAST")),
                                "speedFastCount"),

                        // SPEEDLOW
                        ExpressionUtils.as(
                                Expressions.numberTemplate(Long.class,
                                        "SUM(CASE WHEN {0} = {1} THEN 1 ELSE 0 END)",
                                        model.cpType,
                                        Expressions.constant("SPEEDLOW")),
                                "speedLowCount"),

                        // SPEEDDESPN
                        ExpressionUtils.as(
                                Expressions.numberTemplate(Long.class,
                                        "SUM(CASE WHEN {0} = {1} THEN 1 ELSE 0 END)",
                                        model.cpType,
                                        Expressions.constant("SPEEDDESPN")),
                                "speedDespnCount")))
                .from(cpInfo)
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .groupBy(csInfo.sido)
                .orderBy(csInfo.sido.asc())
                .fetch();
    }

    @Override
    public List<FacilityCountDto> countFacilityBySidoAndType(String sido, String type, String levelPath,
            boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (sido != null && !sido.isEmpty() && type != null && !type.isEmpty()) {
            builder.and(csInfo.sido.eq(sido)).and(cpTypeName.name.eq(type));
        }

        return queryFactory.select(Projections.fields(FacilityCountDto.class,
                facilityTypeName.name.as("facility"),
                cpInfo.count().as("count")))
                .from(cpInfo)
                .leftJoin(csInfo).on(csInfo.id.eq(cpInfo.stationId.id))
                .leftJoin(model).on(model.modelCode.eq(cpInfo.modelCode))
                .leftJoin(facilityTypeName).on(facilityTypeName.commonCode.eq(csInfo.facilityType))
                .leftJoin(cpTypeName).on(cpTypeName.commonCode.eq(model.cpType))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .groupBy(facilityTypeName.name)
                .fetch();
    }

    @Override
    public Page<ChargerListDto> findChargerListByCompanyAndStationFw(Pageable pageable, Long companyId,
            String stationId) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(csInfo.company.id.eq(companyId));
        builder.and(csInfo.id.eq(stationId));

        List<ChargerListDto> chargerList = queryFactory.select(Projections.fields(ChargerListDto.class,
                company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                csInfo.id.as("stationId"),
                cpInfo.chargerName.as("chargerName"),
                cpInfo.id.as("chargerId"),
                commonTypeName.name.as("commonTypeName"),
                model.modelName.as("modelName"),
                cpplan.name.as("policyName"),
                cpInfo.installDate.as("installDate"), // LocalDate 타입 그대로 사용
                manufCdName.name.as("manufCdName"),
                cpInfo.fwVersion.as("fwVersion"),
                cpStatus.lastFwupdateTime.as("lastUpdateDt")))
                .from(cpInfo)
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(cpStatus).on(cpInfo.id.eq(cpStatus.chargerId))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(commonTypeName).on(cpInfo.commonType.eq(commonTypeName.commonCode))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpplan).on(cpInfo.planInfo.id.eq(cpplan.id))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .where(builder)
                .orderBy(cpInfo.regDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(cpInfo.count())
                .from(cpInfo)
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(cpStatus).on(cpInfo.id.eq(cpStatus.chargerId))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(chargerList, pageable, totalCount);
    }
}
