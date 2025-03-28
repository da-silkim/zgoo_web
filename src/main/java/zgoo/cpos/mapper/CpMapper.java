package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.charger.CpInfo;
import zgoo.cpos.domain.charger.CpModem;
import zgoo.cpos.domain.company.CpPlanPolicy;
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.dto.cp.ChargerDto.ChargerDetailListDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerRegDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelListDto;

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

    public static ChargerDetailListDto toDetailDto(CpInfo entity, CpModelListDto cpModel) {
        ChargerDetailListDto dto = ChargerDetailListDto.builder()
                .companyId(entity.getStationId().getCompany().getId())
                .companyName(entity.getStationId().getCompany().getCompanyName())
                .stationId(entity.getStationId().getId())
                .stationName(entity.getStationId().getStationName())
                .chargerName(entity.getChargerName())
                .chargerId(entity.getId())
                .modelCode(entity.getModelCode())
                .cpType(cpModel.getCpTypeName())
                .manufCd(cpModel.getManufCd())
                .manufCdName(cpModel.getManufCdName())
                .chgKw(cpModel.getPowerUnit())
                .chgSerialNo(entity.getSerialNo())
                .fwversion(entity.getFwVersion())
                .anydeskId(entity.getAnydeskId())
                .commonType(entity.getCommonType())
                .installDate(entity.getInstallDate())
                .pricePolicyId(entity.getPlanInfo().getId())
                .cpPlanName(entity.getPlanInfo().getName())
                .useYn(entity.getUseYn())
                .reason(entity.getReason())
                .location(entity.getLocation())
                .dualYn(cpModel.getDualYn())
                .modemSerialNo(entity.getCpmodemInfo().getSerialNo())
                .modemNo(entity.getCpmodemInfo().getModemNo())
                .modemContractStart(entity.getCpmodemInfo().getContractStart())
                .modemContractEnd(entity.getCpmodemInfo().getContractEnd())
                .modemPricePlan(entity.getCpmodemInfo().getPricePlan())
                .modemdataCapacity(entity.getCpmodemInfo().getDataCapacity())
                .modemTelCorp(entity.getCpmodemInfo().getTelCorp())
                .modemModelName(entity.getCpmodemInfo().getModelName())
                .modemContractStatus(entity.getCpmodemInfo().getContractStatus())
                .build();

        return dto;
    }
}
