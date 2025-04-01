package zgoo.cpos.repository.menu;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.menu.CompanyMenuAuthority;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto.CompanyMenuRegDto;

public interface CompanyMenuAuthorityRepositoryCustom {
    // 메뉴권한이 등록된 사업자 조회(중복제거)
    List<Long> findDistinctCompanyIds(); // companyId만 조회
    List<CompanyMenuAuthorityDto.CompanyMenuRegDto> findDistinctCompanyWithCompanyName(); // companyId, companyName 조회
    Page<CompanyMenuAuthorityDto.CompanyMenuRegDto> findCompanyMenuWithPagination(Pageable pageable);
 
    // 메뉴권한이 등록된 사업자 조회(중복제거) - 검색
    Page<CompanyMenuAuthorityDto.CompanyMenuRegDto> searchCompanyMenuWithPagination(String companyName, Pageable pageable);

    // 해당 사업장의 메뉴 전체 조회
    List<CompanyMenuAuthorityDto.CompanyMenuAuthorityListDto> findCompanyMenuAuthorityList(Long companyId);

    // 사용여부에 따른 부모메뉴 useYn 업데이트
    void companyMenuAuthorityUseYnUpdate(CompanyMenuAuthority cma);
    String getParentCode(String menuCode);
    void updateParentMenuUseYn(String parentCode, CompanyMenuAuthority cma);

    // 메뉴 접근 권한이 설정된 사업자인지 확인
    Long companyMenuAuthorityRegCheck(Long companyId);

    // 해당 사업장의 menuCode 1개 조회
    CompanyMenuAuthority findCompanyMenuAuthorityOne(Long companyId, String menuCode);

    // 해당 사업장의 메뉴 권한 전체 삭제
    Long deleteCompanyMenuAuthorityOne(Long companyId);

    // Menu 테이블 메뉴 삭제에 의한 사업장별 해당 메뉴코드 일괄 삭제
    Long deleteCompanyMenuAuthorityMenuCodeAll(String menuCode);

    // 사업장 메뉴권한 조회
    List<CompanyMenuRegDto> findCompanyMenuList(Long companyId);
}
