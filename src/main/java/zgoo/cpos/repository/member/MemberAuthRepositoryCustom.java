package zgoo.cpos.repository.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.member.MemberDto.MemberAuthDto;

public interface MemberAuthRepositoryCustom {

    // 전체조회
    Page<MemberAuthDto> findMemberAuthWithPagination(Pageable pageable);

    // 검색조회
    Page<MemberAuthDto> searchMemberAuthWithPagination(String idTag, String name, Pageable pageable);
}
