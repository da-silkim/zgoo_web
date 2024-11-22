package zgoo.cpos.repository.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.company.CompanyDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyListDto;

public interface CompanyRepositoryCustom {

    // 업체조회
    // List<CompanyDto.CompanyListDto> findCompanyListAllCustom();
    Page<CompanyListDto> findCompanyListAllCustom(Pageable pageable);

    // 업체조회 - 검색창(조건 : 사업자id )
    Page<CompanyDto.CompanyListDto> findCompanyListById(Long id, Pageable pageable);

    // 업체조회 - 검색창(조건 : 사업자유형 )
    Page<CompanyDto.CompanyListDto> findCompanyListByType(String type, Pageable pageable);

    // 업체조회 - 검색창(조건 : 사업자레벨 )
    Page<CompanyDto.CompanyListDto> findCompanyListByLv(String level, Pageable pageable);

}
