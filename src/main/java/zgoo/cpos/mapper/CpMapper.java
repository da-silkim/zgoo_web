package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.charger.CpInfo;
import zgoo.cpos.domain.charger.CpModem;
import zgoo.cpos.domain.company.CpPlanPolicy;
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.dto.cp.ChargerDto.ChargerRegDto;

public class CpMapper {
    /*
     * dto >> entity
     */
    public static CpInfo toEntity(ChargerRegDto dto, CsInfo csInfo, CpModem modem, CpPlanPolicy plan) {
        CpInfo entity = CpInfo.builder()
                .id(dto.getChargerId())
                .stationId(csInfo)
                .cpmodemInfo(modem)
                .planInfo(plan)
                .chargerName(dto.getChargerName())
                .serialNo(dto.getChgSerialNo())
                .fwVersion(dto.getFwversion())
                .commonType(dto.getCommonType())
                .anydeskId(dto.getAnydeskId())
                .installDate(dto.getInstallDate())
                .regDt(LocalDateTime.now())
                .protocol("OCPP16")
                .location(dto.getLocation())
                .modelCode(dto.getModelCode())
                .useYn(dto.getUseYn())
                .reason(dto.getReason())
                .build();

        return entity;
    }
}
