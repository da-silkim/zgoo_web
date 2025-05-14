package zgoo.cpos.service;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.history.ChgCommlogDto;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.history.ChgCommLogRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChgCommlogService {
    private final ChgCommLogRepository chgCommLogRepository;
    private final CompanyRepository companyRepository;
    private final ComService comService;

    /*
     * paging - 전체 통신로그 조회
     */
    public Page<ChgCommlogDto> findAllChgCommlog(int page, int size, String loginUserId) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                return Page.empty(pageable);
            }
            Page<ChgCommlogDto> chgCommlogList = chgCommLogRepository.findAllChgCommlog(pageable, levelPath,
                    isSuperAdmin);
            log.info("===ChgCommlog_PageInfo >> totalPages:{}, totalCount:{}", chgCommlogList.getTotalPages(),
                    chgCommlogList.getTotalElements());
            return chgCommlogList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching charging history: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching charging history: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    /**
     * paging - 통신로그 조회 with 검색조건
     */
    public Page<ChgCommlogDto> findChgCommlog(String searchOp, String searchContent, String recvFrom,
            String recvTo, int page, int size, String loginUserId) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                return Page.empty(pageable);
            }
            Page<ChgCommlogDto> chgCommlogList = chgCommLogRepository.findChgCommlog(searchOp, searchContent, recvFrom,
                    recvTo, pageable, levelPath, isSuperAdmin);
            return chgCommlogList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching charging history: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching charging history: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }
}
