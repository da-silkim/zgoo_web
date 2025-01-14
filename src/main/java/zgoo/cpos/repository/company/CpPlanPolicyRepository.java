package zgoo.cpos.repository.company;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.company.CpPlanPolicy;

public interface CpPlanPolicyRepository extends JpaRepository<CpPlanPolicy, Long>, CpPlanPolicyRepositoryCustom {

}
