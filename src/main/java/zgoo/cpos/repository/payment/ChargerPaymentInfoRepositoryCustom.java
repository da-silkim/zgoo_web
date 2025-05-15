package zgoo.cpos.repository.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.payment.ChgPaymentInfoDto;
import zgoo.cpos.dto.payment.ChgPaymentSummaryDto;
import zgoo.cpos.dto.statistics.PurchaseSalesDto.PurchaseSalesLineChartBaseDto;
import zgoo.cpos.dto.statistics.PurchaseSalesDto.SalesDashboardDto;

public interface ChargerPaymentInfoRepositoryCustom {
    Page<ChgPaymentInfoDto> findChgPaymentInfo(String startMonthSearch, String endMonthSearch, String searchOp,
            String searchContent, Long companyId, Pageable pageable, String levelPath,
            boolean isSuperAdmin);

    // 합계 조회 메서드 추가
    ChgPaymentSummaryDto calculatePaymentSummary(String startMonthSearch, String endMonthSearch, String searchOp,
            String searchContent, Long companyId, String levelPath, boolean isSuperAdmin);

    // 충전기 결제이력 엑셀 다운로드
    List<ChgPaymentInfoDto> findAllChgPaymentInfoListWithoutPagination(String startMonthSearch,
            String endMonthSearch, String searchOp, String searchContent, Long companyId, String levelPath,
            boolean isSuperAdmin);

    // 연도별 매출 합계
    BigDecimal findTotalSalesByYear(Long companyId, String searchOp, String searchContent, Integer year);

    // 월별 매출 합계
    List<PurchaseSalesLineChartBaseDto> searchMonthlyTotalSales(Long companyId, String searchOp,
            String searchContent,
            Integer year);

    // 매출현황(대시보드)
    SalesDashboardDto findPaymentByPeriod(LocalDateTime startDate, LocalDateTime endDate);
}
