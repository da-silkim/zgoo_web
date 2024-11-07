package zgoo.cpos.repository.company;

import java.util.List;

import zgoo.cpos.dto.company.CompanyDto;

public interface CompanyRepositoryCustom {

    // 업체조회
    List<CompanyDto.CompanyListDto> findCompanyListAllCustom();

    // 업체조회 - 검색창(조건 : 사업자id )
    List<CompanyDto.CompanyListDto> findCompanyListById(Long id);

    // 업체조회 - 검색창(조건 : 사업자유형 )
    List<CompanyDto.CompanyListDto> findCompanyListByType(String type);

    // 업체조회 - 검색창(조건 : 사업자레벨 )
    List<CompanyDto.CompanyListDto> findCompanyListByLv(String level);

}
