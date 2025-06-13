package zgoo.cpos.repository.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.member.MemberAuthHistDto;

public interface MemberAuthHistRepositoryCusotm {
    Page<MemberAuthHistDto> findAllMemberAuthHistWithPagination(Pageable pageable, String levelPath,
            boolean isSuperAdmin);

    Page<MemberAuthHistDto> findMemberAuthHistWithPagination(Long companyId, String searchOp, String contentSearch,
            String fromDate, String toDate, Pageable pageable, String levelPath, boolean isSuperAdmin);
}
