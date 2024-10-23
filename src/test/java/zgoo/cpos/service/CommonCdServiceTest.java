package zgoo.cpos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zgoo.cpos.domain.CommonCode;
import zgoo.cpos.domain.GrpCode;
import zgoo.cpos.dto.CommonCdDto;
import zgoo.cpos.dto.GrpCodeDto;
import zgoo.cpos.repository.CommonCodeRepository;
import zgoo.cpos.repository.GrpCodeRepository;
import zgoo.cpos.type.CommonCodeKey;

@SpringBootTest
@Transactional
public class CommonCdServiceTest {

    @Autowired
    CommonCdService commonCdService;

    @Autowired
    GrpCodeRepository grpCodeRepository;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    @Test
    @Rollback(false)
    public void 그룹코드_저장() throws Exception {

        // given
        GrpCodeDto dto = new GrpCodeDto();
        dto.setGrpCode("COKIND");
        dto.setGrpcdName("사업자유형");
        dto.setRegDt(LocalDateTime.now());
        dto.setRegUserId("test");

        // when
        commonCdService.saveGrpCode(dto);

        // then
    }

    @Test
    @Rollback(false)
    public void 공통코드_저장() throws Exception {
        // given
        GrpCodeDto dto = new GrpCodeDto();
        dto.setGrpCode("COKIND");
        dto.setGrpcdName("사업자유형");
        dto.setRegDt(LocalDateTime.now());
        dto.setRegUserId("test");
        // commonCdService.saveGrpCode(dto);

        CommonCdDto cdto = new CommonCdDto();
        CommonCodeKey ckey = new CommonCodeKey(dto.getGrpCode(), "CO");
        cdto.setId(ckey);
        cdto.setName("위탁운영사");
        cdto.setRegUserId("test");
        cdto.setRegDt(LocalDateTime.now());

        // when
        commonCdService.saveGrpAndCommonCode(cdto, dto);

        // then
    }

    // 그룹코드/공통코드 조회
    @Test
    @Rollback(false)
    public void 그룹코드_조회() throws Exception {
        // given
        GrpCodeDto dto = new GrpCodeDto();
        dto.setGrpCode("COKIND");
        dto.setGrpcdName("사업자유형");
        dto.setRegDt(LocalDateTime.now());
        dto.setRegUserId("test");
        // commonCdService.saveGrpCode(dto);

        CommonCdDto cdto = new CommonCdDto();
        CommonCodeKey ckey = new CommonCodeKey(dto.getGrpCode(), "CO");
        cdto.setId(ckey);
        cdto.setName("위탁운영사");
        cdto.setRegUserId("test");
        cdto.setRegDt(LocalDateTime.now());
        commonCdService.saveGrpAndCommonCode(cdto, dto);

        // when
        CommonCode commonOne = commonCdService.findCommonOne(dto.getGrpCode(), cdto.getId().getCommonCode());

        System.out.println("grp_code:" + commonOne.getGroup().getGrpCode());
        System.out.println("grp_code_name:" + commonOne.getGroup().getGrpcdName());
        System.out.println("common_code:" + commonOne.getId().getCommonCode());
        System.out.println("commoncd_name:" + commonOne.getName());

        // then
        Assertions.assertThat(commonOne.getId().getCommonCode()).isEqualTo(cdto.getId().getCommonCode());

    }

    // 기존 그룹코드에 공통코드만 저장
    @Test
    @Rollback(false)
    public void 공통코드_저장2() throws Exception {
        // given
        String grpcode = "COKIND";
        // 그룹코드 조회
        GrpCode grpOne = commonCdService.findGrpOne(grpcode);

        CommonCdDto cdto = new CommonCdDto();
        CommonCodeKey ckey = new CommonCodeKey(grpOne.getGrpCode(), "OP");
        cdto.setId(ckey);
        cdto.setName("충전사업자");
        cdto.setRegUserId("test");
        cdto.setRegDt(LocalDateTime.now());

        // when
        commonCdService.saveCommonCode(cdto, grpOne);

        // then
    }

    // 그룹코드별 공통코드 조회
    @Test
    public void 공통코드_조회_그룹코드() throws Exception {
        // given
        String grpCode = "COKIND";
        List<CommonCode> flist = commonCdService.findCommonCdListWithGrpCode(grpCode);

        // when
        for (CommonCode commonCode : flist) {
            System.out.println("c_name:" + commonCode.getName() + ", c_code:" + commonCode.getId().getCommonCode()
                    + ", grp_code:" + commonCode.getId().getGrpCode());
        }

        // then
    }
}
