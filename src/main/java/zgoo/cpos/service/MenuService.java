package zgoo.cpos.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.menu.Menu;
import zgoo.cpos.dto.menu.MenuDto;
import zgoo.cpos.mapper.MenuMapper;
import zgoo.cpos.repository.menu.MenuRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;

    // 메뉴 - 전체 조회
    public List<MenuDto.MenuListDto> findMenuList() {
        try {
            List<Menu> menuList= this.menuRepository.findAll();

            if (menuList.isEmpty()) {
                log.info("=== no menu found ===");
                return new ArrayList<>();
            }
            List<MenuDto.MenuListDto> menuListDto = MenuMapper.toDtoList(menuList);
            return menuListDto;
        } catch (Exception e) {
            log.error("[findMenuList] 메뉴 리스트 조회 중 오류 발생: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 메뉴 - 전체 조회(자식 메뉴 개수, 메뉴 레벨명 추가)
    public List<MenuDto.MenuListDto> findMenuListWithChild() {
        try {
            List<MenuDto.MenuListDto> menuListDto =this.menuRepository.getMuenListWithChildCount();
            return menuListDto;
        } catch (Exception e) {
            log.error("[findMenuListWithChild] 메뉴 리스트 조회 중 오류 발생: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 메뉴 - 단건 조회
    public MenuDto.MenuRegDto findMenuOne(String menuCode) {
        try {
            Menu menu = this.menuRepository.findMenuOne(menuCode);
            return MenuMapper.toDto(menu);
        } catch (Exception e) {
            log.error("[findMenuOne] 메뉴 조회 중 오류 발생: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // 메뉴 - 메뉴레벨별 조회
    public List<MenuDto.MenuRegDto> getParentMenuByMenuLv(String menuLv) {
        try {
            List<Menu> menuList = this.menuRepository.findByMenuLv(menuLv);
            return menuList.stream()
                        .map(MenuMapper::toDto)
                        .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[getParentMenuByMenuLv] 부모 메뉴 조회 중 오류 발생: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 메뉴 - 등록
    public void saveMenu(MenuDto.MenuRegDto dto) {
        try {
            Menu menu = MenuMapper.toEntity(dto);
            this.menuRepository.save(menu);
        } catch (Exception e) {
            log.error("[saveMenu] 메뉴 등록 중 오류 발생: {}", e.getMessage());
        }
    }

    // 메뉴 - 수정
    @Transactional
    public MenuDto.MenuRegDto updateMenu(MenuDto.MenuRegDto dto) {
        try {
            Menu menu = this.menuRepository.findMenuOne(dto.getMenuCode());

            log.info("=== before update: {}", menu.toString());

            menu.updateMenuInfo(dto);
            
            log.info("=== after update: {}", menu.toString());

            return MenuMapper.toDto(menu);
        } catch (Exception e) {
            log.error("[updateMenu] 메뉴 업데이트 중 오류 발생: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void deleteMenu(String menuCode) {
        Long count = this.menuRepository.deleteMenuOne(menuCode);
        log.info("=== delete menu info: {}", count);
    }
}
