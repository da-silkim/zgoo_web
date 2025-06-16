package zgoo.cpos.repository.member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.member.MemberAuthHistDto;

public interface MemberAuthHistRepositoryCusotm {
    Page<MemberAuthHistDto> findAllMemberAuthHistWithPagination(Pageable pageable);

    Page<MemberAuthHistDto> findMemberAuthHistWithPagination(String searchOp, String contentSearch, String fromDate,
            String toDate, Pageable pageable);

    List<MemberAuthHistDto> findMemberAuthHistList(String searchOp, String contentSearch, String fromDate,
            String toDate);

    void deleteAllByMemberId(Long memberId);
}
