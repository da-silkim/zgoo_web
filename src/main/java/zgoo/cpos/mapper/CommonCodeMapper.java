package zgoo.cpos.mapper;

import java.util.List;
import java.util.stream.Collectors;

import zgoo.cpos.domain.CommonCode;
import zgoo.cpos.domain.GrpCode;
import zgoo.cpos.dto.CommonCdDto;

public class CommonCodeMapper {
    /*
     * Dto >> Entity
     */
    public static CommonCode toEntity(CommonCdDto dto, GrpCode grp) {
        CommonCode cen = CommonCode.builder()
                .id(dto.getId())
                .name(dto.getName())
                .reserved(dto.getReserved())
                .refCode1(dto.getRefCode1())
                .refCode2(dto.getRefCode2())
                .refCode3(dto.getRefCode3())
                .regUserId(dto.getRegUserId())
                .regDt(dto.getRegDt())
                .modUserId(dto.getModUserId())
                .modDt(dto.getModDt())
                .group(grp)
                .build();

        return cen;
    }

    /*
     * Dto List >> Entity List
     */
    public static List<CommonCode> toEntityList(List<CommonCdDto> dtolist) {
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
    public static CommonCdDto toDto(CommonCode entity) {
        CommonCdDto dto = CommonCdDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .reserved(entity.getReserved())
                .refCode1(entity.getRefCode1())
                .refCode2(entity.getRefCode2())
                .refCode3(entity.getRefCode3())
                .regUserId(entity.getRegUserId())
                .regDt(entity.getRegDt())
                .modUserId(entity.getModUserId())
                .modDt(entity.getModDt())
                .group(entity.getGroup())
                .build();

        return dto;
    }

    /*
     * Entity List >> Dto list
     */
    public static List<CommonCdDto> toDtoList(List<CommonCode> entitylist) {
        List<CommonCdDto> dtolist = entitylist.stream()
                .map(eninfo -> new CommonCdDto(
                        eninfo.getId(),
                        eninfo.getName(),
                        eninfo.getReserved(),
                        eninfo.getRefCode1(),
                        eninfo.getRefCode2(),
                        eninfo.getRefCode3(),
                        eninfo.getRegUserId(),
                        eninfo.getRegDt(),
                        eninfo.getModUserId(),
                        eninfo.getModDt(),
                        eninfo.getGroup()))
                .collect(Collectors.toList());

        return dtolist;
    }
}
