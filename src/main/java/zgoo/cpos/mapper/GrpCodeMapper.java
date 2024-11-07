package zgoo.cpos.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import zgoo.cpos.domain.code.GrpCode;
import zgoo.cpos.dto.code.CodeDto;

public class GrpCodeMapper {
        /*
         * Dto >> Entity
         */
        public static GrpCode toEntity(CodeDto.GrpCodeDto dto) {
                GrpCode grpCode = GrpCode.builder()
                                .grpCode(dto.getGrpCode())
                                .grpcdName(dto.getGrpcdName())
                                .regUserId(dto.getRegUserId())
                                .regDt(LocalDateTime.now())
                                .modUserId(dto.getModUserId())
                                .modDt(dto.getModDt())
                                .build();

                return grpCode;
        }

        /*
         * Dto List >> Entity List
         */
        public static List<GrpCode> toEntityList(List<CodeDto.GrpCodeDto> dtolist) {
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
        public static CodeDto.GrpCodeDto toDto(GrpCode entity) {
                CodeDto.GrpCodeDto dto = CodeDto.GrpCodeDto.builder()
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
        public static List<CodeDto.GrpCodeDto> toDtoList(List<GrpCode> entitylist) {
                List<CodeDto.GrpCodeDto> dtolist = entitylist.stream()
                                .map(grpinfo -> CodeDto.GrpCodeDto.builder().grpCode(grpinfo.getGrpCode())
                                                .grpcdName(grpinfo.getGrpcdName())
                                                .regUserId(grpinfo.getRegUserId())
                                                .regDt(grpinfo.getRegDt())
                                                .modUserId(grpinfo.getModUserId())
                                                .modDt(grpinfo.getModDt())
                                                .build())
                                .collect(Collectors.toList());

                return dtolist;
        }

}
