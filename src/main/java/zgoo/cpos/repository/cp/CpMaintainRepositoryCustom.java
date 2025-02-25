package zgoo.cpos.repository.cp;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.cp.CpMaintainDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainListDto;

public interface CpMaintainRepositoryCustom {

    // 장애 등록 현황 - 전체 조회
    Page<CpMaintainListDto> findCpMaintainWithPagination(Pageable pageable);

    // 장애 등록 현황 - 검색 조회
    Page<CpMaintainListDto> searchCpMaintainWithPagination(Long companyId, String searchOp, String searchContent,
        String processStatus, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
