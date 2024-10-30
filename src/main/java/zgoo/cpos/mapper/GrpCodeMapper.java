package zgoo.cpos.mapper;

import java.util.List;
import java.util.stream.Collectors;

import zgoo.cpos.domain.GrpCode;
import zgoo.cpos.dto.code.GrpCodeDto;

public class GrpCodeMapper {
    /*
     * Dto >> Entity111
     */
    public static GrpCode toEntity(GrpCodeDto dto) {
        GrpCode grpCode = GrpCode.builder()
                .grpCode(dto.getGrpCode())
                .grpcdName(dto.getGrpcdName())
                .regUserId(dto.getRegUserId())
                .regDt(dto.getRegDt())
                .modUserId(dto.getModUserId())
                .modDt(dto.getModDt())
                .build();

        return grpCode;
    }

    /*
     * Dto List >> Entity List
     */
    public static List<GrpCode> toEntityList(List<GrpCodeDto> dtolist) {
        List<GrpCode> entityList = dtolist.stream()
                .map(grp -> new GrpCode(grp.getGrpCode(),
                        grp.getGrpcdName(),
                        grp.getRegUserId(),
                        grp.getRegDt(),
                        grp.getModUserId(),
                        grp.getModDt()))
                .collect(Collectors.toList());

        return entityList;
    }

    /*
     * Entity >> Dto
     */
    public static GrpCodeDto toDto(GrpCode entity) {
        GrpCodeDto dto = GrpCodeDto.builder()
                .grpCode(entity.getGrpCode())
                .grpcdName(entity.getGrpcdName())
                .modDt(entity.getModDt())
                .modUserId(entity.getModUserId())
                .regDt(entity.getRegDt())
                .regUserId(entity.getRegUserId())
                .build();

        return dto;
    }

    /*
     * Entity List >> Dto list
     */
    public static List<GrpCodeDto> toDtoList(List<GrpCode> entitylist) {
        List<GrpCodeDto> dtolist = entitylist.stream()
                .map(grpinfo -> new GrpCodeDto(grpinfo.getGrpCode(),
                        grpinfo.getGrpcdName(),
                        grpinfo.getRegUserId(),
                        grpinfo.getRegDt(),
                        grpinfo.getModUserId(),
                        grpinfo.getModDt()))
                .collect(Collectors.toList());

        return dtolist;
    }

}
