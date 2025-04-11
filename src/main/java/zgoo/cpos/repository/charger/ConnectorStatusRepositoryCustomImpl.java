package zgoo.cpos.repository.charger;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.charger.QConnectorStatus;
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.domain.charger.QCpStatus;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusCountDto;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusDto;

@RequiredArgsConstructor
@Slf4j
public class ConnectorStatusRepositoryCustomImpl implements ConnectorStatusRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QConnectorStatus conn = QConnectorStatus.connectorStatus;
    QCpInfo cpInfo = QCpInfo.cpInfo;
    QCpStatus cpStatus = QCpStatus.cpStatus;

    @Override
    public List<ConnectorStatusDto> findAllConnectorStatusList() {
        return queryFactory.select(Projections.fields(ConnectorStatusDto.class,
                conn.id.chargerId.as("chargerId"),
                conn.id.connectorId.as("connectorId"),
                conn.status.as("status"),
                cpStatus.connectionYn.as("connectionYn")))
                .from(conn)
                .leftJoin(cpStatus)
                .on(conn.id.chargerId.eq(cpStatus.chargerId))
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
    public ConnectorStatusCountDto getConnectorStatusCount() {
        return queryFactory.select(Projections.fields(ConnectorStatusCountDto.class,
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
                .from(conn)
                .fetchOne();
    }
}
