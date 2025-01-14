package zgoo.cpos.repository.tariff;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.tariff.TariffPolicy;

public interface TariffPolicyRepository extends JpaRepository<TariffPolicy, Long>, TariffPolicyRepositoryCustom {

}
