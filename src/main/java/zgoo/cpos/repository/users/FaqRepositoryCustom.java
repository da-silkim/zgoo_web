package zgoo.cpos.repository.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.users.Faq;
import zgoo.cpos.dto.users.FaqDto;

public interface FaqRepositoryCustom {

    // FAQ 전체 조회
    Page<FaqDto.FaqListDto> findFaqWithPagination(Pageable pageable, String levelPath, boolean isSuperAdmin);

    // FAQ 검색 조회
    Page<FaqDto.FaqListDto> searchFaqListWithPagination(String section, Pageable pageable, String levelPath,
            boolean isSuperAdmin);

    // FAQ 단건 조회
    Faq findFaqOne(Long id, String levelPath, boolean isSuperAdmin);

    // FAQ 단건 조회(detail)
    FaqDto.FaqDetailDto findFaqDetailOne(Long id, String levelPath, boolean isSuperAdmin);

    // FAQ 이전글, 다음글 조회
    FaqDto.FaqDetailDto findPreviousFaq(Long id, String section, Faq currentFaq, String levelPath,
            boolean isSuperAdmin);

    FaqDto.FaqDetailDto findNextFaq(Long id, String section, Faq currentFaq, String levelPath, boolean isSuperAdmin);

    // FAQ 삭제
    Long deleteFaqOne(Long id);
}
