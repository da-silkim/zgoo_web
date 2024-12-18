package zgoo.cpos.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.menu.CompanyMenuAuthority;
import zgoo.cpos.domain.menu.Menu;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto;
import zgoo.cpos.dto.menu.MenuDto;
import zgoo.cpos.mapper.MenuMapper;
import zgoo.cpos.repository.menu.CompanyMenuAuthorityRepository;
import zgoo.cpos.repository.menu.MenuRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;
    private final CompanyMenuAuthorityRepository companyMenuAuthorityRepository;

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
            log.error("[findMenuList] error : {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 메뉴 - 전체 조회(부모코드명 추가)
    public List<MenuDto.MenuAuthorityListDto> findMenuListWithParentName() {
        try {
            List<MenuDto.MenuAuthorityListDto> menuList = this.menuRepository.findMenuListWithParentName();
            return menuList;
        } catch (Exception e) {
            log.error("[findMenuListWithParentName] error : {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 메뉴 - 전체 조회(자식 메뉴 개수, 메뉴 레벨명 추가)
    public List<MenuDto.MenuListDto> findMenuListWithChild() {
        try {
            List<MenuDto.MenuListDto> menuListDto =this.menuRepository.getMuenListWithChildCount();
            return menuListDto;
        } catch (Exception e) {
            log.error("[findMenuListWithChild] error : {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 메뉴 - 단건 조회
    public MenuDto.MenuRegDto findMenuOne(String menuCode) {
        try {
            Menu menu = this.menuRepository.findMenuOne(menuCode);
            return MenuMapper.toDto(menu);
        } catch (Exception e) {
            log.error("[findMenuOne] error : {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // 메뉴 - 메뉴레벨별(부모메뉴) 조회
    public List<MenuDto.MenuRegDto> getParentMenuByMenuLv(String menuLv) {
        try {
            List<Menu> menuList = this.menuRepository.findByMenuLv(menuLv);
            return menuList.stream()
                        .map(MenuMapper::toDto)
                        .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[getParentMenuByMenuLv] error : {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 메뉴 등록
    @Transactional
    public void saveMenu(MenuDto.MenuRegDto dto) {
        try {
            Menu menu = MenuMapper.toEntity(dto);
            this.menuRepository.save(menu);

            // 사업장에 새메뉴 추가
            List<Long> companyIds = this.companyMenuAuthorityRepository.findDistinctCompanyIds();
            log.info("companyIds: {}", companyIds);
            if (companyIds != null && !companyIds.isEmpty()) {
                for (Long companyId : companyIds) {
                    CompanyMenuAuthority cma = MenuMapper.toEntityCompanyMenu(companyId, dto);
                    log.info("Found CompanyMenuAuthority for companyId: {}, menuCode: {}, cma: {}", companyId, dto.getMenuCode(), cma);
                    this.companyMenuAuthorityRepository.save(cma);
                    this.companyMenuAuthorityRepository.companyMenuAuthorityUseYnUpdate(cma);
                }
            } else {
                System.out.println("companyIds is null or empty");
            }
        } catch (Exception e) {
            log.error("[saveMenu] error : {}", e.getMessage());
        }
    }

    // 메뉴 수정
    @Transactional
    public MenuDto.MenuRegDto updateMenu(MenuDto.MenuRegDto dto) {
        try {
            Menu menu = this.menuRepository.findMenuOne(dto.getMenuCode());

            log.info("=== before update: {}", menu.toString());

            menu.updateMenuInfo(dto);
            
            log.info("=== after update: {}", menu.toString());

            // 사업장 전체 메뉴 useYn 업데이트
            List<Long> companyIds = this.companyMenuAuthorityRepository.findDistinctCompanyIds();
            log.info("companyIds: {}", companyIds);
            if (companyIds != null && !companyIds.isEmpty()) {
                for (Long companyId : companyIds) {
                    CompanyMenuAuthority cma = this.companyMenuAuthorityRepository.findCompanyMenuAuthorityOne(companyId, dto.getMenuCode());
                    log.info("Found CompanyMenuAuthority for companyId: {}, menuCode: {}, cma: {}", companyId, dto.getMenuCode(), cma);
                    cma.updateCompanyMenuInfoWithMenu(dto.getUseYn());
                    this.companyMenuAuthorityRepository.companyMenuAuthorityUseYnUpdate(cma);
                }
            } else {
                System.out.println("companyIds is null or empty");
            }

            return MenuMapper.toDto(menu);
        } catch (Exception e) {
            log.error("[updateMenu] error : {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(String menuCode) {
        // 메뉴(Master) 삭제 전 사업장별 메뉴 접근 권한에 있는 메뉴 먼저 삭제
        Long cmaCount = this.companyMenuAuthorityRepository.deleteCompanyMenuAuthorityMenuCodeAll(menuCode);
        log.info("=== delete company menu info: {}", cmaCount);

        Long menuCount = this.menuRepository.deleteMenuOne(menuCode);
        log.info("=== delete menu info: {}", menuCount);
    }

    // 사업장별 메뉴 접근 권한 전체 조회
    public Page<CompanyMenuAuthorityDto.CompanyMenuRegDto> findCompanyMenuAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CompanyMenuAuthorityDto.CompanyMenuRegDto> cmaList = this.companyMenuAuthorityRepository.findCompanyMenuWithPagination(pageable);
            return cmaList;
        } catch (DataAccessException dae) {
            log.error("[findCompanyMenuAll] database error : {}", dae.getMessage());
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("[findCompanyMenuAll] error : {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // 사업장별 메뉴 접근 권한 검색 조회
    public Page<CompanyMenuAuthorityDto.CompanyMenuRegDto> searchCompanyMenuWithPagination(String companyName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CompanyMenuAuthorityDto.CompanyMenuRegDto> cmaList = this.companyMenuAuthorityRepository.searchCompanyMenuWithPagination(companyName, pageable);
            return cmaList;
        } catch (DataAccessException dae) {
            log.error("[searchCompanyMenuWithPagination] database error : {}", dae.getMessage());
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("[searchCompanyMenuWithPagination] error : {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // 메뉴권한이 등록된 사업자 조회
    public List<CompanyMenuAuthorityDto.CompanyMenuRegDto> findCompanyDistinctList() {
        try {
            List<CompanyMenuAuthorityDto.CompanyMenuRegDto> cmaList = this.companyMenuAuthorityRepository.findDistinctCompanyWithCompanyName();
            return cmaList;
        } catch (Exception e) {
            log.error("[findCompanyList] error : {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<CompanyMenuAuthorityDto.CompanyMenuAuthorityListDto> findCompanyMenuAuthorityList(Long companyId) {
        try {
            List<CompanyMenuAuthorityDto.CompanyMenuAuthorityListDto> cmaList = this.companyMenuAuthorityRepository.findCompanyMenuAuthorityList(companyId);
            // log.info("companyMenuList 조회: {}", cmaList.toString());
            return cmaList;
        } catch (Exception e) {
            log.error("[findCompanyMenuAuthorityList] error : {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 사업장별 메뉴 권한 등록
    @Transactional
    public int saveCompanyMenuAuthorities(List<CompanyMenuAuthorityDto.CompanyMenuAuthorityBaseDto> menuAuthorites) {
        if (menuAuthorites.isEmpty()) {
            log.error("[saveCompanyMenuAuthorities] list is empty");
            return -1;
        }

        try {
            Long companyId = menuAuthorites.get(0).getCompanyId();
            Long companyMenuIsExis = this.companyMenuAuthorityRepository.companyMenuAuthorityRegCheck(companyId);

            if (companyMenuIsExis > 0) {
                log.info("[saveCompanyMenuAuthorities] companyId exists");
                return 0;
            }

            for (CompanyMenuAuthorityDto.CompanyMenuAuthorityBaseDto dto : menuAuthorites) {
                CompanyMenuAuthority cma = MenuMapper.toEntityCompanyMenu(dto);
                this.companyMenuAuthorityRepository.save(cma);
                this.companyMenuAuthorityRepository.companyMenuAuthorityUseYnUpdate(cma);
            }
            return 1;

        } catch (Exception e) {
            log.error("[saveCompanyMenuAuthorities] error: {}", e.getMessage());
            return -2;
        }
    }

    // 사업장별 메뉴 권한 수정
    @Transactional
    public void updateCompanyMenuAuthority(List<CompanyMenuAuthorityDto.CompanyMenuAuthorityBaseDto> menuAuthorites) {
        try {
            for (CompanyMenuAuthorityDto.CompanyMenuAuthorityBaseDto dto : menuAuthorites) {
                CompanyMenuAuthority cma = this.companyMenuAuthorityRepository.findCompanyMenuAuthorityOne(dto.getCompanyId(), dto.getMenuCode());
                cma.updateCompanyMenuInfo(dto);
                this.companyMenuAuthorityRepository.companyMenuAuthorityUseYnUpdate(cma);
            }
        } catch (Exception e) {
            log.error("[updateCompanyMenuAuthority] error : {}", e.getMessage());
            e.printStackTrace();
        }
    }

    // 사업장별 메뉴 권한 삭제
    @Transactional
    public void deleteCompanyMenuAuthority(Long companyId) {
        Long count = this.companyMenuAuthorityRepository.deleteCompanyMenuAuthorityOne(companyId);
        log.info("=== delete company menu info: {}", count);
    }
}
