package zgoo.cpos.repository.member;

import java.util.List;

import zgoo.cpos.domain.member.MemberCar;
import zgoo.cpos.dto.member.MemberDto.MemberCarDto;

public interface MemberCarRepositoryCustom {
    List<MemberCarDto> findAllByMemberIdDto(Long memberId);
    List<MemberCar> findAllByMemberId(Long memberId);
    void deleteAllByMemberId(Long memberId);
}
