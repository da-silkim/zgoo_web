package zgoo.cpos.repository.charger;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.charger.QConnectorStatus;
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusDto;

@RequiredArgsConstructor
@Slf4j
public class ConnectorStatusRepositoryCustomImpl implements ConnectorStatusRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QConnectorStatus conn = QConnectorStatus.connectorStatus;
    QCpInfo cpInfo = QCpInfo.cpInfo;

    @Override
    public List<ConnectorStatusDto> findAllConnectorStatusList() {
        return queryFactory.select(Projections.fields(ConnectorStatusDto.class,
                conn.id.chargerId.as("chargerId"),
                conn.id.connectorId.as("connectorId"),
                conn.status.as("status")))
                .from(conn)
                .orderBy(conn.id.connectorId.asc())
                .fetch();
    }

}
