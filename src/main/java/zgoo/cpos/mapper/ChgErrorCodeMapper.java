package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.code.ChgErrorCode;
import zgoo.cpos.dto.code.ChgErrorCodeDto.ChgErrorCodeRegDto;

public class ChgErrorCodeMapper {

    /* 
     * dto >> entity
     */
    public static ChgErrorCode toEntity(ChgErrorCodeRegDto dto) {
        ChgErrorCode errorCode = ChgErrorCode.builder()
                .errCode(dto.getErrCode())
                .errName(dto.getErrName())
                .menufCode(dto.getMenufCode())
                .regDt(LocalDateTime.now())
                .build();
        return errorCode;
    }
}
