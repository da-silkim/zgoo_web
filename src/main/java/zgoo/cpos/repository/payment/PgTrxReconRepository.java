package zgoo.cpos.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.payment.PgTrxRecon;

public interface PgTrxReconRepository extends JpaRepository<PgTrxRecon, Long>, PgTrxReconRepositoryCustom {

}
