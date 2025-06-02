package zgoo.cpos.repository.charger;

import java.util.Collections;
import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.charger.QConnectorStatus;
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.domain.charger.QCpStatus;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusCountDto;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusDto;
import zgoo.cpos.util.QueryUtils;

@RequiredArgsConstructor
@Slf4j
public class ConnectorStatusRepositoryCustomImpl implements ConnectorStatusRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QConnectorStatus conn = QConnectorStatus.connectorStatus;
    QCpInfo cpInfo = QCpInfo.cpInfo;
    QCpStatus cpStatus = QCpStatus.cpStatus;
    QCompany company = QCompany.company;
    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
    QCsInfo csInfo = QCsInfo.csInfo;

    @Override
    public List<ConnectorStatusDto> findConnectorStatusByChargerIds(List<String> chargerIds) {
        if (chargerIds == null || chargerIds.isEmpty()) {
            return Collections.emptyList();
        }
        return queryFactory.select(Projections.fields(ConnectorStatusDto.class,
                conn.id.chargerId.as("chargerId"),
                conn.id.connectorId.as("connectorId"),
                conn.status.as("status"),
                cpStatus.connectionYn.as("connectionYn")))
                .from(conn)
                .leftJoin(cpStatus)
                .on(conn.id.chargerId.eq(cpStatus.chargerId))
                .where(conn.id.chargerId.in(chargerIds))
                .orderBy(conn.id.connectorId.asc())
                .fetch();
    }

    @Override
    public List<ConnectorStatusDto> findConnectorStatusByChargerId(String chargerId) {
        return queryFactory.select(Projections.fields(ConnectorStatusDto.class,
                conn.id.chargerId.as("chargerId"),
                conn.id.connectorId.as("connectorId"),
                conn.status.as("status"),
                cpStatus.connectionYn.as("connectionYn")))
                .from(conn)
                .leftJoin(cpStatus)
                .on(conn.id.chargerId.eq(cpStatus.chargerId))
                .orderBy(conn.id.connectorId.asc())
                .where(conn.id.chargerId.eq(chargerId))
                .fetch();
    }

    @Override
    public ConnectorStatusCountDto getConnectorStatusCount(String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        // CpStatus 엔티티에 대한 별칭 추가
        QCpStatus cpStatus = QCpStatus.cpStatus;

        // 기본 쿼리 구성
        JPAQuery<ConnectorStatusCountDto> query = queryFactory.select(Projections.fields(
                ConnectorStatusCountDto.class,
                Expressions.numberTemplate(Long.class,
                        "COUNT(CASE WHEN {0} = 'N' THEN 1 END)",
                        cpStatus.connectionYn).as("disconnectedCount"),
                Expressions.numberTemplate(Long.class,
                        "COUNT(CASE WHEN {0} = 'Y' AND {1} = 'Available' THEN 1 END)",
                        cpStatus.connectionYn, conn.status).as("availableCount"),
                Expressions.numberTemplate(Long.class,
                        "COUNT(CASE WHEN {0} = 'Y' AND {1} = 'Preparing' THEN 1 END)",
                        cpStatus.connectionYn, conn.status).as("preparingCount"),
                Expressions.numberTemplate(Long.class,
                        "COUNT(CASE WHEN {0} = 'Y' AND {1} = 'Charging' THEN 1 END)",
                        cpStatus.connectionYn, conn.status).as("chargingCount"),
                Expressions.numberTemplate(Long.class,
                        "COUNT(CASE WHEN {0} = 'Y' AND {1} = 'SuspendedEV' THEN 1 END)",
                        cpStatus.connectionYn, conn.status).as("suspendedEvCount"),
                Expressions.numberTemplate(Long.class,
                        "COUNT(CASE WHEN {0} = 'Y' AND {1} = 'SuspendedEVSE' THEN 1 END)",
                        cpStatus.connectionYn, conn.status).as("suspendedEvseCount"),
                Expressions.numberTemplate(Long.class,
                        "COUNT(CASE WHEN {0} = 'Y' AND {1} = 'Finishing' THEN 1 END)",
                        cpStatus.connectionYn, conn.status).as("finishingCount"),
                Expressions.numberTemplate(Long.class,
                        "COUNT(CASE WHEN {0} = 'Y' AND {1} = 'Reserved' THEN 1 END)",
                        cpStatus.connectionYn, conn.status).as("reservedCount"),
                Expressions.numberTemplate(Long.class,
                        "COUNT(CASE WHEN {0} = 'Y' AND {1} = 'Unavailable' THEN 1 END)",
                        cpStatus.connectionYn, conn.status).as("unavailableCount"),
                Expressions.numberTemplate(Long.class,
                        "COUNT(CASE WHEN {0} = 'Y' AND {1} = 'Faulted' THEN 1 END)",
                        cpStatus.connectionYn, conn.status).as("faultedCount")))
                .from(conn)
                .join(cpStatus).on(conn.id.chargerId.eq(cpStatus.chargerId)) // CP_STATUS 테이블과 조인
                .join(cpInfo).on(conn.id.chargerId.eq(cpInfo.id))
                .join(csInfo).on(cpInfo.stationId.eq(csInfo))
                .join(company).on(csInfo.company.eq(company))
                .join(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder);

        return query.fetchOne();
    }
}
