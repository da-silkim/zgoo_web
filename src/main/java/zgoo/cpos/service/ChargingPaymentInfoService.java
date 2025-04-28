package zgoo.cpos.service;

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
}
