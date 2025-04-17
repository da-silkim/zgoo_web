package zgoo.cpos.repository.calc;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.calc.PurchaseInfo;

public interface PurchaseRepository extends JpaRepository<PurchaseInfo, Long>, PurchaseRepositoryCustom {

}
