package zgoo.cpos.repository.cpmodel;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.cp.CpConnector;
import zgoo.cpos.domain.cp.QCpConnector;
import zgoo.cpos.dto.cp.CpModelDto.CpConnectorDto;

@Slf4j
@RequiredArgsConstructor
public class CpConnectorRepositoryCustomImpl implements CpConnectorRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCpConnector connector = QCpConnector.cpConnector;
    QCommonCode connectorTypeName = new QCommonCode("connectorType");

    @Override
    public List<CpConnectorDto> findAllByModelIdDto(Long modelId) {
        return queryFactory.select(Projections.fields(CpConnectorDto.class,
            connector.connectorId.as("connectorId"),
            connector.connectorType.as("connectorType"),
            connectorTypeName.name.as("connectorTypeName")))
            .from(connector)
            .leftJoin(connectorTypeName).on(connector.connectorType.eq(connectorTypeName.commonCode))
            .where(connector.cpModel.id.eq(modelId))
            .fetch();
    }

    @Override
    public List<CpConnector> findAllByModelId(Long modelId) {
        return queryFactory
            .selectFrom(connector)
            .where(connector.cpModel.id.eq(modelId))
            .fetch();
    }

    @Override
    public void deleteAllByModelId(Long modelId) {
        queryFactory
            .delete(connector)
            .where(connector.cpModel.id.eq(modelId))
            .execute();
    }
}
