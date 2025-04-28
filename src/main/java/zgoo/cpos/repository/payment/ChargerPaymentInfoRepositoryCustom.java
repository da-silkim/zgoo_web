package zgoo.cpos.repository.payment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.payment.ChgPaymentInfoDto;
import zgoo.cpos.dto.payment.ChgPaymentSummaryDto;

public interface ChargerPaymentInfoRepositoryCustom {
    Page<ChgPaymentInfoDto> findChgPaymentInfo(String startMonthSearch, String endMonthSearch, String searchOp,
            String searchContent, Long companyId, Pageable pageable);

    // 합계 조회 메서드 추가
    ChgPaymentSummaryDto calculatePaymentSummary(String startMonthSearch, String endMonthSearch, String searchOp,
            String searchContent, Long companyId);

    // 충전기 결제이력 엑셀 다운로드
    List<ChgPaymentInfoDto> findAllChgPaymentInfoListWithoutPagination(String startMonthSearch,
            String endMonthSearch, String searchOp, String searchContent, Long companyId);
}
