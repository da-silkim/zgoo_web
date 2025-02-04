package zgoo.cpos.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;

import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.menu.Menu;
import zgoo.cpos.domain.menu.MenuAuthority;
import zgoo.cpos.dto.menu.MenuAuthorityDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;

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

    /* 
     * dto >> entity
     */
    public static MenuAuthority toEntity(MenuAuthorityBaseDto dto, Company company, Menu menu) {
        MenuAuthority menuAuthority = MenuAuthority.builder()
                .company(company)
                .menu(menu)
                .authority(dto.getAuthority())
                .modYn(dto.getModYn())
                .readYn(dto.getReadYn())
                .excelYn(dto.getExcelYn())
                .regAt(LocalDateTime.now())
                .build();
        return menuAuthority;
    }
}
