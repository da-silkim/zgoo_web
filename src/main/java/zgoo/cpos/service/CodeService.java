package zgoo.cpos.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.CommonCode;
import zgoo.cpos.domain.code.GrpCode;
import zgoo.cpos.dto.code.CodeDto;
import zgoo.cpos.dto.code.CodeDto.CommCdBaseDto;
import zgoo.cpos.dto.code.CodeDto.CommCodeDto;
import zgoo.cpos.dto.code.CodeDto.GrpCodeDto;
import zgoo.cpos.mapper.CommonCodeMapper;
import zgoo.cpos.mapper.GrpCodeMapper;
import zgoo.cpos.repository.code.CommonCodeRepository;
import zgoo.cpos.repository.code.GrpCodeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeService {

    private final GrpCodeRepository grpCodeRepository;
    private final CommonCodeRepository commonCodeRepository;

    // 그룹코드 저장
    public void saveGrpCode(GrpCodeDto dto) {
        // dto >> entity
        GrpCode grpCode = GrpCodeMapper.toEntity(dto);

        grpCodeRepository.save(grpCode);

    }

    // 그룹코드 조회 - 단건
    public GrpCodeDto findGrpOne(String grpcode) {
        GrpCode findOne = grpCodeRepository.findByGrpCode(grpcode);
        GrpCodeDto dto = null;

        if (findOne != null) {
            dto = GrpCodeMapper.toDto(findOne);
        }

        return dto;
    }

    // 그룹코드 조회 - by grpcodeName
    public GrpCodeDto findGrpCodeByName(String grpcdName) {
        GrpCode findOne = grpCodeRepository.findByGrpCodeName(grpcdName);
        GrpCodeDto dto = null;
        if (findOne != null) {
            dto = GrpCodeMapper.toDto(findOne);
        }

        return dto;
    }

    // 그룹코드 조회 - 전체
    public List<CodeDto.GrpCodeDto> findGrpCodeAll() {

        List<GrpCode> grpcdList = grpCodeRepository.findAll();
        List<CodeDto.GrpCodeDto> list = null;

        if (grpcdList.size() > 0) {
            list = GrpCodeMapper.toDtoList(grpcdList);
        }

        return list;
    }

    // 그룹코드명의 일부분이 일치하는 그룹코드 조회
    public List<GrpCodeDto> findGrpCodeByGrpcdName(String grpcdName) {
        List<GrpCode> grpCodeList = grpCodeRepository.findByGrpcdNameLike(grpcdName);

        return GrpCodeMapper.toDtoList(grpCodeList);
    }

    // 공통코드 저장
    public void saveCommonCode(CodeDto.CommCodeDto cdto) {

        GrpCode grpfind = grpCodeRepository.findByGrpCode(cdto.getGrpCode());

        // dto >> entity
        CommonCode commonCode = CommonCodeMapper.toEntity(cdto, grpfind);

        commonCodeRepository.save(commonCode);
    }

    // 그룹,공통코드 저장
    public void saveGrpAndCommonCode(CodeDto.CommCodeDto cdto, GrpCodeDto gdto) {
        // dto >> entity
        GrpCode grpCode = GrpCodeMapper.toEntity(gdto);

        // dto >> entity
        CommonCode commonCode = CommonCodeMapper.toEntity(cdto, grpCode);

        commonCodeRepository.save(commonCode);
    }

    // 공통코드 조회 (그룹코드, 공통코드)
    public CodeDto.CommCodeDto findCommonOne(String grpcd, String commoncd) {

        CommonCode findOne = commonCodeRepository.findCommonCodeOne(grpcd, commoncd);
        return CommonCodeMapper.toDto(findOne);
    }

    // 공통코드 조회 - 전체
    public List<CodeDto.CommCodeDto> findCommonCodeAll() {
        List<CommonCode> findList = commonCodeRepository.findAll();
        List<CodeDto.CommCodeDto> list = null;

        if (findList.size() > 0) {
            list = CommonCodeMapper.toDtoList(findList);
        }

        return list;
    }

    // 공통코드 조회 - 조건(그룹코드)
    public List<CommonCode> findCommonCdListWithGrpCode(String grpcd) {
        return commonCodeRepository.findAllByGrpCode(grpcd);
    }

    // 공통코드 조회
    public CodeDto.CommCodeDto findCommCdGrpCd(String grpcd, String commoncd) {
        CommonCode findOne = commonCodeRepository.findCommonCodeOne(grpcd, commoncd);
        // CommonCdDt
        return CommonCodeMapper.toDto(findOne);
    }

    // 공통코드 조회 - 그룹코드 선택 시
    @Transactional(readOnly = true)
    public List<CommCodeDto> findCommonCdByGrpCd(String grpcd) {
        List<CommonCode> commonCodes = commonCodeRepository.findAllByGrpCode(grpcd);

        if (commonCodes.isEmpty())
            return Collections.emptyList();

        // 공통코드 엔티티를 DTO로 변환하여 반환
        // return commonCodes.stream()
        // .map(this::convertToDto)
        // .collect(Collectors.toList());
        return CommonCodeMapper.toDtoList(commonCodes);
    }

    public List<CommCdBaseDto> findCommonCdNamesByGrpcd(String grpcd) {
        return commonCodeRepository.findCommonCdNamesByGrpCode(grpcd);
    }

    // // 공통코드 entity -> dto
    // private CommonCdDto convertToDto(CommonCode commonCode) {
    // return CommonCdDto.builder()
    // .name(commonCode.getName())
    // .refCode1(commonCode.getRefCode1())
    // .refCode2(commonCode.getRefCode2())
    // .refCode3(commonCode.getRefCode3())
    // .regDt(commonCode.getRegDt())
    // .regUserId(commonCode.getRegUserId())
    // .modDt(commonCode.getModDt())
    // .modUserId(commonCode.getModUserId())
    // .id(commonCode.getId())
    // .build();
    // }

    // 그룹코드,그룹코드명,공통코드,공통코드명(GrpAndCommCdDto) 조회 - 조건(그룹코드)
    // public List<GrpAndCommCdDto> findCodeAndName(String grpcode) {
    // return commonCodeRepository.findGrpAndCommCodeByGrpcode(grpcode);
    // }

    /*
     * 수정
     * 
     * 영속성 상태에서 엔티티 값 수정 후 @Transactional이 붙어있는 함수가 끝나면 hibernate가 udpate 쿼리문을 전송하여
     * DB에 반영됨
     * 여러개의 data를 업데이트 해야 할 경우 벌크
     */
    @Transactional
    public CodeDto.CommCodeDto updateCommonCodeInfo(String origin_grpcode, String origin_commoncd,
            String commoncdName) {

        // @Transactional 어노테이션으로 조회시 영속성 상태로 전환
        CommonCode findOne = commonCodeRepository.findCommonCodeOne(origin_grpcode, origin_commoncd);

        log.info("=== before update: {}", findOne.toString());

        findOne.updateCommonCodeName(commoncdName);

        log.info("=== after update: {}", findOne.toString());

        return CommonCodeMapper.toDto(findOne);
    }

    /*
     * 삭제 - 그룹코드
     */
    @Transactional
    public void deleteGroupCode(String grpCode) {
        // 공통코드 먼저 삭제(그룹코드 FK로 참조하고 있기 때문)
        List<CommonCode> common = commonCodeRepository.findAllByGrpCode(grpCode);
        if (common.size() > 0) {
            Long count = commonCodeRepository.deleteAllCommonCodeByGrpCode(grpCode);
            log.info("=== commoncode deleted : {}", count);

        }

        Long grpcdDelCount = grpCodeRepository.deleteGrpCode(grpCode);
        log.info("=== grpcode deleted : {}", grpcdDelCount);
    }

    @Transactional
    public void deleteCommonCode(String commonCode) {
        Long count = commonCodeRepository.deleteCommonCodeOne(commonCode);
        log.info("===commoncode deleted : {} ", count);
    }

    /*
     * 그룹코드 - 그룹코드명 수정
     */
    @Transactional
    public GrpCodeDto updateGrpCode(GrpCodeDto grpcode) {
        GrpCode findOne = grpCodeRepository.findByGrpCode(grpcode.getGrpCode());

        log.info("=== before update: {}", findOne.toString());

        findOne.updateGrpcdCode(grpcode.getGrpcdName());

        log.info("=== after update: {}", findOne.toString());

        return GrpCodeMapper.toDto(findOne);
    }
}
