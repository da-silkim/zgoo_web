package zgoo.cpos.repository.company;

import java.util.List;

import zgoo.cpos.dto.company.CompanyDto;

public interface CompanyRepositoryCustom {

    // 업체조회
    List<CompanyDto.CompanyListDto> findCompanyListAllCustom();

}
