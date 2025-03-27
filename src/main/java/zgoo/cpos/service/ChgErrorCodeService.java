package zgoo.cpos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.ChgErrorCode;
import zgoo.cpos.dto.code.ChgErrorCodeDto.ChgErrorCodeListDto;
import zgoo.cpos.dto.code.ChgErrorCodeDto.ChgErrorCodeRegDto;
import zgoo.cpos.mapper.ChgErrorCodeMapper;
import zgoo.cpos.repository.code.ChgErrorCodeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChgErrorCodeService {

    private final ChgErrorCodeRepository chgErrorCodeRepository;

    // 에러코드 조회(전체 + 검색)
    public Page<ChgErrorCodeListDto> findErrorCodeInfoWithPagination(String manuf, String searchOp, String searchContent, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            if ((manuf == null || manuf.isEmpty()) && (searchOp == null || searchOp.isEmpty()) && (searchContent == null || searchContent.isEmpty())) {
                log.info("Executing the [findErrorCodeWithPagination]");
                return this.chgErrorCodeRepository.findErrorCodeWithPagination(pageable);
            } else {
                log.info("Executing the [searchErrorCodeWithPagination]");
                return this.chgErrorCodeRepository.searchErrorCodeWithPagination(manuf, searchOp, searchContent, pageable);
            }
        } catch (Exception e) {
            log.error("[findErrorCodeInfoWithPagination] error: {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // 에러코드 단건 조회
    public ChgErrorCodeRegDto findErrorCodeOne(Long errcdId) {
        try {
            ChgErrorCodeRegDto errorCode = this.chgErrorCodeRepository.findErrorCodeOne(errcdId);
            return errorCode;
        } catch (Exception e) {
            log.error("[findErrorCode] error : {}", e.getMessage());
            return null;
        }
    }

    // 에러코드 저장
    public void saveErrorCode(ChgErrorCodeRegDto dto) {
        try {
            ChgErrorCode errorCode = ChgErrorCodeMapper.toEntity(dto);
            this.chgErrorCodeRepository.save(errorCode);
        } catch (Exception e) {
            log.error("[saveErrorCode] error: {}", e.getMessage());
        }
    }

    // 에러코드 수정
    @Transactional
    public void updateErrorCode(Long errcdId, ChgErrorCodeRegDto dto) {
        ChgErrorCode errorCode = this.chgErrorCodeRepository.findById(errcdId)
            .orElseThrow(() -> new IllegalArgumentException("error code not found with id: " + errcdId));

        try {
            errorCode.updateChgErrorCode(dto);
        } catch (Exception e) {
            log.error("[updateErrorCode] error: {}", e.getMessage());
        }
    }

    // 에러코드 삭제
    public void deleteErrorCode(Long errcdId) {
        ChgErrorCode errorCode = this.chgErrorCodeRepository.findById(errcdId)
            .orElseThrow(() -> new IllegalArgumentException("error code not found with id: " + errcdId));

        try {
            this.chgErrorCodeRepository.deleteById(errcdId);
            log.info("==== errcdId: {} is deleted..", errcdId);
        } catch (Exception e) {
            log.error("[deleteErrorCode] error: {}", e.getMessage());
        }
    }
}
