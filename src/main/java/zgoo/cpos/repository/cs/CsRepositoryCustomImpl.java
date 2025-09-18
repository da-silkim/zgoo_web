package zgoo.cpos.repository.cs;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.cs.QCsKepcoContractInfo;
import zgoo.cpos.domain.cs.QCsLandInfo;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoDetailDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoListDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoRegDto;
import zgoo.cpos.dto.cs.CsInfoDto.StationOpStatusDto;
import zgoo.cpos.dto.cs.CsInfoDto.StationSearchDto;
import zgoo.cpos.util.LocaleUtil;
import zgoo.cpos.util.QueryUtils;

@Slf4j
@RequiredArgsConstructor
public class CsRepositoryCustomImpl implements CsRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCsInfo csInfo = QCsInfo.csInfo;
    QCsKepcoContractInfo kepco = QCsKepcoContractInfo.csKepcoContractInfo;
    QCsLandInfo land = QCsLandInfo.csLandInfo;
    QCompany company = QCompany.company;
    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
    QCommonCode opStatusCode = new QCommonCode("opStatus");
    QCommonCode stationTypeCode = new QCommonCode("stationType");
    QCommonCode facilityTypeCode = new QCommonCode("facilityType");
    QCommonCode landTypeCode = new QCommonCode("landType");
    QCommonCode rcvCapacityMethodCode = new QCommonCode("rcvCapacityMethod");
    QCommonCode voltageTypeCode = new QCommonCode("voltageType");

    @Override
    public Page<CsInfoListDto> findCsInfoWithPagination(Pageable pageable, String levelPath, boolean isSuperAdmin) {

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<CsInfoListDto> csList = queryFactory.select(Projections.fields(CsInfoListDto.class,
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName"),
                csInfo.address.as("address"),
                csInfo.openStartTime.as("openStartTime"),
                csInfo.openEndTime.as("openEndTime"),
                csInfo.opStatus.as("opStatus"),
                opStatusCode.name.as("opStatusName"),
                company.companyName.as("companyName")))
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .leftJoin(opStatusCode).on(csInfo.opStatus.eq(opStatusCode.commonCode))
                .orderBy(csInfo.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(builder)
                .fetch();

        long totalCount = queryFactory
                .select(csInfo.count())
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .leftJoin(opStatusCode).on(csInfo.opStatus.eq(opStatusCode.commonCode))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(csList, pageable, totalCount);
    }

    @Override
    public Page<CsInfoListDto> searchCsInfoWithPagination(Long companyId, String searchOp, String searchContent,
            Pageable pageable, String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (searchOp != null && (searchContent != null && !searchContent.isEmpty())) {
            if (searchOp.equals("stationId")) {
                builder.and(csInfo.id.contains(searchContent));
            } else if (searchOp.equals("stationName")) {
                builder.and(csInfo.stationName.contains(searchContent));
            }
        }

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<CsInfoListDto> csList = queryFactory.select(Projections.fields(CsInfoListDto.class,
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName"),
                csInfo.address.as("address"),
                csInfo.openStartTime.as("openStartTime"),
                csInfo.openEndTime.as("openEndTime"),
                csInfo.opStatus.as("opStatus"),
                opStatusCode.name.as("opStatusName"),
                company.companyName.as("companyName")))
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .leftJoin(opStatusCode).on(csInfo.opStatus.eq(opStatusCode.commonCode))
                .orderBy(csInfo.id.desc())
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(csInfo.count())
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .leftJoin(opStatusCode).on(csInfo.opStatus.eq(opStatusCode.commonCode))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(csList, pageable, totalCount);
    }

    @Override
    public boolean isStationNameDuplicate(String stationName) {
        CsInfo csInfoResult = queryFactory
                .selectFrom(csInfo)
                .where(csInfo.stationName.eq(stationName))
                .limit(1)
                .fetchOne();

        return csInfoResult != null;
    }

    @Override
    public String findRecentStationId(Long companyId) {
        return queryFactory
                .select(csInfo.id)
                .from(csInfo)
                .where(csInfo.company.id.eq(companyId))
                .orderBy(csInfo.id.desc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public CsInfoRegDto findCsInfoOne(String stationId) {
        return queryFactory.select(Projections.fields(CsInfoRegDto.class,
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName"),
                csInfo.stationType.as("stationType"),
                csInfo.facilityType.as("facilityType"),
                csInfo.asNum.as("asNum"),
                csInfo.opStatus.as("opStatus"),
                csInfo.latitude.as("latitude"),
                csInfo.longitude.as("longitude"),
                csInfo.zipcode.as("zipcode"),
                csInfo.address.as("address"),
                csInfo.addressDetail.as("addressDetail"),
                csInfo.openStartTime.as("openStartTime"),
                csInfo.openEndTime.as("openEndTime"),
                csInfo.parkingFeeYn.as("parkingFeeYn"),
                csInfo.sido.as("sido"),
                csInfo.safetyManagementFee.as("safetyManagementFee"),
                land.institutionName.as("institutionName"),
                land.landType.as("landType"),
                land.staffName.as("staffName"),
                land.staffPhone.as("staffPhone"),
                land.contractDate.as("contractDate"),
                land.startDate.as("startDate"),
                land.endDate.as("endDate"),
                land.landUseType.as("landUseType"),
                land.landUseFee.as("landUseFee"),
                land.settlementDate.as("settlementDate"),
                kepco.KepcoCustNo.as("kepcoCustNo"),
                kepco.openingDate.as("openingDate"),
                kepco.contPower.as("contPower"),
                kepco.rcvCapacityMethod.as("rcvCapacityMethod"),
                kepco.rcvCapacity.as("rcvCapacity"),
                kepco.voltageType.as("voltageType"),
                opStatusCode.name.as("opStatusName"),
                company.id.as("companyId"),
                company.companyName.as("companyName")))
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .leftJoin(opStatusCode).on(csInfo.opStatus.eq(opStatusCode.commonCode))
                .where(csInfo.id.eq(stationId))
                .fetchOne();
    }

    @Override
    public CsInfo findStationOne(String stationId) {
        return queryFactory
                .selectFrom(csInfo)
                .where(csInfo.id.eq(stationId))
                .fetchOne();
    }

    @Override
    public CsInfo findStationByKepcoCustNo(String kepcoCustNo) {
        return queryFactory
                .selectFrom(csInfo)
                .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
                .where(kepco.KepcoCustNo.eq(kepcoCustNo))
                .fetchOne();
    }

    @Override
    public CsInfoDetailDto findCsInfoDetailOne(String stationId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        CsInfoDetailDto csInfoDetailDto = queryFactory.select(Projections.fields(CsInfoDetailDto.class,
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName"),
                csInfo.stationType.as("stationType"),
                csInfo.facilityType.as("facilityType"),
                csInfo.asNum.as("asNum"),
                csInfo.opStatus.as("opStatus"),
                csInfo.latitude.as("latitude"),
                csInfo.longitude.as("longitude"),
                csInfo.zipcode.as("zipcode"),
                csInfo.address.as("address"),
                csInfo.addressDetail.as("addressDetail"),
                csInfo.openStartTime.as("openStartTime"),
                csInfo.openEndTime.as("openEndTime"),
                csInfo.parkingFeeYn.as("parkingFeeYn"),
                LocaleUtil.isEnglish()
                        ? Expressions.stringTemplate("IF({0}  = 'Y', 'Paid', 'Free')", csInfo.parkingFeeYn)
                                .as("parkingFeeString")
                        : Expressions.stringTemplate("IF({0}  = 'Y', '유료', '무료')", csInfo.parkingFeeYn)
                                .as("parkingFeeString"),
                csInfo.sido.as("sido"),
                Expressions.stringTemplate("IF({0} IS NULL, '정보없음', {0})", csInfo.safetyManagementFee)
                        .as("safetyFeeString"),
                csInfo.safetyManagementFee.as("safetyManagementFee"),
                land.institutionName.as("institutionName"),
                land.landType.as("landType"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", land.staffName).as("staffName"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", land.staffPhone)
                        .as("staffPhone"),
                Expressions.stringTemplate("IF({0} IS NULL, '정보없음', {0})", land.contractDate).as("contractDateString"),
                land.contractDate.as("contractDate"),
                land.startDate.as("startDate"),
                land.endDate.as("endDate"),
                land.landUseType.as("landUseType"),
                land.landUseFee.as("landUseFee"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', CONCAT({0}, '일'))", land.settlementDate)
                        .as("settlementDate"),
                kepco.KepcoCustNo.as("kepcoCustNo"),
                kepco.openingDate.as("openingDate"),
                kepco.contPower.as("contPower"),
                kepco.rcvCapacityMethod.as("rcvCapacityMethod"),
                kepco.rcvCapacity.as("rcvCapacity"),
                kepco.voltageType.as("voltageType"),
                LocaleUtil.isEnglish() ? opStatusCode.nameEn.as("opStatusName") : opStatusCode.name.as("opStatusName"),
                stationTypeCode.name.as("stationTypeName"),
                facilityTypeCode.name.as("facilityTypeName"),
                landTypeCode.name.as("landTypeName"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", rcvCapacityMethodCode.name)
                        .as("rcvCapacityMethodName"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", voltageTypeCode.name)
                        .as("voltageTypeName"),
                Expressions.stringTemplate("IF({0} IS NULL, '정보없음', {0})", kepco.rcvCapacity).as("rcvCapacityString"),
                company.companyName.as("companyName")))
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .leftJoin(opStatusCode).on(csInfo.opStatus.eq(opStatusCode.commonCode))
                .leftJoin(stationTypeCode).on(csInfo.stationType.eq(stationTypeCode.commonCode))
                .leftJoin(facilityTypeCode).on(csInfo.facilityType.eq(facilityTypeCode.commonCode))
                .leftJoin(landTypeCode).on(land.landType.eq(landTypeCode.commonCode))
                .leftJoin(rcvCapacityMethodCode).on(kepco.rcvCapacityMethod.eq(rcvCapacityMethodCode.commonCode))
                .leftJoin(voltageTypeCode).on(kepco.voltageType.eq(voltageTypeCode.commonCode))
                .where(csInfo.id.eq(stationId))
                .fetchOne();

        if (csInfoDetailDto.getOpenStartTime() != null && csInfoDetailDto.getOpenEndTime() != null) {
            csInfoDetailDto.setOpenStartTimeFormatted(csInfoDetailDto.getOpenStartTime().format(formatter));
            csInfoDetailDto.setOpenEndTimeFormatted(csInfoDetailDto.getOpenEndTime().format(formatter));
        }

        return csInfoDetailDto;
    }

    @Override
    public Long deleteCsInfoOne(String stationId) {
        return queryFactory
                .delete(csInfo)
                .where(csInfo.id.eq(stationId))
                .execute();
    }

    @Override
    public List<CsInfoDetailDto> findCsInfo() {
        List<CsInfoDetailDto> csList = queryFactory.select(Projections.fields(CsInfoDetailDto.class,
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName"),
                csInfo.stationType.as("stationType"),
                csInfo.facilityType.as("facilityType"),
                csInfo.opStatus.as("opStatus"),
                csInfo.latitude.as("latitude"),
                csInfo.longitude.as("longitude"),
                csInfo.zipcode.as("zipcode"),
                csInfo.address.as("address"),
                csInfo.addressDetail.as("addressDetail"),
                csInfo.openStartTime.as("openStartTime"),
                csInfo.openEndTime.as("openEndTime"),
                csInfo.parkingFeeYn.as("parkingFeeYn"),
                csInfo.sido.as("sido"),
                csInfo.safetyManagementFee.as("safetyManagementFee"),
                opStatusCode.name.as("opStatusName"),
                company.companyName.as("companyName")))
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .leftJoin(opStatusCode).on(csInfo.opStatus.eq(opStatusCode.commonCode))
                .orderBy(csInfo.id.desc())
                .fetch();
        return csList;
    }

    @Override
    public List<StationSearchDto> findCsInfoContainKeyword(String keyword, String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.and(csInfo.stationName.contains(keyword));
        }

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<StationSearchDto> csList = queryFactory.select(Projections.fields(StationSearchDto.class,
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName"),
                csInfo.address.as("address"),
                csInfo.latitude.as("latitude"),
                csInfo.longitude.as("longitude")))
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetch();
        return csList;
    }

    @Override
    public List<StationSearchDto> searchStationByOption(String searchOp, String searchContent, String levelPath,
            boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchOp != null && (searchContent != null && !searchContent.isEmpty())) {
            if (searchOp.equals("stationId")) {
                builder.and(csInfo.id.contains(searchContent));
            } else if (searchOp.equals("stationName")) {
                builder.and(csInfo.stationName.contains(searchContent));
            }
        }

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<StationSearchDto> csList = queryFactory.select(Projections.fields(StationSearchDto.class,
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName")))
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetch();

        return csList;
    }

    @Override
    public List<CsInfoDetailDto> findStationsWithinRadius(double latitude, double longitude, double radiusInKm,
            String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        NumberExpression<Double> distance = Expressions.numberTemplate(Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
                latitude, csInfo.latitude, csInfo.longitude, longitude);

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<CsInfoDetailDto> csList = queryFactory.select(Projections.fields(CsInfoDetailDto.class,
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName"),
                csInfo.stationType.as("stationType"),
                csInfo.facilityType.as("facilityType"),
                csInfo.opStatus.as("opStatus"),
                csInfo.latitude.as("latitude"),
                csInfo.longitude.as("longitude"),
                csInfo.zipcode.as("zipcode"),
                csInfo.address.as("address"),
                csInfo.addressDetail.as("addressDetail"),
                csInfo.openStartTime.as("openStartTime"),
                csInfo.openEndTime.as("openEndTime"),
                csInfo.parkingFeeYn.as("parkingFeeYn"),
                opStatusCode.name.as("opStatusName"),
                company.companyName.as("companyName")))
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .leftJoin(opStatusCode).on(csInfo.opStatus.eq(opStatusCode.commonCode))
                .where(distance.loe(radiusInKm), builder)
                .orderBy(csInfo.id.desc())
                .fetch();

        return csList;
    }

    @Override
    public List<CsInfoListDto> findAllStationWithoutPagination(Long companyId, String searchOp, String searchContent,
            String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (searchOp != null && (searchContent != null && !searchContent.isEmpty())) {
            if (searchOp.equals("stationId")) {
                builder.and(csInfo.id.contains(searchContent));
            } else if (searchOp.equals("stationName")) {
                builder.and(csInfo.stationName.contains(searchContent));
            }
        }

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        return queryFactory.select(Projections.fields(CsInfoListDto.class,
                csInfo.id.as("stationId"),
                csInfo.stationName.as("stationName"),
                csInfo.address.as("address"),
                csInfo.openStartTime.as("openStartTime"),
                csInfo.openEndTime.as("openEndTime"),
                csInfo.opStatus.as("opStatus"),
                opStatusCode.name.as("opStatusName"),
                company.companyName.as("companyName")))
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
                .leftJoin(land).on(csInfo.csLandInfo.eq(land))
                .leftJoin(opStatusCode).on(csInfo.opStatus.eq(opStatusCode.commonCode))
                .orderBy(csInfo.id.desc())
                .where(builder)
                .fetch();
    }

    @Override
    public StationOpStatusDto getStationOpStatusCount(String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        StationOpStatusDto dto = queryFactory.select(Projections.fields(StationOpStatusDto.class,
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'OPTEST' THEN 1 END)",
                        csInfo.opStatus).as("opTestCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'OPSTOP' THEN 1 END)",
                        csInfo.opStatus).as("opStopCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'OPERATING' THEN 1 END)",
                        csInfo.opStatus).as("operatingCount")))
                .from(csInfo)
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        return dto;
    }
}
