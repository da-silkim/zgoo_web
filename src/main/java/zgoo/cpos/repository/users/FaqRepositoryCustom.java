package zgoo.cpos.repository.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.users.Faq;
import zgoo.cpos.dto.users.FaqDto;

public interface FaqRepositoryCustom {

    // FAQ 전체 조회
    Page<FaqDto.FaqListDto> findFaqWithPagination(Pageable pageable);

    // FAQ 검색 조회
    Page<FaqDto.FaqListDto> searchFaqListWithPagination(String section, Pageable pageable);

    // FAQ 단건 조회
    Faq findFaqOne(Long id);

    // FAQ 단건 조회(detail)
    FaqDto.FaqDetailDto findFaqDetailOne(Long id);

    // FAQ 이전글, 다음글 조회
    FaqDto.FaqDetailDto findPreviousFaq(Long id, String section, Faq currentFaq);
    FaqDto.FaqDetailDto findNextFaq(Long id, String section, Faq currentFaq);

    // FAQ 삭제
    Long deleteFaqOne(Long id);
}
