package zgoo.cpos.repository.charger;

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
import zgoo.cpos.domain.charger.QCpCurrentTx;
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.member.QMember;
import zgoo.cpos.dto.cp.CurrentChargingListDto;
import zgoo.cpos.util.QueryUtils;

@RequiredArgsConstructor
@Slf4j
public class CpCurrentTxRepositoryCustomImpl implements CpCurrentTxRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QCpCurrentTx cpCurrentTx = QCpCurrentTx.cpCurrentTx;
    QCsInfo csInfo = QCsInfo.csInfo;
    QCpInfo cpInfo = QCpInfo.cpInfo;
    QMember member = QMember.member;
    QCompany company = QCompany.company;
    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;

    @Override
    public Page<CurrentChargingListDto> findAllChargerListPaging(Pageable page, String levelPath,
            boolean isSuperAdmin) {

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<CurrentChargingListDto> chargingList = queryFactory.select(Projections.fields(CurrentChargingListDto.class,
                csInfo.company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                csInfo.id.as("stationId"),
                cpCurrentTx.id.chargerId.as("chargerId"),
                cpCurrentTx.id.connectorId.as("connectorId"),
                cpCurrentTx.startTime.as("chgStartTime"),
                member.name.as("memberName"),
                cpCurrentTx.idTag.as("memberCardNo"),
                cpCurrentTx.chargePower.as("chgAmount"),
                cpCurrentTx.remainStopTs.as("remainTime"),
                cpCurrentTx.soc.as("soc"),
                cpCurrentTx.transactionId.as("transactionId")))
                .from(cpCurrentTx)
                .leftJoin(cpInfo).on(cpCurrentTx.id.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .join(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(member).on(cpCurrentTx.idTag.eq(member.idTag))
                .where(builder)
                .orderBy(cpCurrentTx.startTime.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(cpCurrentTx.count())
                .from(cpCurrentTx)
                .leftJoin(cpInfo).on(cpCurrentTx.id.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .join(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(member).on(cpCurrentTx.idTag.eq(member.idTag))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(chargingList, page, totalCount);
    }

    @Override
    public Page<CurrentChargingListDto> findChargerListPaging(Long companyId, String chgStartTimeFrom,
            String chgStartTimeTo, String searchOp, String searchContent, Pageable pageable, String levelPath,
            boolean isSuperAdmin) {

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (chgStartTimeFrom != null && !chgStartTimeFrom.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime fromDateTime;
            if (chgStartTimeFrom.length() <= 10) { // yyyy-MM-dd 형식인 경우
                fromDateTime = LocalDateTime.parse(chgStartTimeFrom + " 00:00:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                fromDateTime = LocalDateTime.parse(chgStartTimeFrom,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            builder.and(cpCurrentTx.startTime.after(fromDateTime));
        }

        if (chgStartTimeTo != null && !chgStartTimeTo.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime toDateTime;
            if (chgStartTimeTo.length() <= 10) { // yyyy-MM-dd 형식인 경우
                toDateTime = LocalDateTime.parse(chgStartTimeTo + " 23:59:59",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                toDateTime = LocalDateTime.parse(chgStartTimeTo,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            builder.and(cpCurrentTx.startTime.before(toDateTime));
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
                    builder.and(cpCurrentTx.id.chargerId.containsIgnoreCase(searchContent));
                    break;
                default:
                    break;
            }
        }

        List<CurrentChargingListDto> chargingList = queryFactory.select(Projections.fields(CurrentChargingListDto.class,
                csInfo.company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                csInfo.id.as("stationId"),
                cpCurrentTx.id.chargerId.as("chargerId"),
                cpCurrentTx.id.connectorId.as("connectorId"),
                cpCurrentTx.startTime.as("chgStartTime"),
                member.name.as("memberName"),
                cpCurrentTx.idTag.as("memberCardNo"),
                cpCurrentTx.chargePower.as("chgAmount"),
                cpCurrentTx.remainStopTs.as("remainTime"),
                cpCurrentTx.soc.as("soc"),
                cpCurrentTx.transactionId.as("transactionId")))
                .from(cpCurrentTx)
                .leftJoin(cpInfo).on(cpCurrentTx.id.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .join(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(member).on(cpCurrentTx.idTag.eq(member.idTag))
                .where(builder)
                .orderBy(cpCurrentTx.startTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(cpCurrentTx.count())
                .from(cpCurrentTx)
                .leftJoin(cpInfo).on(cpCurrentTx.id.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .join(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(member).on(cpCurrentTx.idTag.eq(member.idTag))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(chargingList, pageable, totalCount);

    }

    @Override
    public List<CurrentChargingListDto> findAllCurrentTxListWithoutPagination(Long companyId, String startFrom,
            String startTo, String searchOp, String searchContent, String levelPath, boolean isSuperAdmin) {

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (startFrom != null && !startFrom.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime fromDateTime;
            if (startFrom.length() <= 10) { // yyyy-MM-dd 형식인 경우
                fromDateTime = LocalDateTime.parse(startFrom + " 00:00:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                fromDateTime = LocalDateTime.parse(startFrom,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            builder.and(cpCurrentTx.startTime.after(fromDateTime));
        }

        if (startTo != null && !startTo.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime toDateTime;
            if (startTo.length() <= 10) { // yyyy-MM-dd 형식인 경우
                toDateTime = LocalDateTime.parse(startTo + " 23:59:59",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                toDateTime = LocalDateTime.parse(startTo,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            builder.and(cpCurrentTx.startTime.before(toDateTime));
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
                    builder.and(cpCurrentTx.id.chargerId.containsIgnoreCase(searchContent));
                    break;
                default:
                    break;
            }
        }

        return queryFactory.select(Projections.fields(CurrentChargingListDto.class,
                csInfo.company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                csInfo.id.as("stationId"),
                cpCurrentTx.id.chargerId.as("chargerId"),
                cpCurrentTx.id.connectorId.as("connectorId"),
                cpCurrentTx.startTime.as("chgStartTime"),
                member.name.as("memberName"),
                cpCurrentTx.idTag.as("memberCardNo"),
                cpCurrentTx.chargePower.as("chgAmount"),
                cpCurrentTx.remainStopTs.as("remainTime"),
                cpCurrentTx.soc.as("soc"),
                cpCurrentTx.transactionId.as("transactionId")))
                .from(cpCurrentTx)
                .leftJoin(cpInfo).on(cpCurrentTx.id.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .join(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(member).on(cpCurrentTx.idTag.eq(member.idTag))
                .where(builder)
                .orderBy(cpCurrentTx.startTime.desc())
                .fetch();

    }

}
