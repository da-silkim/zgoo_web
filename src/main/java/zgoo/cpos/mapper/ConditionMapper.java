package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.member.ConditionCode;
import zgoo.cpos.domain.member.ConditionVersionHist;
import zgoo.cpos.dto.member.ConditionDto.ConditionCodeBaseDto;
import zgoo.cpos.dto.member.ConditionDto.ConditionVersionHistBaseDto;

public class ConditionMapper {

    /* 
     * condition code(dto >> entity)
     */
    public static ConditionCode toEntityConditionCode(ConditionCodeBaseDto dto) {
        ConditionCode condition = ConditionCode.builder()
                .conditionCode(dto.getConditionCode())
                .conditionName(dto.getConditionName())
                .section(dto.getSection())
                .regDt(LocalDateTime.now())
                .build();
        return condition;
    }

    /* 
     * condition version hist(dto >> entity)
     */
    public static ConditionVersionHist toEntityConditionHist(ConditionVersionHistBaseDto dto, ConditionCode condition) {
        ConditionVersionHist conditionHist = ConditionVersionHist.builder()
                .conditionCode(condition)
                .version(dto.getVersion())
                .filePath(dto.getFilePath())
                .originalName(dto.getOriginalName())
                .storedName(dto.getStoredName())
                .memo(dto.getMemo())
                .regDt(LocalDateTime.now())
                .applyDt(dto.getApplyDt())
                .applyYn("N")
                .build();
        return conditionHist;
    }
}
