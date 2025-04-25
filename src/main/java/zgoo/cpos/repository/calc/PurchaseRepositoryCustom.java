package zgoo.cpos.repository.calc;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.calc.PurchaseDto.PurchaseAccountDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseDetailDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseListDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseRegDto;

public interface PurchaseRepositoryCustom {

    // 매입 전체 조회(페이징)
    Page<PurchaseListDto> findPurchaseWithPagination(Pageable pageable);

    // 매입 검색 조회
    Page<PurchaseListDto> searchPurchaseWithPagination(String searchOp, String searchContent, LocalDate startDate,
        LocalDate endDate, Pageable pageable);

    // 매입 삭제
    Long deletePurchaseOne(Long id);

    // 매입 전체 조회(리스트)
    List<PurchaseListDto> findAllPurchaseWithoutPagination(String searchOp, String searchContent,
            LocalDate startDate, LocalDate endDate);
    
    // 매입 단건 조회
    PurchaseRegDto findPurchaseOne(Long id);

    // 계정과목 정보 조회
    PurchaseAccountDto searchAccountLand(String stationId);
    PurchaseAccountDto searchAccountSafety(String stationId);

    // 매입 상세 조회
    PurchaseDetailDto findPurchaseDetailOne(Long id);
}
