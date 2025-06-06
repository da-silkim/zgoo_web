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
    Page<CpModelListDto> findCpModelWithPagination(Pageable pageable, String levelPath, boolean isSuperAdmin);

    // 충전기 모델 검색 조회
    Page<CpModelListDto> searchCpModelWithPagination(Long companyId, String manuf, String chgSpeed, Pageable pageable,
            String levelPath, boolean isSuperAdmin);

    List<CpModelListDto> findCpModelListForSelectOpt(String levelPath, boolean isSuperAdmin);

    // 충전기 모델 단건 조회
    CpModelRegDto findCpModelOne(Long modelId);

    // 충전기 모델 단건 조회(모달용)
    CpModelListDto findCpModelModalOne(String modelCode);

    // 충전기 모델 상세 조회
    CpModelDetailDto findCpModelDetailOne(Long modelId);

    // 충전기 커넥터 조회
    List<CpConnectorDto> findCpConnectorByModelId(Long modelId);

    // 충전기 모델 조회 by 사업자
    List<CpModelListDto> findCpModelListByCompanyId(Long companyId);
}
