package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.cp.CpMaintain;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainRegDto;

public class CpMaintainMapper {

    /* 
     * dto >> entity
     */
    public static CpMaintain toEntity(CpMaintainRegDto dto, String regUserId) {
        CpMaintain cpMaintain = CpMaintain.builder()
                .chargerId(dto.getChargerId())
                .regDt(LocalDateTime.now())
                .errorType(dto.getErrorType())
                .errorContent(dto.getErrorContent())
                .pictureLoc1(dto.getPictureLoc1())
                .pictureLoc2(dto.getPictureLoc2())
                .pictureLoc3(dto.getPictureLoc3())
                .processDate(dto.getProcessDate())
                .processStatus(dto.getProcessStatus())
                .processContent(dto.getProcessContent())
                .regUserId(regUserId)
                .build();
        return cpMaintain;
    }
}
