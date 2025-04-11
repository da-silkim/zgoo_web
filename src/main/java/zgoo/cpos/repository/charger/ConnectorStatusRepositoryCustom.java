package zgoo.cpos.repository.charger;

import java.util.List;

import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusCountDto;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusDto;

public interface ConnectorStatusRepositoryCustom {
    List<ConnectorStatusDto> findAllConnectorStatusList();

    List<ConnectorStatusDto> findConnectorStatusByChargerId(String chargerId);

    ConnectorStatusCountDto getConnectorStatusCount();
}
