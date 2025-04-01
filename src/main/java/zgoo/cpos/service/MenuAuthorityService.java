package zgoo.cpos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.menu.Menu;
import zgoo.cpos.domain.menu.MenuAuthority;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.menu.MenuAuthorityDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityListDto;
import zgoo.cpos.mapper.MenuAuthorityMapper;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.menu.MenuAuthorityRepository;
import zgoo.cpos.repository.menu.MenuRepository;
import zgoo.cpos.repository.users.UsersRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuAuthorityService {

    private final MenuAuthorityRepository menuAuthorityRepository;
    private final CompanyRepository companyRepository;
    private final MenuRepository menuRepository;
    private final UsersRepository usersRepository;

    // 사업자 권한 리스트 조회
    public List<MenuAuthorityDto.CompanyAuthorityListDto> findCompanyAuthorityList() {
        try {
            List<Tuple> companyAuthorityList = this.menuAuthorityRepository.companyAuthorityList();
            if (companyAuthorityList.isEmpty()) {
                return new ArrayList<>();
            }

            List<MenuAuthorityDto.CompanyAuthorityListDto> comAuthList = MenuAuthorityMapper.toComListDto(companyAuthorityList);
            return comAuthList;
        } catch (Exception e) {
            log.error("[findCompanyAuthorityList] error: {}", e.getMessage());
            return new ArrayList<>();
        }
        
    }

    // 접근권한조회
    @Transactional
    public List<MenuAuthorityListDto> findMenuAuthorityList(Long companyId, String authority) {
        try {
            List<MenuAuthorityListDto> authorityList = this.menuAuthorityRepository.findMenuAuthorityList(companyId, authority);
            // log.info("[findMenuAuthorityList] >> {}", authorityList.toString());
                
            if (authorityList.isEmpty()) {
                return this.menuAuthorityRepository.defaultMenuAuthorityList(companyId, authority);
            }

            return authorityList;
        } catch (Exception e) {
            log.error("[findMenuAuthorityList] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 접근권한 저장 및 업데이트
    @Transactional
    public void saveMenuAuthorities(List<MenuAuthorityBaseDto> dtos) {
        try {
            if (dtos.isEmpty()) {
                log.error("[saveMenuAuthorities] dto empty error");
                return;
            }

            Long companyId = dtos.get(0).getCompanyId();
            String authority = dtos.get(0).getAuthority();

            Long authorityIsExis = this.menuAuthorityRepository.menuAuthorityRegCheck(companyId, authority);
            Company company = this.companyRepository.findById(companyId)
                    .orElseThrow(() -> new IllegalArgumentException("company not found with id: " + companyId));

            for (MenuAuthorityBaseDto dto : dtos) {
                Menu menu = this.menuRepository.findMenuOne(dto.getMenuCode());

                if (authorityIsExis > 0) {
                    // 업데이트
                    log.info("[saveMenuAuthorities] update");
                    MenuAuthority menuAuthority = this.menuAuthorityRepository.findMenuAuthorityOne(companyId, authority, dto.getMenuCode());
                    menuAuthority.updateMenuAuthority(dto);
                } else {
                    // 저장
                    log.info("[saveMenuAuthorities] save");
                    MenuAuthority menuAuthority = MenuAuthorityMapper.toEntity(dto, company, menu);
                    this.menuAuthorityRepository.save(menuAuthority);
                }
            }
        } catch (Exception e) {
            log.error("[saveMenuAuthorities] error: {}", e.getMessage());
        }
    }

    // 로그인 사용자 메뉴별 접근 권한 조회
    public MenuAuthorityBaseDto searchUserAuthority(String userId, String menuCode) {
        log.info("=== search login user menu authority ===");

        try {
            if (userId == null || userId.isEmpty()) return null;
            
            Users user = this.usersRepository.findUserOne(userId);
            String authority = user.getAuthority();
            Long companyId = user.getCompany().getId();

            // 슈퍼관리자
            if ("SU".equals(authority)) {
                MenuAuthorityBaseDto suAdmin = new MenuAuthorityBaseDto();
                suAdmin.setCompanyId(companyId);
                suAdmin.setAuthority("SU");
                suAdmin.setMenuCode(menuCode);
                suAdmin.setReadYn("Y");
                suAdmin.setModYn("Y");
                suAdmin.setExcelYn("Y");
                return suAdmin;
            }

            MenuAuthorityBaseDto dto = this.menuAuthorityRepository.findUserMenuAuthority(companyId, authority, menuCode);
            if (dto == null) return null;

            return dto;
        } catch (Exception e) {
            log.error("[searchUserAuthority] error: {}", e.getMessage());
            return null;
        }
    }
}
