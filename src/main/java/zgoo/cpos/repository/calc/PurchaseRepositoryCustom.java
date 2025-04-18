package zgoo.cpos.repository.calc;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.calc.PurchaseDto.PurchaseListDto;

public interface PurchaseRepositoryCustom {

    Page<PurchaseListDto> findPurchaseWithPagination(Pageable pageable);

    Page<PurchaseListDto> searchPurchaseWithPagination(String searchOp, String searchContent, LocalDate startDate,
        LocalDate endDate, Pageable pageable);

    Long deletePurchaseOne(Long id);

    List<PurchaseListDto> findAllPurchaseWithoutPagination(String searchOp, String searchContent,
            LocalDate startDate, LocalDate endDate);
}
