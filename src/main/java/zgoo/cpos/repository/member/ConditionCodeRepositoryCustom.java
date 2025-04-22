package zgoo.cpos.repository.member;

import java.util.List;

import zgoo.cpos.domain.member.ConditionCode;
import zgoo.cpos.dto.member.ConditionDto.ConditionCodeBaseDto;
import zgoo.cpos.dto.member.ConditionDto.ConditionList;

public interface ConditionCodeRepositoryCustom {

    // 약관 전체조회
    List<ConditionCodeBaseDto> findAllCustom();

    // 약관 단건조회
    ConditionCode findByConditionCode(String conCode);

    // 약관 삭제
    void deleteByConditionCode(String conCode);

    List<ConditionList> findAllConditionWithVersion();
}
