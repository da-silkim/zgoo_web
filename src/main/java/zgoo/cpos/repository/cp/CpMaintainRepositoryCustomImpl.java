package zgoo.cpos.repository.cp;

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
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.cp.QCpMaintain;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.dto.cp.CpMaintainDto.CpInfoDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainListDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainRegDto;

@Slf4j
@RequiredArgsConstructor
public class CpMaintainRepositoryCustomImpl implements CpMaintainRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCpMaintain cpMaintain = QCpMaintain.cpMaintain;
    QCompany company = QCompany.company;
    QCpInfo cpInfo = QCpInfo.cpInfo;
    QCsInfo csInfo = QCsInfo.csInfo;
    QCommonCode errorTypeName = new QCommonCode("errorType");
    QCommonCode processStatusName = new QCommonCode("processStatus");

    @Override
    public Page<CpMaintainListDto> findCpMaintainWithPagination(Pageable pageable) {
        List<CpMaintainListDto> cpList = queryFactory.select(Projections.fields(CpMaintainListDto.class,
            cpMaintain.id.as("cpmaintainId"),
            cpMaintain.chargerId.as("chargerId"),
            cpMaintain.regDt.as("regDt"),
            cpMaintain.errorType.as("errorType"),
            cpMaintain.errorContent.as("errorContent"),
            cpMaintain.processDate.as("processDate"),
            cpMaintain.processStatus.as("processStatus"),
            cpMaintain.regUserId.as("regUserId"),
            company.companyName.as("companyName"),
            csInfo.stationName.as("stationName"),
            errorTypeName.name.as("errorTypeName"),
            processStatusName.name.as("processStatusName")))
            .from(cpMaintain)
            .leftJoin(cpInfo).on(cpMaintain.chargerId.eq(cpInfo.id))
            .leftJoin(csInfo).on(cpInfo.stationId.id.eq(csInfo.id))
            .leftJoin(company).on(csInfo.company.id.eq(company.id))
            .leftJoin(errorTypeName).on(cpMaintain.errorType.eq(errorTypeName.commonCode))
            .leftJoin(processStatusName).on(cpMaintain.processStatus.eq(processStatusName.commonCode))
            .orderBy(cpMaintain.regDt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .select(cpMaintain.count())
            .from(cpMaintain)
            .fetchOne();

        return new PageImpl<>(cpList, pageable, totalCount);
    }

    @Override
    public Page<CpMaintainListDto> searchCpMaintainWithPagination(Long companyId, String searchOp, String searchContent,
            String processStatus, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        System.out.println("searchCpMaintainWithPagination >> " + companyId + ", " + searchOp + ", " + searchContent + ", " +
            processStatus + ", " + startDate + ", " + endDate);

        log.info("Executing the [searchCpMaintainWithPagination]");
        log.info("companyId: {}, saerchOp: {}, searchContent: {}, processStatus: {}, startDate: {}, endDate: {}",
            companyId, searchOp, searchContent, processStatus, startDate, endDate);


        if (companyId != null) {
            System.out.println("companyId: " + companyId);
            builder.and(company.id.eq(companyId));
        }

        if (searchOp != null & (searchContent != null && !searchContent.isEmpty())) {
            if (searchOp.equals("stationName")) {
                System.out.println("stationName: " + searchContent);
                builder.and(csInfo.stationName.contains(searchContent));
            } else if (searchOp.equals("chargerId")) {
                System.out.println("chargerId: " + searchContent);
                builder.and(cpMaintain.chargerId.contains(searchContent));
            }
        }

        if (processStatus != null && !processStatus.isEmpty()) {
            System.out.println("processStatus: " + processStatus);
            builder.and(cpMaintain.processStatus.eq(processStatus));
        }

        if (startDate != null && endDate != null) {
            System.out.println("startDate, endDate: " + startDate + ", " + endDate);
            builder.and(cpMaintain.regDt.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));
        } else if (startDate != null) {
            System.out.println("startDate: " + startDate);
            builder.and(cpMaintain.regDt.goe(startDate.atStartOfDay()));
        } else if (endDate != null) {
            System.out.println("endDate: " + endDate);
            builder.and(cpMaintain.regDt.loe(endDate.atTime(23, 59, 59)));
        }

        List<CpMaintainListDto> cpList = queryFactory.select(Projections.fields(CpMaintainListDto.class,
            cpMaintain.id.as("cpmaintainId"),
            cpMaintain.chargerId.as("chargerId"),
            cpMaintain.regDt.as("regDt"),
            cpMaintain.errorType.as("errorType"),
            cpMaintain.errorContent.as("errorContent"),
            cpMaintain.processDate.as("processDate"),
            cpMaintain.processStatus.as("processStatus"),
            cpMaintain.regUserId.as("regUserId"),
            company.companyName.as("companyName"),
            csInfo.stationName.as("stationName"),
            errorTypeName.name.as("errorTypeName"),
            processStatusName.name.as("processStatusName")))
            .from(cpMaintain)
            .leftJoin(cpInfo).on(cpMaintain.chargerId.eq(cpInfo.id))
            .leftJoin(csInfo).on(cpInfo.stationId.id.eq(csInfo.id))
            .leftJoin(company).on(csInfo.company.id.eq(company.id))
            .leftJoin(errorTypeName).on(cpMaintain.errorType.eq(errorTypeName.commonCode))
            .leftJoin(processStatusName).on(cpMaintain.processStatus.eq(processStatusName.commonCode))
            .where(builder)
            .orderBy(cpMaintain.regDt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .select(cpMaintain.count())
            .from(cpMaintain)
            .leftJoin(cpInfo).on(cpMaintain.chargerId.eq(cpInfo.id))
            .leftJoin(csInfo).on(cpInfo.stationId.id.eq(csInfo.id))
            .leftJoin(company).on(csInfo.company.id.eq(company.id))
            .leftJoin(errorTypeName).on(cpMaintain.errorType.eq(errorTypeName.commonCode))
            .leftJoin(processStatusName).on(cpMaintain.processStatus.eq(processStatusName.commonCode))
            .where(builder)
            .fetchOne();

        return new PageImpl<>(cpList, pageable, totalCount);
    }

    @Override
    public CpInfoDto searchCsCpInfoWithChargerId(String chargerId) {
        CpInfoDto dto = queryFactory.select(Projections.fields(CpInfoDto.class,
            cpInfo.id.as("chargerId"),
            csInfo.id.as("stationId"),
            csInfo.stationName.as("stationName"),
            csInfo.address.as("address"),
            company.companyName.as("companyName")))
            .from(cpInfo)
            .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
            .leftJoin(company).on(csInfo.company.id.eq(company.id))
            .where(cpInfo.id.eq(chargerId))
            .fetchOne();
        return dto;
    }

    @Override
    public CpMaintainRegDto findMaintainOne(Long cpmaintainId) {
        CpMaintainRegDto dto = queryFactory.select(Projections.fields(CpMaintainRegDto.class,
            cpMaintain.id.as("cpmaintainId"),
            cpMaintain.chargerId.as("chargerId"),
            cpMaintain.regDt.as("regDt"),
            cpMaintain.errorType.as("errorType"),
            cpMaintain.errorContent.as("errorContent"),
            cpMaintain.pictureLoc1.as("pictureLoc1"),
            cpMaintain.pictureLoc2.as("pictureLoc2"),
            cpMaintain.pictureLoc3.as("pictureLoc3"),
            cpMaintain.processDate.as("processDate"),
            cpMaintain.processStatus.as("processStatus"),
            cpMaintain.processContent.as("processContent"),
            cpMaintain.regUserId.as("regUserId")))
            .from(cpMaintain)
            .where(cpMaintain.id.eq(cpmaintainId))
            .fetchOne();
        return dto;
    }
}
