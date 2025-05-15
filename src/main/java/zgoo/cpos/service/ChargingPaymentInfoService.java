package zgoo.cpos.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.payment.ChgPaymentInfoDto;
import zgoo.cpos.dto.payment.ChgPaymentSummaryDto;
import zgoo.cpos.dto.statistics.PurchaseSalesDto.SalesDashboardDto;
import zgoo.cpos.repository.payment.ChargerPaymentInfoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChargingPaymentInfoService {

    private final ChargerPaymentInfoRepository chargerPaymentInfoRepository;

    /*
     * paging - 충전이력 조회 with 검색조건
     */
    public Page<ChgPaymentInfoDto> findChgPaymentInfo(String startMonthSearch, String endMonthSearch, String searchOp,
            String searchContent, Long companyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<ChgPaymentInfoDto> chgPaymentInfoList = chargerPaymentInfoRepository.findChgPaymentInfo(
                    startMonthSearch,
                    endMonthSearch, searchOp, searchContent, companyId, pageable);
            return chgPaymentInfoList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching charging history: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching charging history: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    /*
     * 충전기 결제금액 합계 조회
     */
    public ChgPaymentSummaryDto calculatePaymentSummary(
            String startMonthSearch, String endMonthSearch, String searchOp, String searchContent, Long companyId) {

        // 리포지토리의 집계 쿼리 메서드 호출
        return chargerPaymentInfoRepository.calculatePaymentSummary(
                startMonthSearch, endMonthSearch, searchOp, searchContent, companyId);
    }

    /**
     * 충전기 결제이력 엑셀 다운로드
     */
    @Transactional(readOnly = true)
    public List<ChgPaymentInfoDto> findAllChgPaymentInfoListWithoutPagination(String startMonthSearch,
            String endMonthSearch, String searchOp, String searchContent, Long companyId) {
        log.info(
                "=== Finding all charging payment info list: companyId={}, startFrom={}, startTo={}, searchOp={}, searchContent={} ===",
                companyId, startMonthSearch, endMonthSearch, searchOp, searchContent);

        return chargerPaymentInfoRepository.findAllChgPaymentInfoListWithoutPagination(startMonthSearch, endMonthSearch,
                searchOp, searchContent, companyId);
    }

    /* *
     * 매출현황(대시보드)
     */
    public SalesDashboardDto findPaymentByPeriod() {
        try {
            LocalDate today = LocalDate.now(); // 오늘 날짜
            LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1); // 당월의 첫 번째 날
            YearMonth lastMonth = YearMonth.from(today).minusMonths(1); // 전월 YearMonth
            LocalDate sameDayLastMonth = today.minusMonths(1)
                    .withDayOfMonth(Math.min(today.getDayOfMonth(), lastMonth.lengthOfMonth())); // 전월의 같은 일자
            LocalDate firstDayOfLastMonth = lastMonth.atDay(1); // 전월의 첫 번째 날
            LocalDate lastDayOfLastMonth = lastMonth.atEndOfMonth(); // 전월의 마지막 날

            LocalDate endDate = today.getDayOfMonth() > lastDayOfLastMonth.getDayOfMonth() ? lastDayOfLastMonth
                    : sameDayLastMonth;

            SalesDashboardDto lastMonthDto = chargerPaymentInfoRepository.findPaymentByPeriod(
                    firstDayOfLastMonth.atStartOfDay(),
                    endDate.atTime(23, 59, 59));
            SalesDashboardDto currMonthDto = chargerPaymentInfoRepository.findPaymentByPeriod(
                    firstDayOfCurrentMonth.atStartOfDay(),
                    today.atTime(23, 59, 59));

            currMonthDto.setPeriod(firstDayOfCurrentMonth + " ~ " + today);

            currMonthDto.setFastCheck(comparePaymentCheck(lastMonthDto.getFastSales(), currMonthDto.getFastSales(), "Fast"));
            currMonthDto.setLowCheck(comparePaymentCheck(lastMonthDto.getLowSales(), currMonthDto.getLowSales(), "Low"));
            currMonthDto.setDespnCheck(comparePaymentCheck(lastMonthDto.getDespnSales(), currMonthDto.getDespnSales(), "Despn"));

            currMonthDto.setCompareFast(comparePayment(lastMonthDto.getFastSales(), currMonthDto.getFastSales()));
            currMonthDto.setCompareLow(comparePayment(lastMonthDto.getLowSales(), currMonthDto.getLowSales()));
            currMonthDto.setCompareDespn(comparePayment(lastMonthDto.getDespnSales(), currMonthDto.getDespnSales()));

            log.info("lastMonthDto >> {}", lastMonthDto);
            log.info("currMonthDto >> {}", currMonthDto);
            return currMonthDto;
        } catch (Exception e) {
            log.error("[findPaymentByPeriod] error: {}", e.getMessage(), e);
            return createEmptySalesDashboardDto();
        }
    }

    /* *
     * 빈 SalesDashboardDto 객체 생성
     */
    private SalesDashboardDto createEmptySalesDashboardDto() {
        SalesDashboardDto dto = new SalesDashboardDto();
        dto.setLowCheck(0);
        dto.setFastCheck(0);
        dto.setDespnCheck(0);
        dto.setLowSales(BigDecimal.ZERO);
        dto.setFastSales(BigDecimal.ZERO);
        dto.setDespnSales(BigDecimal.ZERO);
        dto.setCompareLow(BigDecimal.ZERO);
        dto.setCompareFast(BigDecimal.ZERO);
        dto.setCompareDespn(BigDecimal.ZERO);
        dto.setPeriod(LocalDate.now().withDayOfMonth(1) + " ~ " + LocalDate.now());
        return dto;
    }

    private int comparePaymentCheck(BigDecimal prev, BigDecimal curr, String label) {
        if (prev == null)
            prev = BigDecimal.ZERO;
        if (curr == null)
            curr = BigDecimal.ZERO;

        int comparison = prev.compareTo(curr);
        if (comparison > 0) {
            log.info("전월의 {} 매출이 당월보다 큽니다.", label);
            return -1;
        } else if (comparison < 0) {
            log.info("당월의 {} 매출이 전월보다 큽니다.", label);
            return 1;
        } else {
            log.info("두 달의 {} 매출이 같습니다.", label);
            return 0;
        }
    }

    private BigDecimal comparePayment(BigDecimal prev, BigDecimal curr) {
                if (prev == null)
            prev = BigDecimal.ZERO;
        if (curr == null)
            curr = BigDecimal.ZERO;

        int comparison = prev.compareTo(curr);
        if (comparison > 0) {
            return prev.subtract(curr).setScale(2, RoundingMode.HALF_UP);
        } else if (comparison < 0) {
            return curr.subtract(prev).setScale(2, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
    }
}
