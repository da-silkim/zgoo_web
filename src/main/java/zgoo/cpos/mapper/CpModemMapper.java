package zgoo.cpos.mapper;

import zgoo.cpos.domain.charger.CpModem;
import zgoo.cpos.dto.cp.ChargerDto.ChargerRegDto;

public class CpModemMapper {
    /*
     * dto >> entity
     */
    public static CpModem toEntity(ChargerRegDto dto) {

        CpModem entity = CpModem.builder()
                .modemNo(dto.getModemNo())
                .serialNo(dto.getModemSerialNo())
                .contractStart(dto.getModemContractStart())
                .contractEnd(dto.getModemContractEnd())
                .pricePlan(dto.getModemPricePlan())
                .dataCapacity(dto.getModemdataCapacity())
                .telCorp(dto.getModemTelCorp())
                .modelName(dto.getModemModelName())
                .contractStatus(dto.getModemContractStatus())
                .build();
        return entity;
    }

}
