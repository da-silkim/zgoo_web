package zgoo.cpos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.querydsl.core.Tuple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.menu.MenuAuthorityDto;
import zgoo.cpos.mapper.MenuAuthorityMapper;
import zgoo.cpos.repository.menu.MenuAuthorityRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuAuthorityService {

    private final MenuAuthorityRepository menuAuthorityRepository;

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
            log.error("사업자 권한 리스트 조회 중 오류 발생: ", e);
            return new ArrayList<>();
        }
        
    }
}
