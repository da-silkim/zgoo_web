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
import zgoo.cpos.dto.menu.MenuAuthorityDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityListDto;
import zgoo.cpos.mapper.MenuAuthorityMapper;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.menu.MenuAuthorityRepository;
import zgoo.cpos.repository.menu.MenuRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuAuthorityService {

    private final MenuAuthorityRepository menuAuthorityRepository;
    private final CompanyRepository companyRepository;
    private final MenuRepository menuRepository;

    // 사업자 권한 리스트 조회
    public List<MenuAuthorityDto.CompanyAuthorityListDto> findCompanyAuthorityList() {
        List<Tuple> companyAuthorityList = this.menuAuthorityRepository.companyAuthorityList();
        // log.info("사업자 권한 리스트 조회 >> {}", companyAuthorityList.toString());

        if (companyAuthorityList.isEmpty() || companyAuthorityList == null) {
            return new ArrayList<>();
        }

        try {
            List<MenuAuthorityDto.CompanyAuthorityListDto> comAuthList = MenuAuthorityMapper.toComListDto(companyAuthorityList);
            // log.info("사업자 권한 리스트 조회 >> {}", comAuthList);
            return comAuthList;
        } catch (Exception e) {
            log.error("[findCompanyAuthorityList] error:", e);
            return new ArrayList<>();
        }
        
    }

    // 접근권한조회
    public List<MenuAuthorityListDto> findMenuAuthorityList(Long companyId, String authority) {
        try {
            List<MenuAuthorityListDto> authorityList = this.menuAuthorityRepository.findMenuAuthorityList(companyId, authority);

            if (authorityList.isEmpty()) {
                return this.menuAuthorityRepository.defaultMenuAuthorityList();
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
        if (dtos.isEmpty()) {
            log.error("[saveMenuAuthorities] error");
        }

        try {
            Long companyId = dtos.get(0).getCompanyId();
            String authority = dtos.get(0).getAuthority();
            // String menuCode = dtos.get(0).getMenuCode();

            Long authorityIsExis = this.menuAuthorityRepository.menuAuthorityRegCheck(companyId, authority);
            Company company = this.companyRepository.findById(companyId)
                    .orElseThrow(() -> new IllegalArgumentException("company not found with id: " + companyId));

            for (MenuAuthorityBaseDto dto : dtos) {
                Menu menu = this.menuRepository.findMenuOne(dto.getMenuCode());

                if (authorityIsExis > 0) {
                    // 업데이트
                    log.error("[saveMenuAuthorities] update");
                    MenuAuthority menuAuthority = this.menuAuthorityRepository.findMenuAuthorityOne(companyId, authority, dto.getMenuCode());
                    menuAuthority.updateMenuAuthority(dto);
                } else {
                    // 저장
                    log.error("[saveMenuAuthorities] save");
                    MenuAuthority menuAuthority = MenuAuthorityMapper.toEntity(dto, company, menu);
                    this.menuAuthorityRepository.save(menuAuthority);
                }
            }
        } catch (Exception e) {
            log.error("[saveMenuAuthorities] error: {}", e.getMessage());
        }
    }
}
