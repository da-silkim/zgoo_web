package zgoo.cpos.repository.history;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import zgoo.cpos.domain.code.QChgErrorCode;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.cp.QCpModel;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.history.QErrorHist;
import zgoo.cpos.dto.history.ErrorHistDto;
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
                .orderBy(errorHist.occurDate.desc())
                .limit(4)
                .fetch();
        return errorHistList;
    }
}
