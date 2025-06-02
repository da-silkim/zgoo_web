package zgoo.cpos.repository.payment;

import java.time.LocalDate;
import java.util.List;

import zgoo.cpos.domain.payment.PgSettlmntTotal;

public interface PgSettlmntTotalRepositoryCustom {
    List<PgSettlmntTotal> findAllByReqDt(LocalDate reqDt);

}
