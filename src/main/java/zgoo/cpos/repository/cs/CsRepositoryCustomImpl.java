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
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.cs.QCsKepcoContractInfo;
import zgoo.cpos.domain.cs.QCsLandInfo;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoBaseDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoDetailDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoListDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoRegDto;
import zgoo.cpos.dto.cs.CsInfoDto.StationSearchDto;

@Slf4j
@RequiredArgsConstructor
public class CsRepositoryCustomImpl implements CsRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCsInfo csInfo = QCsInfo.csInfo;
    QCsKepcoContractInfo kepco = QCsKepcoContractInfo.csKepcoContractInfo;
    QCsLandInfo land = QCsLandInfo.csLandInfo;
    QCompany company = QCompany.company;
    QCommonCode opStatusCode = new QCommonCode("opStatus");
    QCommonCode stationTypeCode = new QCommonCode("stationType");
    QCommonCode facilityTypeCode = new QCommonCode("facilityType");
    QCommonCode landTypeCode = new QCommonCode("landType");
    QCommonCode rcvCapacityMethodCode = new QCommonCode("rcvCapacityMethod");
    QCommonCode voltageTypeCode = new QCommonCode("voltageType");

    @Override
    public Page<CsInfoListDto> findCsInfoWithPagination(Pageable pageable) {
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
            .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
            .leftJoin(land).on(csInfo.csLandInfo.eq(land))
            .leftJoin(opStatusCode).on(csInfo.opStatus.eq(opStatusCode.commonCode))
            .orderBy(csInfo.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .select(csInfo.count())
            .from(csInfo)
            .fetchOne();

        return new PageImpl<>(csList, pageable, totalCount);
    }

    @Override
    public Page<CsInfoListDto> searchCsInfoWithPagination(Long companyId, String searchOp, String searchContent,
            Pageable pageable) {
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
            .where(builder)
            .fetchOne();

        return new PageImpl<>(csList, pageable, totalCount);
    }

    @Override
    public boolean isStationNameDuplicate(String stationName) {
        CsInfo csInfoResult =  queryFactory
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
            land.institutionName.as("institutionName"),
            land.landType.as("landType"),
            land.staffName.as("staffName"),
            land.staffPhone.as("staffPhone"),
            land.contractDate.as("contractDate"),
            land.startDate.as("startDate"),
            land.endDate.as("endDate"),
            land.landUseRate.as("landUseRate"),
            land.billDate.as("billDate"),
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
            Expressions.stringTemplate("IF({0}  = 'Y', '유료', '무료')", csInfo.parkingFeeYn).as("parkingFeeString"),
            land.institutionName.as("institutionName"),
            land.landType.as("landType"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", land.staffName).as("staffName"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", land.staffPhone).as("staffPhone"),
            Expressions.stringTemplate("IF({0} IS NULL, '정보없음', {0})", land.contractDate).as("contractDateString"),
            land.contractDate.as("contractDate"),
            land.startDate.as("startDate"),
            land.endDate.as("endDate"),
            land.landUseRate.as("landUseRate"),
            land.billDate.as("billDate"),
            Expressions.stringTemplate("IF({0} IS NULL, '정보없음', {0})", land.billDate).as("billDateString"),
            kepco.KepcoCustNo.as("kepcoCustNo"),
            kepco.openingDate.as("openingDate"),
            kepco.contPower.as("contPower"),
            kepco.rcvCapacityMethod.as("rcvCapacityMethod"),
            kepco.rcvCapacity.as("rcvCapacity"),
            kepco.voltageType.as("voltageType"),
            opStatusCode.name.as("opStatusName"),
            stationTypeCode.name.as("stationTypeName"),
            facilityTypeCode.name.as("facilityTypeName"),
            landTypeCode.name.as("landTypeName"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", rcvCapacityMethodCode.name).as("rcvCapacityMethodName"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", voltageTypeCode.name).as("voltageTypeName"),
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
    public CsInfoDetailDto findPreviousCsInfo(String stationId, Long companyId, String searchOp, String searchContent) {
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

        builder.and(csInfo.id.lt(stationId));

        return queryFactory.select(Projections.fields(CsInfoDetailDto.class,
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
            land.institutionName.as("institutionName"),
            land.landType.as("landType"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", land.staffName).as("staffName"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", land.staffPhone).as("staffPhone"),
            Expressions.stringTemplate("IF({0} IS NULL, '정보없음', {0})", land.contractDate).as("contractDateString"),
            land.contractDate.as("contractDate"),
            land.startDate.as("startDate"),
            land.endDate.as("endDate"),
            land.landUseRate.as("landUseRate"),
            land.billDate.as("billDate"),
            Expressions.stringTemplate("IF({0} IS NULL, '정보없음', {0})", land.billDate).as("billDateString"),
            kepco.KepcoCustNo.as("kepcoCustNo"),
            kepco.openingDate.as("openingDate"),
            kepco.contPower.as("contPower"),
            kepco.rcvCapacityMethod.as("rcvCapacityMethod"),
            kepco.rcvCapacity.as("rcvCapacity"),
            kepco.voltageType.as("voltageType"),
            opStatusCode.name.as("opStatusName"),
            stationTypeCode.name.as("stationTypeName"),
            facilityTypeCode.name.as("facilityTypeName"),
            landTypeCode.name.as("landTypeName"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", rcvCapacityMethodCode.name).as("rcvCapacityMethodName"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", voltageTypeCode.name).as("voltageTypeName"),
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
            .where(builder)
            .orderBy(csInfo.id.desc())
            .fetchFirst();
    }

    @Override
    public CsInfoDetailDto findNextCsInfo(String stationId, Long companyId, String searchOp, String searchContent) {
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

        builder.and(csInfo.id.gt(stationId));

        return queryFactory.select(Projections.fields(CsInfoDetailDto.class,
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
            land.institutionName.as("institutionName"),
            land.landType.as("landType"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", land.staffName).as("staffName"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", land.staffPhone).as("staffPhone"),
            Expressions.stringTemplate("IF({0} IS NULL, '정보없음', {0})", land.contractDate).as("contractDateString"),
            land.contractDate.as("contractDate"),
            land.startDate.as("startDate"),
            land.endDate.as("endDate"),
            land.landUseRate.as("landUseRate"),
            land.billDate.as("billDate"),
            Expressions.stringTemplate("IF({0} IS NULL, '정보없음', {0})", land.billDate).as("billDateString"),
            kepco.KepcoCustNo.as("kepcoCustNo"),
            kepco.openingDate.as("openingDate"),
            kepco.contPower.as("contPower"),
            kepco.rcvCapacityMethod.as("rcvCapacityMethod"),
            kepco.rcvCapacity.as("rcvCapacity"),
            kepco.voltageType.as("voltageType"),
            opStatusCode.name.as("opStatusName"),
            stationTypeCode.name.as("stationTypeName"),
            facilityTypeCode.name.as("facilityTypeName"),
            landTypeCode.name.as("landTypeName"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", rcvCapacityMethodCode.name).as("rcvCapacityMethodName"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", voltageTypeCode.name).as("voltageTypeName"),
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
            .where(builder)
            .orderBy(csInfo.id.asc())
            .fetchFirst();
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
    public List<StationSearchDto> findCsInfoContainKeyword(String keyword) {
        List<StationSearchDto> csList = queryFactory.select(Projections.fields(StationSearchDto.class,
            csInfo.id.as("stationId"),
            csInfo.stationName.as("stationName"),
            csInfo.address.as("address"),
            csInfo.latitude.as("latitude"),
            csInfo.longitude.as("longitude")))
            .from(csInfo)
            .where(csInfo.stationName.contains(keyword))
            .fetch();
        return csList;
    }

    @Override
    public List<CsInfoDetailDto> findStationsWithinRadius(double latitude, double longitude, double radiusInKm) {
        NumberExpression<Double> distance = Expressions.numberTemplate(Double.class,
        "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
                latitude, csInfo.latitude, csInfo.longitude, longitude);

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
            .leftJoin(kepco).on(csInfo.csKepcoContractInfo.eq(kepco))
            .leftJoin(land).on(csInfo.csLandInfo.eq(land))
            .leftJoin(opStatusCode).on(csInfo.opStatus.eq(opStatusCode.commonCode))
            .where(distance.loe(radiusInKm))
            .orderBy(csInfo.id.desc())
            .fetch();
        return csList;
    }
}
