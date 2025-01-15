package zgoo.cpos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.member.Voc;
import zgoo.cpos.dto.member.VocDto.VocAnswerDto;
import zgoo.cpos.dto.member.VocDto.VocListDto;
import zgoo.cpos.repository.member.VocRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class VocService {
    private final VocRepository vocRepository;

    // 1:1문의 조회
    public Page<VocListDto> findVocInfoWithPagination(String type, String replyStat, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<VocListDto> vocList;

            if ((type == null || type.isEmpty()) && (replyStat == null || replyStat.isEmpty()) && (name == null || name.isEmpty())) {
                log.info("Executing the [findVocWithPagination]");
                vocList = this.vocRepository.findVocWithPagination(pageable);
            } else {
                log.info("Executing the [searchVocWithPagination]");
                vocList = this.vocRepository.searchVocWithPagination(type, replyStat, name, pageable);
            }

            return vocList;
        } catch (Exception e) {
            log.error("[findVocInfoWithPagination] error: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    // 1:1문의 단건 조회
    public VocAnswerDto findVocOne(Long vocId) {       
        try {
            VocAnswerDto voc = this.vocRepository.findVocOne(vocId);
            return voc;
        } catch (Exception e) {
            log.error("[findVocOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 1:1문의 답변 등록
    @Transactional
    public Integer updateVocAnswer(Long vocId, VocAnswerDto dto) {
        Voc voc = this.vocRepository.findById(vocId)
            .orElseThrow(() -> new IllegalArgumentException("voc not found with id: " + vocId));

        try {
            if (dto.getReplyContent() != null && !dto.getReplyContent().isEmpty()) {
                dto.setReplyStat("COMPLETE");
                voc.updateVocAnswer(dto);
                return 1;
            }
            return -1;
        } catch (Exception e) {
            log.error("[updateVocAnswer] error: {}", e.getMessage());
            return null;
        }
    }
}
