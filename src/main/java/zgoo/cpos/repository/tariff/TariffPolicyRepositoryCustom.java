package zgoo.cpos.repository.tariff;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.tariff.TariffPolicy;
import zgoo.cpos.dto.tariff.TariffDto.TariffInfoDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffPolicyDto;

public interface TariffPolicyRepositoryCustom {
    Page<TariffPolicyDto> findAllTariffPolicyPaging(Pageable page);

    Page<TariffPolicyDto> findTariffPolicyByCompanyIdPaging(Pageable pageable, Long companyId);

    TariffPolicy findTariffPolicyByPolicyId(Long policyId);

    TariffPolicy findByApplyCodeAndPolicyId(String applyCode, Long policyId);

    List<TariffPolicy> findTariffListByPolicyId(Long policyId);

    Optional<TariffPolicy> findTariffPolicyByPlanName(String planName);

    Long findTariffIdByPlanName(String planName);

    Optional<List<TariffInfoDto>> findTariffInfoListByTariffId(Long tariffId);
}
