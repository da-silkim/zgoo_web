package zgoo.cpos.repository.company;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.company.CompanyContract;

public interface CompanyContractRepository extends JpaRepository<CompanyContract, Long> {

}
