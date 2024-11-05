package zgoo.cpos.repository.code;

import java.util.List;

import zgoo.cpos.domain.code.GrpCode;

public interface GrpCodeRepositoryCustom {
    // ======= 조회 =====================
    // 그룹코드 - 전체조회
    List<GrpCode> findAll();

    // 그룹코드 - 코드값으로 조회
    GrpCode findByGrpCode(String grpcode);

    // 그룹코드 - 그룹코드이름으로 조회
    GrpCode findByGrpCodeName(String name);

    // ========== 삭제 ==================
    Long deleteGrpCode(String grpcode);

    // ========== 수정 ==================

}
