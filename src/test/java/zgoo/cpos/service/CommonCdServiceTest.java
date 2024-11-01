package zgoo.cpos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zgoo.cpos.dto.code.CommonCdDto;
import zgoo.cpos.dto.code.GrpAndCommCdDto;
import zgoo.cpos.dto.code.GrpCodeDto;
import zgoo.cpos.repository.CommonCodeRepository;
import zgoo.cpos.repository.GrpCodeRepository;
import zgoo.cpos.type.CommonCodeKey;

@SpringBootTest
@Transactional
public class CommonCdServiceTest {

    @Autowired
    CodeService codeService;

    @Autowired
    GrpCodeRepository grpCodeRepository;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    // 조회 테스트
    // ===================================================================================
    /*
     * where 조건에 의한 조회
     */
    @Test
    @Rollback(false)
    public void 그룹코드_조회_단건() throws Exception {
        // given
        String grpcode = "COLV";

        // when
        GrpCodeDto dto = codeService.findGrpOne(grpcode);

        System.out.println("=== grp_code:" + dto.toString());

        // then

    }

    @Test
    @Rollback(false)
    public void 그룹코드조회_전체() throws Exception {
        // given
        // when
        List<GrpCodeDto> findList = codeService.findGrpCodeAll();

        for (GrpCodeDto grpCodeDto : findList) {
            System.out.println(grpCodeDto.toString());
        }
        // then
    }

    /*
     * where 조건 그룹코드명으로 그룹코드 조회
     */
    @Test
    public void 조회_그룹코드명() throws Exception {
        // given
        String grpcodeName = "사업자구분";

        // when
        GrpCodeDto dto = codeService.findGrpCodeByName(grpcodeName);

        System.out.println("===== result : " + dto.toString());

        // then
        Assertions.assertThat(grpcodeName).isEqualTo(dto.getGrpcdName());
    }

    /*
     * 공통코드 전체 조회
     */
    @Test
    public void 공통코드조회_전체() throws Exception {
        // given
        // when
        List<CommonCdDto> commonCodeAll = codeService.findCommonCodeAll();
        // then
        for (CommonCdDto commonCdDto : commonCodeAll) {
            System.out.println("===result : " + commonCdDto.toString());
        }

    }

    /*
     * 공통코드 단건 조회
     */
    @Test
    public void 공통코드_조회_단건() throws Exception {
        // given
        String grpCode = "COKIND";
        String commonCode = "CO";

        // when
        CommonCdDto dto = codeService.findCommonOne(grpCode, commonCode);

        System.out.println("==== grpCode : " + dto.getGroup().toString());
        System.out.println("=== commonCode : " + dto.toString());

        // then
        Assertions.assertThat(dto.getId().getCommonCode()).isEqualTo(commonCode);
        Assertions.assertThat(dto.getId().getGrpCode()).isEqualTo(grpCode);
    }

    /*
     * 그룹코드 + 공통코드 join하여 그룹코드,그룹코드명,공통코드,공통코드명 값만 가져오기
     */
    @Test
    public void 그룹코드_공통코드_조회_1() throws Exception {
        // given
        String grpCode = "COKIND";

        // when
        List<GrpAndCommCdDto> findList = codeService.findCodeAndName(grpCode);

        // then
        for (GrpAndCommCdDto dto : findList) {
            System.out.println("==== result : " + dto.toString());
        }

    }

    // 저장 테스트
    // ===================================================================================
    /*
     * 그룹코드만 저장
     */
    @Test
    @Rollback(false)
    public void 그룹코드_저장() throws Exception {

        // given
        GrpCodeDto dto = new GrpCodeDto();
        dto.setGrpCode("GTEST3");
        dto.setGrpcdName("그룹테스트3");
        dto.setRegDt(LocalDateTime.now());
        dto.setRegUserId("test");

        // when
        codeService.saveGrpCode(dto);

        // then

    }

    /*
     * 기존 그룹코드와 연관지어 공통코드만 저장
     */
    @Test
    @Rollback(false)
    public void 공통코드_저장() throws Exception {

        // given
        String grpCode = "GTEST3";
        CommonCdDto cdto = new CommonCdDto();
        CommonCodeKey ckey = new CommonCodeKey(grpCode, "GCTEST3-1");
        cdto.setId(ckey);
        cdto.setName("gctest3");
        cdto.setRegUserId("test");
        cdto.setRegDt(LocalDateTime.now());

        // when
        codeService.saveCommonCode(cdto);

        // then
    }

    /*
     * 그룹코드, 공통코드 동시 저장
     */
    @Test
    @Rollback(false)
    public void 공통코드_저장2() throws Exception {
        // given
        GrpCodeDto dto = new GrpCodeDto();
        dto.setGrpCode("GTEST4");
        dto.setGrpcdName("그룹테스트4");
        dto.setRegDt(LocalDateTime.now());
        dto.setRegUserId("test");

        codeService.saveGrpCode(dto);

        GrpCodeDto findGrpCode = codeService.findGrpOne(dto.getGrpCode());

        CommonCdDto cdto = new CommonCdDto();
        CommonCodeKey ckey = new CommonCodeKey(dto.getGrpCode(), "GCTEST4");

        cdto.setId(ckey);
        cdto.setName("공통코드테스트4");
        cdto.setRegUserId("test");
        cdto.setRegDt(LocalDateTime.now());

        // when
        codeService.saveGrpAndCommonCode(cdto, findGrpCode);

        // then
    }

    // 업데이트 테스트
    // ===================================================================================
    /*
     * 공통코드명 수정
     */
    @Test
    @Rollback(false)
    public void 공통코드_정보수정_공통코드명() throws Exception {
        // given
        String origin_grpcode = "GTEST3";
        String origin_commoncd = "GCTEST3-1";
        String changeCommonCdName = "gctest3-1";

        // when
        // 공통코드 수정
        CommonCdDto updateOne = codeService.updateCommonCodeInfo(origin_grpcode, origin_commoncd, changeCommonCdName);

        System.out.println("==result code : " + updateOne.getName());
        // then
        Assertions.assertThat(updateOne.getName()).isEqualTo(changeCommonCdName);
    }

    // 삭제 테스트
    // ===================================================================================
    /*
     * 특정 그룹코드 삭제 - FK로 참조중인 모든 공통코드도 함께 제거됨
     */
    @Test
    @Rollback(false)
    public void 그룹코드_삭제() throws Exception {
        // given
        String grpCode = "GTEST4";

        // when
        codeService.deleteGroupCode(grpCode);
        // then
    }

    /*
     * 특정 공통코드만 삭제
     */
    @Test
    @Rollback(false)
    public void 공통코드_삭제() throws Exception {
        // given
        String commonCode = "TEST2";
        // when
        codeService.deleteCommonCode(commonCode);

        // then
    }

    // 페이징 테스트
    // ===================================================================================
    @Test
    @DisplayName("Page테스트")
    public void searchWithPage() throws Exception {
        // given
        String grpCode = "GTEST1";

        // 페이징에 사용할 page, size 데이터를 갖는 pageRequest 생성
        PageRequest pageRequest = PageRequest.of(0, 4);

        // when
        Page<GrpAndCommCdDto> results = commonCodeRepository.findAllByGrpCodePaging(grpCode, pageRequest);

        for (GrpAndCommCdDto grpAndCommCdDto : results) {
            System.out.println("===resutl : " + grpAndCommCdDto.toString());
        }

        // then
        Assertions.assertThat(results.getSize()).isEqualTo(4);
        Assertions.assertThat(results.getContent()).extracting("commonCode").containsExactly("ATEST", "BTEST",
                "CTEST", "ETEST");
    }
}
