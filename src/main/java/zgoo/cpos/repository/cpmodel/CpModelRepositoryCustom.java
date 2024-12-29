package zgoo.cpos.repository.cpmodel;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.cp.CpModelDto.CpConnectorDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelDetailDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelListDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelRegDto;

public interface CpModelRepositoryCustom {

    // 충전기 모델 전체 조회
    Page<CpModelListDto> findCpModelWithPagination(Pageable pageable);

    // 충전기 모델 검색 조회
    Page<CpModelListDto> searchCpModelWithPagination(Long companyId, String manuf, String chgSpeed, Pageable pageable);

    // 충전기 모델 단건 조회
    CpModelRegDto findCpModelOne(Long modelId);

    // 충전기 모델 상세 조회
    CpModelDetailDto findCpModelDetailOne(Long modelId);

    // 충전기 커넥터 조회
    List<CpConnectorDto> findCpConnectorByModelId(Long modelId);
}
