package zgoo.cpos.repository.cpmodel;

import java.util.List;

import zgoo.cpos.domain.cp.CpConnector;
import zgoo.cpos.dto.cp.CpModelDto.CpConnectorDto;

public interface CpConnectorRepositoryCustom {
    List<CpConnectorDto> findAllByModelIdDto(Long modelId);
    List<CpConnector> findAllByModelId(Long modelId);
    void deleteAllByModelId(Long modelId);
}
