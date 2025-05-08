package zgoo.cpos.repository.member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.member.MemberDto.MemberCarDto;
import zgoo.cpos.dto.member.MemberDto.MemberConditionDto;
import zgoo.cpos.dto.member.MemberDto.MemberCreditCardDto;
import zgoo.cpos.dto.member.MemberDto.MemberDetailDto;
import zgoo.cpos.dto.member.MemberDto.MemberListDto;
import zgoo.cpos.dto.member.MemberDto.MemberRegDto;

public interface MemberRepositoryCustom {

    // 회원 전체 조회
    Page<MemberListDto> findMemberWithPagination(Pageable pageable, String levelPath, boolean isSuperAdmin);

    // 회원 검색 조회
    Page<MemberListDto> searchMemberWithPagination(Long companyId, String idTag, String name, Pageable pageable,
            String levelPath, boolean isSuperAdmin);

    // 회원 단건 조회
    MemberRegDto findMemberOne(Long memberId, List<MemberCreditCardDto> cardInfo, List<MemberCarDto> carInfo,
            List<MemberConditionDto> conditionInfo);

    MemberRegDto findBizMemberOne(Long memberId, List<MemberCarDto> carInfo, List<MemberConditionDto> conditionInfo);

    // 회원 상세 조회
    MemberDetailDto findMemberDetailOne(Long memberId, List<MemberCreditCardDto> cardInfo, List<MemberCarDto> carInfo,
            List<MemberConditionDto> conditionInfo);

    MemberDetailDto findBizMemberDetailOne(Long memberId, List<MemberCarDto> carInfo,
            List<MemberConditionDto> conditionInfo);

    // 회원정보 검색(1:1문의에서 사용)
    List<MemberListDto> findMemberList(String name, String phoneNo);

    List<MemberListDto> findAllMemberListWithoutPagination(Long companyId, String idTag, String name, String levelPath,
            boolean isSuperAdmin);
}
