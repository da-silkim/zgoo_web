package zgoo.cpos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.history.ErrorHistDto;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.history.ErrorHistRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ErrorHistService {
    private final ErrorHistRepository errorHistRepository;
    private final CompanyRepository companyRepository;
    private final ComService comService;

    /*
     * paging - 전체 에러이력 조회
     */
    public Page<ErrorHistDto> findAllErrorHist(int page, int size, String loginUserId) {
        Pageable pageable = PageRequest.of(page, size);
        try {

            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return Page.empty(pageable);
            }

            Page<ErrorHistDto> errorHistList = errorHistRepository.findAllErrorHist(pageable, levelPath,
                    isSuperAdmin);
            log.info("===ErrorHist_PageInfo >> totalPages:{}, totalCount:{}", errorHistList.getTotalPages(),
                    errorHistList.getTotalElements());

            return errorHistList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching error history: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching error history: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    /*
     * paging - 충전이력 조회 with 검색조건
     */
    public Page<ErrorHistDto> findErrorHist(Long companyId, String manfCode, String chgStartTimeFrom,
            String chgStartTimeTo,
            String searchOp, String searchContent, int page, int size, String loginUserId) {
        Pageable pageable = PageRequest.of(page, size);
        try {

            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return Page.empty(pageable);
            }

            Page<ErrorHistDto> errorHistList = errorHistRepository.findErrorHist(companyId, manfCode, chgStartTimeFrom,
                    chgStartTimeTo, searchOp, searchContent, pageable, levelPath, isSuperAdmin);
            return errorHistList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching error history: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching error history: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    /* 
     * 에러이력(대시보드)
     */
    public List<ErrorHistDto> findLatestErrorHist(String loginUserId) {
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return new ArrayList<>();
            }

            List<ErrorHistDto> errorHistList = this.errorHistRepository.findLatestErrorHist(levelPath, isSuperAdmin);
            log.info("[findLatestErrorHist] success");
            return errorHistList;
        } catch (Exception e) {
            log.error("[findLatestErrorHist] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}
