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
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.menu.CompanyMenuAuthority;
import zgoo.cpos.domain.menu.Menu;
import zgoo.cpos.domain.menu.MenuAuthority;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.dto.menu.MenuDto.MenuListDto;
import zgoo.cpos.dto.menu.MenuDto;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto.CompanyMenuAuthorityListDto;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto.CompanyMenuRegDto;
import zgoo.cpos.mapper.MenuAuthorityMapper;
import zgoo.cpos.mapper.MenuMapper;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.menu.CompanyMenuAuthorityRepository;
import zgoo.cpos.repository.menu.MenuAuthorityRepository;
import zgoo.cpos.repository.menu.MenuRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;
    private final CompanyRepository companyRepository;
    private final MenuAuthorityRepository menuAuthorityRepository;
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
            log.error("[findMenuList] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 메뉴 - 전체 조회(부모코드명 추가)
    public List<MenuDto.MenuAuthorityListDto> findMenuListWithParentName() {
        try {
            List<MenuDto.MenuAuthorityListDto> menuList = this.menuRepository.findMenuListWithParentName();
            return menuList;
        } catch (Exception e) {
            log.error("[findMenuListWithParentName] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 메뉴 - 전체 조회(자식 메뉴 개수, 메뉴 레벨명 추가)
    public List<MenuDto.MenuListDto> findMenuListWithChild(String authority) {
        try {
            List<MenuDto.MenuListDto> menuListDto =this.menuRepository.getMuenListWithChildCount();
            if ("SU".equals(authority)) {
                for (MenuListDto dto : menuListDto) {
                    dto.setReadYn("Y");
                }
            }
            log.info("[MenuService] >> findMenuListWithChild");
            return menuListDto;
        } catch (Exception e) {
            log.error("[findMenuListWithChild] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 메뉴 - 단건 조회
    public MenuDto.MenuRegDto findMenuOne(String menuCode) {
        try {
            Menu menu = this.menuRepository.findMenuOne(menuCode);
            return MenuMapper.toDto(menu);
        } catch (Exception e) {
            log.error("[findMenuOne] error: {}", e.getMessage());
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
            log.error("[getParentMenuByMenuLv] error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 메뉴 등록
    @Transactional
    public void saveMenu(MenuDto.MenuRegDto dto) {
        try {
            Menu menu = MenuMapper.toEntity(dto);
            this.menuRepository.save(menu);

            /* 
             * 메뉴 권한이 설정된 사업장이 존재하면, 새메뉴 정보 추가
             */
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
            log.error("[saveMenu] error: {}", e.getMessage());
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
            log.error("[updateMenu] error: {}", e.getMessage());
            return null;
        }
    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(String menuCode) {
        try {
            // 메뉴(Master) 삭제 전 사업장별 메뉴 접근 권한에 있는 메뉴 먼저 삭제
            Long cmaCount = this.companyMenuAuthorityRepository.deleteCompanyMenuAuthorityMenuCodeAll(menuCode);
            log.info("=== delete company menu info: {}", cmaCount);

            Long menuCount = this.menuRepository.deleteMenuOne(menuCode);
            log.info("=== delete menu info: {}", menuCount);
        } catch (Exception e) {
            log.error("[deleteMenu] error: {}", e.getMessage());
        }

    }

    // 사업장별 메뉴 접근 권한 전체 조회
    public Page<CompanyMenuAuthorityDto.CompanyMenuRegDto> findCompanyMenuAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CompanyMenuAuthorityDto.CompanyMenuRegDto> cmaList = this.companyMenuAuthorityRepository.findCompanyMenuWithPagination(pageable);
            return cmaList;
        } catch (DataAccessException dae) {
            log.error("[findCompanyMenuAll] database error: {}", dae.getMessage());
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("[findCompanyMenuAll] error: {}", e.getMessage());
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
            log.error("[searchCompanyMenuWithPagination] database error: {}", dae.getMessage());
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("[searchCompanyMenuWithPagination] error: {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // 메뉴권한이 등록된 사업자 조회
    public List<CompanyMenuAuthorityDto.CompanyMenuRegDto> findCompanyDistinctList() {
        try {
            List<CompanyMenuAuthorityDto.CompanyMenuRegDto> cmaList = this.companyMenuAuthorityRepository.findDistinctCompanyWithCompanyName();
            return cmaList;
        } catch (Exception e) {
            log.error("[findCompanyDistinctList] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<CompanyMenuAuthorityDto.CompanyMenuAuthorityListDto>findCompanyMenuAuthorityList(Long companyId) {
        try {
            List<CompanyMenuAuthorityDto.CompanyMenuAuthorityListDto> cmaList = this.companyMenuAuthorityRepository.findCompanyMenuAuthorityList(companyId);
            return cmaList;
        } catch (Exception e) {
            log.error("[findCompanyMenuAuthorityList] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<CompanyMenuAuthorityListDto> findCompanyMenuAuthorityBasedUserAuthority(Long companyId, String authority) {
        try {
            return this.companyMenuAuthorityRepository.findCompanyMenuAuthorityBasedUserAuthority(companyId, authority);
        } catch (Exception e) {
            log.error("[findCompanyMenuAuthorityBasedUserAuthority] error: {}", e.getMessage());
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

            // 등록이 완료된 사업장별 메뉴 권한 조회
            List<CompanyMenuRegDto> comList = this.companyMenuAuthorityRepository.findCompanyMenuList(companyId);
            Company company = this.companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("=== company not found ==="));

            // AD, AS, NO authority
            userAuthorityDefault(comList, company, "AD");
            userAuthorityDefault(comList, company, "AS");
            userAuthorityDefault(comList, company, "NO");

            return 1;
        } catch (Exception e) {
            log.error("[saveCompanyMenuAuthorities] error: {}", e.getMessage());
            return -2;
        }
    }

    // AD, AS, NO setting permissions
    private void userAuthorityDefault(List<CompanyMenuRegDto> comList, Company company, String authority) {
        try {
            Long companyId = company.getId();

            /* 
             * 1. 권한이 이미 설정되어 있는지 확인
             * 2. 권한이 설정되어 있으면 return;
             * 3. 권한이 설정되어 있지 않으면, 사용자 권한 설정
             * 
             *     read_yn     mod_yn      excel_yn
             * AD    Y           Y            Y
             * AS    Y           Y            Y
             * NO    Y           N            Y
             */
            Long authorityIsExis = this.menuAuthorityRepository.menuAuthorityRegCheck(companyId, authority);
            if (authorityIsExis > 0) return;

            String modYn = authority.equals("NO") ? "N" : "Y";
            for (CompanyMenuRegDto dto : comList) {
                String menuCode = dto.getMenuCode();
                Menu menu = this.menuRepository.findMenuOne(menuCode);

                MenuAuthorityBaseDto menuAuth = new MenuAuthorityBaseDto();
                menuAuth.setAuthority(authority);
                menuAuth.setReadYn("Y");
                menuAuth.setModYn(modYn);
                menuAuth.setExcelYn("Y");

                MenuAuthority menuAuthority = MenuAuthorityMapper.toEntity(menuAuth, company, menu);
                this.menuAuthorityRepository.save(menuAuthority);
            }
        } catch (Exception e) {
            log.error("[userAuthorityDefault] error: {}", e.getMessage());
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
            log.error("[updateCompanyMenuAuthority] error: {}", e.getMessage());
        }
    }

    // 사업장별 메뉴 권한 삭제
    @Transactional
    public void deleteCompanyMenuAuthority(Long companyId) {
        try {
            Long count = this.companyMenuAuthorityRepository.deleteCompanyMenuAuthorityOne(companyId);
            log.info("=== delete company menu info: {}", count);
        } catch (Exception e) {
            log.error("[deleteCompanyMenuAuthority] error: {}", e.getMessage());
        }
    }
}
