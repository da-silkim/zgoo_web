package zgoo.cpos.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;

import zgoo.cpos.dto.authority.MenuAuthorityDto;

public class MenuAuthorityMapper {
    /*
     * Entity >> Dto (사업자 권한 리스트)
     */
    public static List<MenuAuthorityDto.CompanyAuthorityListDto> toComListDto(List<Tuple> tupleList) {
        List<MenuAuthorityDto.CompanyAuthorityListDto> dtolist = tupleList.stream()
                .map(tuple -> MenuAuthorityDto.CompanyAuthorityListDto.builder()
                        .companyId(tuple.get(0, Long.class))
                        .companyName(tuple.get(1, String.class))
                        .authority(tuple.get(2, String.class))
                        .authorityName(tuple.get(3, String.class))
                        .build())
                .collect(Collectors.toList());

        return dtolist;
    }
}
