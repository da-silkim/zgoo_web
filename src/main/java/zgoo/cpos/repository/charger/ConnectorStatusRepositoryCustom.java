package zgoo.cpos.repository.charger;

import java.util.List;

import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusDto;

public interface ConnectorStatusRepositoryCustom {
    List<ConnectorStatusDto> findAllConnectorStatusList();
}
