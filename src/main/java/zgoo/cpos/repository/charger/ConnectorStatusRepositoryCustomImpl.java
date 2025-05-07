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

        // 기본 쿼리 구성
        JPAQuery<ConnectorStatusCountDto> query = queryFactory.select(Projections.fields(ConnectorStatusCountDto.class,
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'Available' THEN 1 END)",
                        conn.status).as("availableCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'Preparing' THEN 1 END)",
                        conn.status).as("preparingCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'Charging' THEN 1 END)",
                        conn.status).as("chargingCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'SuspendedEV' THEN 1 END)",
                        conn.status).as("suspendedEvCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'SuspendedEVSE' THEN 1 END)",
                        conn.status).as("suspendedEvseCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'Finishing' THEN 1 END)",
                        conn.status).as("finishingCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'Reserved' THEN 1 END)",
                        conn.status).as("reservedCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'Unavailable' THEN 1 END)",
                        conn.status).as("unavailableCount"),
                Expressions.numberTemplate(Long.class, "COUNT(CASE WHEN {0} = 'Faulted' THEN 1 END)",
                        conn.status).as("faultedCount")))
                .from(conn);

        // 슈퍼 관리자가 아닌 경우 회사 레벨 경로에 따른 필터링 추가

        query = query.join(cpInfo).on(conn.id.chargerId.eq(cpInfo.id))
                .join(csInfo).on(cpInfo.stationId.eq(csInfo))
                .join(company).on(csInfo.company.eq(company))
                .join(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder);

        return query.fetchOne();
    }
}
