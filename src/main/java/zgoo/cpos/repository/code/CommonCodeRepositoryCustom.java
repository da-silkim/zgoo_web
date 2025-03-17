package zgoo.cpos.repository.code;

import java.util.List;

import zgoo.cpos.domain.code.CommonCode;
import zgoo.cpos.dto.code.CodeDto.CommCdBaseDto;

public interface CommonCodeRepositoryCustom {

    /*
     * ttt
     * 조회
     */
    // 공통코드조회 - 전체조회
    List<CommonCode> findAll();

    // 공통코드조회 - 그룹코드,공통코드명으로 조회
    CommonCode findCommonCodeOne(String grpcode, String commoncode);

    // 공통코드조회 - 그룹코드명조건으로 조회
    List<CommonCode> findAllByGrpCode(String grpcode);

    List<CommCdBaseDto> findCommonCdNamesByGrpCode(String grpcode);

    // Page<GrpAndCommCdDto> findAllByGrpCodePaging(String grpcode, Pageable
    // pageable);

    // // 공통코드,그룹코드 조회 - entity가 아닌 dto로 특정 컬럼들만 결과로 반환
    // List<GrpAndCommCdDto> findGrpAndCommCodeByGrpcode(String grpcode);

    /*
     * 수정
     */

    /*
     * 삭제
     */

    // 공통코드 삭제 - 특정 공통코드를 가진 단건의 공통코드 삭제
    Long deleteCommonCodeOne(String commoncode);

    // 공통코드 삭제 - 특정 그룹코드를 가진 모든 공통코드를 삭제
    Long deleteAllCommonCodeByGrpCode(String grpcode);

    String findCommonCodeName(String commoncode);

    // 공통코드 조회 - 문자열 >> 정수로 변환하여 정렬(그리드ROW수: SHOWLISTCNT)
    List<CommCdBaseDto> commonCodeStringSort(String commoncode);

    // 공통코드 조회(메뉴권한)
    List<CommCdBaseDto> commonCodeMenuAuthority(String grpcode);

    // 사용자 권한에 따른 메뉴권한 공통코드 조회
    List<CommCdBaseDto> commonCodeUsersAuthority(String authority);
}
