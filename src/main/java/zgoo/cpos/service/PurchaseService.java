package zgoo.cpos.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseListDto;
import zgoo.cpos.repository.calc.PurchaseRepository;
import zgoo.cpos.repository.cs.CsRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {

    private final CsRepository csRepository;
    private final PurchaseRepository purchaseRepository;

    // 조회
    public Page<PurchaseListDto> findPurchaseInfoWithPagination(String searchOp, String searchContent,
            LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<PurchaseListDto> purList;

            if ((searchOp == null || searchOp.isEmpty()) && (searchContent == null || searchContent.isEmpty()) && 
                    startDate == null && endDate == null) {
                log.info("Executing the [findPurchaseWithPagination]");
                purList = this.purchaseRepository.findPurchaseWithPagination(pageable);
            } else {
                log.info("Executing the [searchPurchaseWithPagination]");
                purList = this.purchaseRepository.searchPurchaseWithPagination(searchOp, searchContent, startDate,
                    endDate, pageable);
            }

            return purList;
        } catch (Exception e) {
            log.error("[findPurchaseInfoWithPagination] error: {}", e.getMessage());
            return Page.empty(pageable);
        }
    }
}
