package zgoo.cpos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.CommonCode;
import zgoo.cpos.domain.GrpCode;
import zgoo.cpos.dto.CommonCdDto;
import zgoo.cpos.dto.GrpCodeDto;
import zgoo.cpos.repository.CommonCodeRepository;
import zgoo.cpos.repository.GrpCodeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CommonCdService {

    private final GrpCodeRepository grpCodeRepository;
    private final CommonCodeRepository commonCodeRepository;

    // 그룹코드 저장
    @Transactional
    public void saveGrpCode(GrpCodeDto dto) {
        // dto >> entity
        GrpCode grpCode = GrpCode.builder()
                .grpCode(dto.getGrpCode())
                .grpcdName(dto.getGrpcdName())
                .regUserId(dto.getRegUserId())
                .regDt(dto.getRegDt())
                .modUserId(dto.getModUserId())
                .modDt(dto.getModDt())
                .build();

        grpCodeRepository.save(grpCode);

    }

    // 그룹코드 조회 - 단건
    public GrpCode findGrpOne(String grpcode) {
        return grpCodeRepository.findOne(grpcode);
    }

    // 그룹코드 조회 - 전체
    public List<GrpCode> findGrpCodeAll() {
        return grpCodeRepository.findAll();
    }

    // 공통코드 저장
    @Transactional
    public void saveCommonCode(CommonCdDto cdto, GrpCode grp) {
        // dto >> entity
        CommonCode commonCode = CommonCode.builder()
                .id(cdto.getId())
                .name(cdto.getName())
                .reserved(cdto.getReserved())
                .refCode1(cdto.getRefCode1())
                .refCode2(cdto.getRefCode2())
                .refCode3(cdto.getRefCode3())
                .regUserId(cdto.getRegUserId())
                .regDt(cdto.getRegDt())
                .modUserId(cdto.getModUserId())
                .modDt(cdto.getModDt())
                .grpCode(grp)
                .build();

        commonCodeRepository.save(commonCode);
    }

    // 그룹,공통코드 저장
    @Transactional
    public void saveGrpAndCommonCode(CommonCdDto cdto, GrpCodeDto gdto) {
        // dto >> entity
        GrpCode grpCode = GrpCode.builder()
                .grpCode(gdto.getGrpCode())
                .grpcdName(gdto.getGrpcdName())
                .regUserId(gdto.getRegUserId())
                .regDt(gdto.getRegDt())
                .modUserId(gdto.getModUserId())
                .modDt(gdto.getModDt())
                .build();

        // dto >> entity
        CommonCode commonCode = CommonCode.builder()
                .id(cdto.getId())
                .name(cdto.getName())
                .reserved(cdto.getReserved())
                .refCode1(cdto.getRefCode1())
                .refCode2(cdto.getRefCode2())
                .refCode3(cdto.getRefCode3())
                .regUserId(cdto.getRegUserId())
                .regDt(cdto.getRegDt())
                .modUserId(cdto.getModUserId())
                .modDt(cdto.getModDt())
                .grpCode(grpCode)
                .build();

        commonCodeRepository.save(commonCode);
    }

    // 공통코드 조회
    public CommonCode findCommonOne(String grpcd, String commoncd) {
        return commonCodeRepository.findOne(grpcd, commoncd);
    }

    // 공통코드 조회 - 전체
    public List<CommonCode> findCommonCodeAll() {
        return commonCodeRepository.findAll();
    }

    // 공통코드 조회 - 조건(그룹코드명)
    public List<CommonCode> findCommonCdListWithGrpCode(String grpcd) {
        return commonCodeRepository.findAllByGrpCode(grpcd);
    }
}
