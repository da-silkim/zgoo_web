package zgoo.cpos.repository.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.history.PaymentHistDto;

public interface PgTrxReconRepositoryCustom {
    Page<PaymentHistDto> findPaymentHist(Pageable pageable, String levelPath, boolean isSuperAdmin, Long companyId,
            String searchOp, String searchContent, String stateCode, String transactionStart, String transactionEnd);
}
