package zgoo.cpos.repository.code;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.code.ChgErrorCodeDto.ChgErrorCodeListDto;
import zgoo.cpos.dto.code.ChgErrorCodeDto.ChgErrorCodeRegDto;

public interface ChgErrorCodeRepositoryCustom {

    // 에러코드 전체 조회
    Page<ChgErrorCodeListDto> findErrorCodeWithPagination(Pageable pageable, String levelPath, boolean isSuperAdmin);

    // 에러코드 검색 조회
    Page<ChgErrorCodeListDto> searchErrorCodeWithPagination(String manuf, String searchOp, String searchContent,
            Pageable pageable, String levelPath, boolean isSuperAdmin);

    // 에러코드 단건 조회
    ChgErrorCodeRegDto findErrorCodeOne(Long errcdId);
}
