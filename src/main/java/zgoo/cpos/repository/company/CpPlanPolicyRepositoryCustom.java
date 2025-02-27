package zgoo.cpos.repository.company;

import java.util.List;
import java.util.Optional;

import zgoo.cpos.domain.company.CpPlanPolicy;
import zgoo.cpos.dto.company.CompanyDto.CpPlanDto;

public interface CpPlanPolicyRepositoryCustom {
    List<CpPlanDto> findAllByCompanyIdDto(Long companyId);

    List<CpPlanPolicy> findAllByCompanyId(Long companyId);

    CpPlanPolicy findByPlanName(String planName);

    List<CpPlanPolicy> findAll();

    CpPlanPolicy findByPlanNameAndCompanyId(String planName, Long companyId);

    void deleteAllByCompanyId(Long companyId);

    Optional<List<CpPlanDto>> findPlanListByCompanyId(Long companyId);
}
