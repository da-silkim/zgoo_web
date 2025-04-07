package zgoo.cpos.repository.cp;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.cp.CpMaintainDto.CpInfoDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainDetailDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainListDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainRegDto;

public interface CpMaintainRepositoryCustom {

    // 장애 등록 현황 - 전체 조회
    Page<CpMaintainListDto> findCpMaintainWithPagination(Pageable pageable);

    // 장애 등록 현황 - 검색 조회
    Page<CpMaintainListDto> searchCpMaintainWithPagination(Long companyId, String searchOp, String searchContent,
        String processStatus, LocalDate startDate, LocalDate endDate, Pageable pageable);

    // 충전소, 충전기 조회
    CpInfoDto searchCsCpInfoWithChargerId(String chargerId);

    // 장애 정보 - 단건 조회
    CpMaintainRegDto findMaintainOne(Long cpmaintainId);

    // 장애 정보 - 단건 조회(detail)
    CpMaintainDetailDto findMaintainDetailOne(Long cpmaintainId);
}
