package zgoo.cpos.repository.member;

import java.util.List;

import zgoo.cpos.domain.member.MemberCreditCard;
import zgoo.cpos.dto.member.MemberDto.MemberCreditCardDto;

public interface MemberCreditCardRepositoryCustom {
    List<MemberCreditCardDto> findAllByMemberIdDto(Long memberId);
    List<MemberCreditCard> findAllByMemberId(Long memberId);
    void deleteAllByMemberId(Long memberId);
}
