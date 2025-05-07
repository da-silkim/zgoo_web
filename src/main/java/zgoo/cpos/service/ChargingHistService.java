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
            LocalDate today = LocalDate.now(); // 오늘 날짜
            LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1); // 당월의 첫 번째 날
            YearMonth lastMonth = YearMonth.from(today).minusMonths(1); // 전월 YearMonth
            LocalDate sameDayLastMonth = today.minusMonths(1)
                    .withDayOfMonth(Math.min(today.getDayOfMonth(), lastMonth.lengthOfMonth())); // 전월의 같은 일자
            LocalDate firstDayOfLastMonth = lastMonth.atDay(1); // 전월의 첫 번째 날
            LocalDate lastDayOfLastMonth = lastMonth.atEndOfMonth(); // 전월의 마지막 날

            LocalDate endDate = today.getDayOfMonth() > lastDayOfLastMonth.getDayOfMonth() ? lastDayOfLastMonth
                    : sameDayLastMonth;

            TotalkwDashboardDto lastMonthDto = chargingHistRepository.findChargingHistByPeriod(
                    firstDayOfLastMonth.atStartOfDay(),
                    endDate.atTime(23, 59, 59));
            TotalkwDashboardDto currMonthDto = chargingHistRepository.findChargingHistByPeriod(
                    firstDayOfCurrentMonth.atStartOfDay(),
                    today.atTime(23, 59, 59));

            // 데이터가 없는 경우 기본값 설정
            if (lastMonthDto == null) {
                lastMonthDto = createEmptyTotalkwDashboardDto();
            }

            if (currMonthDto == null) {
                currMonthDto = createEmptyTotalkwDashboardDto();
                currMonthDto.setPeriod(firstDayOfCurrentMonth + " ~ " + today);
                return currMonthDto;
            }

            // null 체크 추가
            BigDecimal lastFast = lastMonthDto.getFastChgAmount() != null ? lastMonthDto.getFastChgAmount()
                    : BigDecimal.ZERO;
            BigDecimal lastLow = lastMonthDto.getLowChgAmount() != null ? lastMonthDto.getLowChgAmount()
                    : BigDecimal.ZERO;
            BigDecimal lastDespn = lastMonthDto.getDespnChgAmount() != null ? lastMonthDto.getDespnChgAmount()
                    : BigDecimal.ZERO;

            BigDecimal currFast = currMonthDto.getFastChgAmount() != null ? currMonthDto.getFastChgAmount()
                    : BigDecimal.ZERO;
            BigDecimal currLow = currMonthDto.getLowChgAmount() != null ? currMonthDto.getLowChgAmount()
                    : BigDecimal.ZERO;
            BigDecimal currDespn = currMonthDto.getDespnChgAmount() != null ? currMonthDto.getDespnChgAmount()
                    : BigDecimal.ZERO;

            currMonthDto.setFastCheck(compareAmountCheck(lastFast, currFast, "Fast"));
            currMonthDto.setLowCheck(compareAmountCheck(lastLow, currLow, "Low"));
            currMonthDto.setDespnCheck(compareAmountCheck(lastDespn, currDespn, "Despn"));

            currMonthDto.setCompareFast(compareAmount(lastFast, currFast));
            currMonthDto.setCompareLow(compareAmount(lastLow, currLow));
            currMonthDto.setCompareDespn(compareAmount(lastDespn, currDespn));

            currMonthDto.setPeriod(firstDayOfCurrentMonth + " ~ " + today);

            return currMonthDto;
        } catch (Exception e) {
            log.error("[findChargingHistByPeriod] error: {}", e.getMessage(), e);
            // 오류 발생 시 기본 객체 반환
            TotalkwDashboardDto defaultDto = createEmptyTotalkwDashboardDto();
            defaultDto.setPeriod(LocalDate.now().withDayOfMonth(1) + " ~ " + LocalDate.now());
            return defaultDto;
        }
    }

    /**
     * 빈 TotalkwDashboardDto 객체 생성
     */
    private TotalkwDashboardDto createEmptyTotalkwDashboardDto() {
        TotalkwDashboardDto dto = new TotalkwDashboardDto();
        dto.setFastChgAmount(BigDecimal.ZERO);
        dto.setLowChgAmount(BigDecimal.ZERO);
        dto.setDespnChgAmount(BigDecimal.ZERO);
        dto.setFastCheck(0);
        dto.setLowCheck(0);
        dto.setDespnCheck(0);
        dto.setCompareFast(BigDecimal.ZERO);
        dto.setCompareLow(BigDecimal.ZERO);
        dto.setCompareDespn(BigDecimal.ZERO);
        return dto;
    }

    private int compareAmountCheck(BigDecimal prev, BigDecimal curr, String label) {
        // null 체크 추가
        if (prev == null)
            prev = BigDecimal.ZERO;
        if (curr == null)
            curr = BigDecimal.ZERO;

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
        // null 체크 추가
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
