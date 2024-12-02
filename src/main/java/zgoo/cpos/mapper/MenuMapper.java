package zgoo.cpos.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import zgoo.cpos.domain.menu.Menu;
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
                        .menuLv(entity.getMenuLv())
                        .iconClass(entity.getIconClass())
                        .useYn(entity.getUseYn())
                        .regDt(entity.getRegDt())
                        .modDt(entity.getModDt())
                        .build();

        return dto;
    }
    /*
     * Entity list >> DTO list
     */
    // public static List<MenuDto.MenuRegDto> toDtoList(List<Menu> entitylist) {
    //     // List<MenuDto.MenuRegDto> dtolist = entitylist.stream()
    //     //             .map(menu -> MenuDto.MenuRegDto.builder()
    //     //                 .menuId(menu.getMenuId())
    //     //                 .menuName(menu.getMenuName())
    //     //                 .menuLv(menu.getMenuLv())
    //     //                 .parentCode(menu.getParentMenu())
    //     //                 .useYn(menu.getUseYn())
    //     //                 .regDt(menu.getRegDt())
    //     //                 .modDt(menu.getModDt())
    //     //                 .build())
    //     //             .collect(Collectors.toList());
        
    //     // return dtolist;

    //     return entitylist.stream()
    //             .map(menu -> MenuDto.MenuRegDto.builder()
    //                 .menuId(menu.getMenuId())
    //                 .menuName(menu.getMenuName())
    //                 .menuLv(menu.getMenuLv())
    //                 .parentCode(menu.getParentMenu())
    //                 .useYn(menu.getUseYn())
    //                 .regDt(menu.getRegDt())
    //                 .modDt(menu.getModDt())
    //                 .build())
    //             .collect(Collectors.toList());
    // }

    // public static List<MenuDto.MenuListDto> toDtoList(List<Menu> entitylist) {
    //     return entitylist.stream()
    //             .map(menu -> 
    //                 MenuDto.MenuListDto.builder()
    //                 .menuCode(menu.getMenuCode())
    //                 .parentCode(menu.getParentCode())
    //                 .menuUrl(menu.getMenuUrl())
    //                 .menuName(menu.getMenuName())
    //                 .menuLv(menu.getMenuLv())
    //                 .useYn(menu.getUseYn())
    //                 .build())
    //             .collect(Collectors.toList());
    // }


    public static List<MenuDto.MenuListDto> toDtoList(List<Menu> entityList) {
        return entityList.stream()
                .map(menu -> {
                    // menuLv에 따른 이름 매핑
                    String menuLvName;
                    switch (menu.getMenuLv()) {
                        case "0":
                            menuLvName = "상위메뉴";
                            break;
                        case "1":
                            menuLvName = "중위메뉴";
                            break;
                        case "2":
                            menuLvName = "하위메뉴";
                            break;
                        default:
                            menuLvName = "기타";
                            break;
                    }
    
                    return MenuDto.MenuListDto.builder()
                        .menuCode(menu.getMenuCode())
                        .parentCode(menu.getParentCode())
                        .menuUrl(menu.getMenuUrl())
                        .menuName(menu.getMenuName())
                        .menuLv(menu.getMenuLv())
                        .menuLvName(menuLvName)  // menuLv에 따른 이름 설정
                        .iconClass(menu.getIconClass())
                        .useYn(menu.getUseYn())
                        .build();
                })
                .collect(Collectors.toList());
    }    
}
