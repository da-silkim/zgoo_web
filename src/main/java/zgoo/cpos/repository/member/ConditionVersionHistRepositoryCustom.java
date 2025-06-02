package zgoo.cpos.repository.member;

import java.time.LocalDateTime;
import java.util.List;

import zgoo.cpos.domain.member.ConditionVersionHist;
import zgoo.cpos.dto.member.ConditionDto.ConditionVersionHistBaseDto;

public interface ConditionVersionHistRepositoryCustom {

    // 약관 개정 전체조회
    List<ConditionVersionHistBaseDto> findAllByConditionCode(String conditionCode);

    // 약관 개정 삭제
    void deleteByConditionCode(String conCode);

    // 약관 개정 단건 조회
    ConditionVersionHistBaseDto findByIdCustom(Long id);

    /* 
     * 현재 날짜 >= 적용날짜인 약관 중 최신약관 조회
     */
    ConditionVersionHist findRecentHistByConditionCode(String conditionCode, LocalDateTime applyDt);

    // 약관 개정 적용여부가 Y인 것 조회
    ConditionVersionHist findApplyYesByConditionCode(String conditionCode);

    // 현재일 이후, 적용이 안 되어있고, 적용일 30일 전 약관 조회
    ConditionVersionHist findRevisionConditionByConditionCode(String conditionCode);
}
