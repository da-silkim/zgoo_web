package zgoo.cpos.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import zgoo.cpos.domain.code.CommonCode;
import zgoo.cpos.domain.code.GrpCode;
import zgoo.cpos.dto.code.CodeDto;

public class CommonCodeMapper {
    /*
     * Dto >> Entity
     */
    public static CommonCode toEntity(CodeDto.CommCodeDto dto, GrpCode grp) {
        CommonCode cen = CommonCode.builder()
                .commonCode(dto.getCommonCode())
                .name(dto.getCommonCodeName())
                .nameEn(dto.getCommonCodeNameEn())
                .reserved(dto.getReserved())
                .refCode1(dto.getRefCode1())
                .refCode2(dto.getRefCode2())
                .refCode3(dto.getRefCode3())
                .regUserId(dto.getRegUserId())
                .regDt(LocalDateTime.now())
                .modUserId(dto.getModUserId())
                .modDt(dto.getModDt())
                .group(grp)
                .build();

        return cen;
    }

    /*
     * Dto List >> Entity List
     */
    public static List<CommonCode> toEntityList(List<CodeDto.CommCodeDto> dtolist) {
        return null;
        // List<CommonCode> entityList = dtolist.stream()
        // .map(common -> new CommonCdDto(common.getId(),
        // common.getName(),
        // common.getReserved(),
        // common.getRefCode1(),
        // common.getRefCode2(),
        // common.getRefCode3(),
        // common.getRegUserId(),
        // common.getRegDt(),
        // common.getModUserId(),
        // common.getModDt(),
        // common.getg))
        // .collect(Collectors.toList());

        // return entityList;
    }

    /*
     * Entity >> Dto
     */
    public static CodeDto.CommCodeDto toDto(CommonCode entity) {
        CodeDto.CommCodeDto dto = CodeDto.CommCodeDto.builder()
                .grpCode(entity.getGroup().getGrpCode())
                .commonCode(entity.getCommonCode())
                .commonCodeName(entity.getName())
                .commonCodeNameEn(entity.getNameEn())
                .reserved(entity.getReserved())
                .refCode1(entity.getRefCode1())
                .refCode2(entity.getRefCode2())
                .refCode3(entity.getRefCode3())
                .regUserId(entity.getRegUserId())
                .regDt(entity.getRegDt())
                .modUserId(entity.getModUserId())
                .modDt(entity.getModDt())
                .build();

        return dto;
    }

    /*
     * Entity List >> Dto list
     */
    public static List<CodeDto.CommCodeDto> toDtoList(List<CommonCode> entitylist) {
        List<CodeDto.CommCodeDto> dtolist = entitylist.stream()
                .map(eninfo -> CodeDto.CommCodeDto.builder()
                        .grpCode(eninfo.getGroup().getGrpCode())
                        .commonCode(eninfo.getCommonCode())
                        .commonCodeName(eninfo.getName())
                        .commonCodeNameEn(eninfo.getNameEn())
                        .reserved(eninfo.getReserved())
                        .refCode1(eninfo.getRefCode1())
                        .refCode2(eninfo.getRefCode2())
                        .refCode3(eninfo.getRefCode3())
                        .regUserId(eninfo.getRegUserId())
                        .regDt(eninfo.getRegDt())
                        .modUserId(eninfo.getModUserId())
                        .modDt(eninfo.getModDt())
                        .build())
                .collect(Collectors.toList());

        return dtolist;
    }
}
