package zgoo.cpos.repository.company;

import java.util.List;

import zgoo.cpos.domain.company.CompanyRoaming;
import zgoo.cpos.dto.company.CompanyDto.CompanyRoamingtDto;

public interface CompanyRoamingRepositoryCustom {
    List<CompanyRoamingtDto> findAllByCompanyIdDto(Long companyId);

    List<CompanyRoaming> findAllByCompanyId(Long companyId);

    void deleteAllByCompanyId(Long companyId);
}
