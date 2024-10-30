package zgoo.cpos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.CommonCode;
import zgoo.cpos.domain.GrpCode;
import zgoo.cpos.dto.code.CommonCdDto;
import zgoo.cpos.dto.code.GrpAndCommCdDto;
import zgoo.cpos.dto.code.GrpCodeDto;
import zgoo.cpos.mapper.CommonCodeMapper;
import zgoo.cpos.mapper.GrpCodeMapper;
import zgoo.cpos.repository.CommonCodeRepository;
import zgoo.cpos.repository.GrpCodeRepository;

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
    public List<GrpCodeDto> findGrpCodeAll() {

        List<GrpCode> grpcdList = grpCodeRepository.findAll();
        List<GrpCodeDto> list = null;

        if (grpcdList.size() > 0) {
            list = GrpCodeMapper.toDtoList(grpcdList);
        }

        return list;
    }

    // 공통코드 저장
    public void saveCommonCode(CommonCdDto cdto) {

        GrpCode grpfind = grpCodeRepository.findByGrpCode(cdto.getId().getGrpCode());

        // dto >> entity
        CommonCode commonCode = CommonCodeMapper.toEntity(cdto, grpfind);

        commonCodeRepository.save(commonCode);
    }

    // 그룹,공통코드 저장
    public void saveGrpAndCommonCode(CommonCdDto cdto, GrpCodeDto gdto) {
        // dto >> entity
        GrpCode grpCode = GrpCodeMapper.toEntity(gdto);

        // dto >> entity
        CommonCode commonCode = CommonCodeMapper.toEntity(cdto, grpCode);

        commonCodeRepository.save(commonCode);
    }

    // 공통코드 조회 (그룹코드, 공통코드)
    public CommonCdDto findCommonOne(String grpcd, String commoncd) {

        CommonCode findOne = commonCodeRepository.findCommonCodeOne(grpcd, commoncd);
        return CommonCodeMapper.toDto(findOne);
    }

    // 공통코드 조회 - 전체
    public List<CommonCdDto> findCommonCodeAll() {
        List<CommonCode> findList = commonCodeRepository.findAll();
        List<CommonCdDto> list = null;

        if (findList.size() > 0) {
            list = CommonCodeMapper.toDtoList(findList);
        }

        return list;
    }

    // 공통코드 조회 - 조건(그룹코드)
    public List<CommonCode> findCommonCdListWithGrpCode(String grpcd) {
        return commonCodeRepository.findAllByGrpCode(grpcd);
    }

    // 그룹코드,그룹코드명,공통코드,공통코드명(GrpAndCommCdDto) 조회 - 조건(그룹코드)
    public List<GrpAndCommCdDto> findCodeAndName(String grpcode) {
        return commonCodeRepository.findGrpAndCommCodeByGrpcode(grpcode);
    }

    /*
     * 수정
     * 
     * 영속성 상태에서 엔티티 값 수정 후 @Transactional이 붙어있는 함수가 끝나면 hibernate가 udpate 쿼리문을 전송하여
     * DB에 반영됨
     * 여러개의 data를 업데이트 해야 할 경우 벌크
     */
    @Transactional
    public CommonCdDto updateCommonCodeInfo(String origin_grpcode, String origin_commoncd, String commoncdName) {

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

    public void deleteCommonCode(String commonCode) {
        Long count = commonCodeRepository.deleteCommonCodeOne(commonCode);
        log.info("===commoncode deleted : {} ", count);
    }
}
