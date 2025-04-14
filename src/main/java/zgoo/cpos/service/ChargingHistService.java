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
import zgoo.cpos.dto.history.ChargingHistDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwDashboardDto;
import zgoo.cpos.repository.history.ChargingHistRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChargingHistService {
    private final ChargingHistRepository chargingHistRepository;

    /*
     * paging - 전체 충전이력 조회
     */
    public Page<ChargingHistDto> findAllChargingHist(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<ChargingHistDto> chargingHistList = chargingHistRepository.findAllChargingHist(pageable);
            log.info("===ChargingHistory_PageInfo >> totalPages:{}, totalCount:{}", chargingHistList.getTotalPages(),
                    chargingHistList.getTotalElements());

            // memberType이 "CB"이거나 "PB" 일경우 회원/ 없을경우 비회원으로 다시 저장
            return chargingHistList.map(hist -> {
                String memberType = hist.getMemberType();
                if (memberType == null || memberType.isEmpty()) {
                    hist.setMemberType("비회원");
                }

                return hist;
            });
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching charging history: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching charging history: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    /*
     * paging - 충전이력 조회 with 검색조건
     */
    public Page<ChargingHistDto> findChargingHist(Long companyId, String chgStartTimeFrom, String chgStartTimeTo,
            String searchOp, String searchContent, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<ChargingHistDto> chargingHistList = chargingHistRepository.findChargingHist(companyId,
                    chgStartTimeFrom,
                    chgStartTimeTo, searchOp, searchContent, pageable);
            return chargingHistList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching charging history: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching charging history: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    /*
     * 충전이력 엑셀 다운로드
     */
    @Transactional(readOnly = true)
    public List<ChargingHistDto> findAllChargingHistListWithoutPagination(Long companyId, String chgStartTimeFrom,
            String chgStartTimeTo, String searchOp, String searchContent) {
        log.info(
                "=== Finding all charging history list: companyId={}, startFrom={}, startTo={}, searchOp={}, searchContent={} ===",
                companyId, chgStartTimeFrom, chgStartTimeTo, searchOp, searchContent);

        return chargingHistRepository.findAllChargingHistListWithoutPagination(companyId, chgStartTimeFrom,
                chgStartTimeTo, searchOp, searchContent);
    }

    /* 
     * 대시보드 - 충전현황
     */
    public TotalkwDashboardDto findChargingHistByPeriod() {
        try {
            LocalDate today = LocalDate.now();    // 오늘 날짜
            LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1);                       // 당월의 첫 번째 날
            YearMonth lastMonth = YearMonth.from(today).minusMonths(1);                 // 전월 YearMonth
            LocalDate sameDayLastMonth = today.minusMonths(1).withDayOfMonth(today.getDayOfMonth()); // 전월의 같은 일자
            LocalDate firstDayOfLastMonth = lastMonth.atDay(1);                               // 전월의 첫 번째 날
            LocalDate lastDayOfLastMonth = lastMonth.atEndOfMonth();                                     // 전월의 마지막 날


            LocalDate endDate = today.getDayOfMonth() > lastDayOfLastMonth.getDayOfMonth() ? lastDayOfLastMonth : sameDayLastMonth;


            TotalkwDashboardDto lastMonthDto = chargingHistRepository.findChargingHistByPeriod(firstDayOfLastMonth.atStartOfDay(), 
                endDate.atTime(23, 59, 59));
            TotalkwDashboardDto currMonthDto = chargingHistRepository.findChargingHistByPeriod(firstDayOfCurrentMonth.atStartOfDay(), 
                today.atTime(23, 59, 59));

            currMonthDto.setFastCheck(compareAmountCheck(lastMonthDto.getFastChgAmount(), currMonthDto.getFastChgAmount(), "Fast"));
            currMonthDto.setLowCheck(compareAmountCheck(lastMonthDto.getLowChgAmount(), currMonthDto.getLowChgAmount(), "Low"));
            currMonthDto.setDespnCheck(compareAmountCheck(lastMonthDto.getDespnChgAmount(), currMonthDto.getDespnChgAmount(), "Despn"));
            
            currMonthDto.setCompareFast(compareAmount(lastMonthDto.getFastChgAmount(), currMonthDto.getFastChgAmount()));
            currMonthDto.setCompareLow(compareAmount(lastMonthDto.getLowChgAmount(), currMonthDto.getLowChgAmount()));
            currMonthDto.setCompareDespn(compareAmount(lastMonthDto.getDespnChgAmount(), currMonthDto.getDespnChgAmount()));

            currMonthDto.setPeriod(firstDayOfCurrentMonth + " ~ " + today);

            return currMonthDto;
        } catch (Exception e) {
            log.error("[findChargingHistByPeriod] error: {}", e.getMessage(), e);
            return null;
        }
    }

    private int compareAmountCheck(BigDecimal prev, BigDecimal curr, String label) {
        int comparison = prev.compareTo(curr);
        if (comparison > 0) {
            log.info("전월의 {} 충전량이 당월보다 큽니다.", label);
            return -1;
        } else if (comparison < 0) {
            log.info("당월의 {} 충전량이 전월보다 큽니다.", label);
            return 1;
        } else {
            log.info("두 달의 {} 충전량이 같습니다.", label);
            return 0;
        }
    }

    private BigDecimal compareAmount(BigDecimal prev, BigDecimal curr) {
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
