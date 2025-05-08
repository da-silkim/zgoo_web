package zgoo.cpos.repository.member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.member.MemberDto.MemberAuthDto;

public interface MemberAuthRepositoryCustom {

    // 전체조회
    Page<MemberAuthDto> findMemberAuthWithPagination(Pageable pageable, String levelPath, boolean isSuperAdmin);

    // 검색조회
    Page<MemberAuthDto> searchMemberAuthWithPagination(String idtag, String name, Pageable pageable, String levelPath,
            boolean isSuperAdmin);

    // 단건조회
    MemberAuthDto findMemberAuthOne(String idtag);

    List<MemberAuthDto> findAllMemberTagWithoutPagination(String idTag, String name, String levelPath,
            boolean isSuperAdmin);
}
