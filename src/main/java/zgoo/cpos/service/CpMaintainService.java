package zgoo.cpos.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainListDto;
import zgoo.cpos.repository.cp.CpMaintainRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpMaintainService {

    public final CpMaintainRepository cpMaintainRepository;

    // 조회
    public Page<CpMaintainListDto> findCpMaintainInfoWithPagination(Long companyId, String searchOp, String searchContent,
            String processStatus, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CpMaintainListDto> cpList = Page.empty(pageable);

            if (companyId == null && (searchOp == null || searchOp.isEmpty()) && (searchContent == null || searchContent.isEmpty()) &&
                     (processStatus == null || processStatus.isEmpty()) && startDate == null && endDate == null) {
                log.info("Executing the [findCpMaintainWithPagination]");
                cpList = this.cpMaintainRepository.findCpMaintainWithPagination(pageable);
            } else {
                log.info("Executing the [searchCpMaintainWithPagination]");
                cpList = this.cpMaintainRepository.searchCpMaintainWithPagination(companyId, searchOp, searchContent,
                    processStatus, startDate, endDate, pageable);
            }

            return cpList;
        } catch (Exception e) {
            log.error("[findCpMaintainInfoWithPagination] error: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }
}
