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
import zgoo.cpos.dto.cp.CurrentChargingListDto;
import zgoo.cpos.repository.charger.CpCurrentTxRepository;
import zgoo.cpos.repository.company.CompanyRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpCurrentTxService {

    private final CpCurrentTxRepository cpCurrentTxRepository;
    private final ComService comService;
    private final CompanyRepository companyRepository;

    /*
     * paging - 현재 충전목록 전체 조회
     */
    public Page<CurrentChargingListDto> findCurrentChargingListAll(int page, int size, String userId) {

        Pageable pageable = PageRequest.of(page, size);
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);

            Page<CurrentChargingListDto> currentChargingList = cpCurrentTxRepository.findAllChargerListPaging(pageable,
                    levelPath, isSuperAdmin);
            return currentChargingList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching current charging list: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching current charging list: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    /*
     * paging - 현재 충전목록 조회 with 검색조건
     */
    public Page<CurrentChargingListDto> findCurrentChargingList(Long companyId, String chgStartTimeFrom,
            String chgStartTimeTo, String searchOp, String searchContent, int page, int size, String userId) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);

            Page<CurrentChargingListDto> currentChargingList = cpCurrentTxRepository.findChargerListPaging(companyId,
                    chgStartTimeFrom, chgStartTimeTo, searchOp, searchContent, pageable, levelPath, isSuperAdmin);
            return currentChargingList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching current charging list: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching current charging list: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    /*
     * 조회 > 충전기 전체 조회
     */
    @Transactional(readOnly = true)
    public List<CurrentChargingListDto> findAllCurrentTxListWithoutPagination(Long companyId, String startFrom,
            String startTo, String searchOp, String searchContent, String userId) {
        log.info(
                "=== Finding all charger list: companyId={}, startFrom={}, startTo={}, searchOp={}, searchContent={} ===",
                companyId, startFrom, startTo, searchOp, searchContent);

        boolean isSuperAdmin = comService.checkSuperAdmin(userId);
        Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
        String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
        log.info("== levelPath : {}", levelPath);

        // 대용량 데이터 처리를 위한 스트림 처리 또는 배치 처리 고려
        return cpCurrentTxRepository.findAllCurrentTxListWithoutPagination(companyId, startFrom, startTo, searchOp,
                searchContent, levelPath, isSuperAdmin);
    }
}
