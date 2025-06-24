package zgoo.cpos.repository.company;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.company.Company;
import zgoo.cpos.dto.company.CompanyDto;
import zgoo.cpos.dto.company.CompanyDto.BaseCompnayDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyListDto;

public interface CompanyRepositoryCustom {

    // Paging ===================================================
    // 업체조회
    Page<CompanyListDto> findCompanyListPaging(Pageable pageable, String levelPath, boolean isSuperAdmin);

    // 업체조회 - 검색창(조건 : 사업자id, 사업자유형, 사업자레벨 )
    Page<CompanyDto.CompanyListDto> findCompanyListByConditionPaging(Long companyId, String companyType,
            String companyLv, Pageable pageable, String levelPath, boolean isSuperAdmin);

    // Not paging ================================================
    // 검색옵션 사업자 조회 (return type : CompanyListDto)
    List<CompanyListDto> findCompanyListForSelectOptCl(String levelPath, boolean isSuperAdmin);

    // 검색옵션 사업자 조회 (return type : BaseCompnayDto)
    List<BaseCompnayDto> findCompanyListForSelectOptBc(String levelPath, boolean isSuperAdmin);

    // 사업자 단건 조회
    Company findCompanyOne(Long id);

    Optional<Company> findByCompanyName(String companyName);

    List<Company> findByLevelPathStartingWith(String levelPathPrefix);

    String findLevelPathByCompanyId(Long companyId);

    String findCompanyLvById(Long companyId);

}
