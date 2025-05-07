package zgoo.cpos.repository.charger;

import java.util.List;

import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusCountDto;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusDto;

public interface ConnectorStatusRepositoryCustom {
    List<ConnectorStatusDto> findConnectorStatusByChargerIds(List<String> chargerIds);

    List<ConnectorStatusDto> findConnectorStatusByChargerId(String chargerId);

    ConnectorStatusCountDto getConnectorStatusCount(String levelPath, boolean isSuperAdmin);
}
