package zgoo.cpos.repository.menu;

import java.util.List;

import com.querydsl.core.Tuple;

import zgoo.cpos.domain.menu.MenuAuthority;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityListDto;

public interface MenuAuthorityRepositoryCustom {
    // 사업자 권한 리스트 조회
    List<Tuple> companyAuthorityList();

    // 메뉴권한접근 조회
    List<MenuAuthorityListDto> findMenuAuthorityList(Long companyId, String authority);
    List<MenuAuthorityListDto> defaultMenuAuthorityList();

    // 메뉴접근권한이 저장되어 있는지 확인
    Long menuAuthorityRegCheck(Long companyId, String authority);

    // 메뉴 단건의 권한
    MenuAuthority findMenuAuthorityOne(Long companyId, String authority, String menuCode);

    //
    MenuAuthorityBaseDto findUserMenuAuthority(Long companyId, String authority, String menuCode);
}
