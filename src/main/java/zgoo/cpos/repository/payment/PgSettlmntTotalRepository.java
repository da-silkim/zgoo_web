package zgoo.cpos.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.payment.PgSettlmntTotal;

public interface PgSettlmntTotalRepository extends JpaRepository<PgSettlmntTotal, Long>,
        PgSettlmntTotalRepositoryCustom {

}
