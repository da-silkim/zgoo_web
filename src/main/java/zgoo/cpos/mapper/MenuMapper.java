package zgoo.cpos.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import zgoo.cpos.domain.menu.CompanyMenuAuthority;
import zgoo.cpos.domain.menu.Menu;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto;
import zgoo.cpos.dto.menu.MenuDto;

public class MenuMapper {

    /*
     * Dto >> Entity
     */
    public static Menu toEntity(MenuDto.MenuRegDto dto) {
        Menu menu = Menu.builder()
                .menuCode(dto.getMenuCode())
                .parentCode(dto.getParentCode())
                .menuUrl(dto.getMenuUrl())
                .menuName(dto.getMenuName())
                .menuLv(dto.getMenuLv())
                .iconClass(dto.getIconClass())
                .useYn(dto.getUseYn())
                .regDt(LocalDateTime.now())
                .modDt(dto.getModDt())
                .build();
        return menu;
    }

    /*
     * Entity >> Dto
     */
    public static MenuDto.MenuRegDto toDto(Menu entity) {
        MenuDto.MenuRegDto dto = MenuDto.MenuRegDto.builder()
                .menuCode(entity.getMenuCode())
                .parentCode(entity.getParentCode())
                .menuUrl(entity.getMenuUrl())
                .menuName(entity.getMenuName())
                .menuNameEn(entity.getMenuNameEn())
                .menuLv(entity.getMenuLv())
                .iconClass(entity.getIconClass())
                .useYn(entity.getUseYn())
                .regDt(entity.getRegDt())
                .modDt(entity.getModDt())
                .build();

        return dto;
    }

    /*
     * Entity(list) >> DTO(list)
     */
    public static List<MenuDto.MenuListDto> toDtoList(List<Menu> entityList, MessageSource messageSource) {
        return entityList.stream()
                .map(menu -> {
                    // menuLv에 따른 이름 매핑
                    String menuLvName;
                    switch (menu.getMenuLv()) {
                        case "0":
                            menuLvName = messageSource.getMessage("menuManagement.modal.menuLv.options.top", null,
                                    LocaleContextHolder.getLocale());
                            break;
                        case "1":
                            menuLvName = messageSource.getMessage("menuManagement.modal.menuLv.options.mid", null,
                                    LocaleContextHolder.getLocale());
                            break;
                        case "2":
                            menuLvName = messageSource.getMessage("menuManagement.modal.menuLv.options.low", null,
                                    LocaleContextHolder.getLocale());
                            break;
                        default:
                            menuLvName = messageSource.getMessage("menuManagement.modal.menuLv.options.etc", null,
                                    LocaleContextHolder.getLocale());
                            break;
                    }

                    return MenuDto.MenuListDto.builder()
                            .menuCode(menu.getMenuCode())
                            .parentCode(menu.getParentCode())
                            .menuUrl(menu.getMenuUrl())
                            .menuName(menu.getMenuName())
                            .menuNameEn(menu.getMenuNameEn())
                            .menuLv(menu.getMenuLv())
                            .menuLvName(menuLvName) // menuLv에 따른 이름 설정
                            .iconClass(menu.getIconClass())
                            .useYn(menu.getUseYn())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /*
     * company menu(dto >> entity)
     */
    public static CompanyMenuAuthority toEntityCompanyMenu(CompanyMenuAuthorityDto.CompanyMenuAuthorityBaseDto dto) {
        CompanyMenuAuthority companyMenuAuthority = CompanyMenuAuthority.builder()
                .companyId(dto.getCompanyId())
                .menuCode(dto.getMenuCode())
                .useYn(dto.getUseYn())
                .build();

        return companyMenuAuthority;
    }

    /*
     * company menu(dto >> entity)
     */
    public static CompanyMenuAuthority toEntityCompanyMenu(Long companyId, MenuDto.MenuRegDto menuDto) {
        CompanyMenuAuthority companyMenuAuthority = CompanyMenuAuthority.builder()
                .companyId(companyId)
                .menuCode(menuDto.getMenuCode())
                .useYn(menuDto.getUseYn())
                .build();

        return companyMenuAuthority;
    }
}
