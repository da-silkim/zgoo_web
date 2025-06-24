package zgoo.cpos.service;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.history.PaymentHistDto;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.payment.PgTrxReconRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentHistService {
    private final PgTrxReconRepository paymentHistRepository;
    private final CompanyRepository companyRepository;
    private final ComService comService;

    public Page<PaymentHistDto> findPaymentHist(Long companyId, String searchOp, String searchContent, String stateCode,
            String transactionStart, String transactionEnd, int page, int size, String loginUserId) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return Page.empty(pageable);
            }

            Page<PaymentHistDto> paymentHistList = paymentHistRepository.findPaymentHist(pageable, levelPath,
                    isSuperAdmin,
                    companyId, searchOp, searchContent, stateCode, transactionStart, transactionEnd);
            log.info("===PaymentHist_PageInfo >> totalPages:{}, totalCount:{}", paymentHistList.getTotalPages(),
                    paymentHistList.getTotalElements());

            return paymentHistList;

        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching charging history: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching charging history: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }
}
