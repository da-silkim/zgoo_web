package zgoo.cpos.repository.menu;

import java.util.List;
import java.util.Set;

import zgoo.cpos.domain.menu.Menu;
import zgoo.cpos.dto.menu.MenuDto;

public interface MenuRepositoryCustom {

    // 메뉴명 조회
    String findMenuCode(String menucode, String menulv);

    // 메뉴의 자식 개수 조회
    List<MenuDto.MenuListDto> getMuenListWithChildCount();

    // 부모코드명 포함 전체 메뉴 조회
    List<MenuDto.MenuAuthorityListDto> findMenuListWithParentName();

    // 메뉴레벨별 메뉴 조회
    List<Menu> findByMenuLv(String menuLv);
    
    // 메뉴 단건 조회
    Menu findMenuOne(String menucode);

    // 메뉴 삭제
    Long deleteMenuOne(String menucode);

    List<Menu> findByMenuCodeIn(Set<String> menuCodes);
    List<Menu> findByParentCode(String parentCode);
}
