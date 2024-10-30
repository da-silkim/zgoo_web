package zgoo.cpos.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.CommonCode;
import zgoo.cpos.dto.code.GrpAndCommCdDto;

public interface CommonCodeRepositoryCustom {

    /* ttt
     * 조회
     */
    // 공통코드조회 - 전체조회
    List<CommonCode> findAll();

    // 공통코드조회 - 그룹코드,공통코드명으로 조회
    CommonCode findCommonCodeOne(String grpcode, String commoncode);

    // 공통코드조회 - 그룹코드명조건으로 조회
    List<CommonCode> findAllByGrpCode(String grpcode);

    Page<GrpAndCommCdDto> findAllByGrpCodePaging(String grpcode, Pageable pageable);

    // 공통코드,그룹코드 조회 - entity가 아닌 dto로 특정 컬럼들만 결과로 반환
    List<GrpAndCommCdDto> findGrpAndCommCodeByGrpcode(String grpcode);

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

}
