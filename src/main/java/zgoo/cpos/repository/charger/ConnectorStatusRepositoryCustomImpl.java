package zgoo.cpos.repository.charger;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.charger.QConnectorStatus;
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.domain.charger.QCpStatus;
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
}
