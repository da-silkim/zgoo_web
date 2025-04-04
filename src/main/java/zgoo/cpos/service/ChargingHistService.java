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
import zgoo.cpos.dto.history.ChargingHistDto;
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
}
