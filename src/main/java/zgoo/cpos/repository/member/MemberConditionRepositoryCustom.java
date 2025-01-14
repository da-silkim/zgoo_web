package zgoo.cpos.repository.member;

import java.util.List;

import zgoo.cpos.domain.member.MemberCondition;
import zgoo.cpos.dto.member.MemberDto.MemberConditionDto;

public interface MemberConditionRepositoryCustom {
    List<MemberConditionDto> findAllByMemberIdDto(Long memberId);
    List<MemberCondition> findAllByMemberId(Long memberId);
    void deleteAllByMemberId(Long memberId);
}
