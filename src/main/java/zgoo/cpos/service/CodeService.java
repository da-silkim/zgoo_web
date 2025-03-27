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
    public void saveGrpCode(GrpCodeDto dto, String loginUserId) {
        try {
            dto.setRegUserId(loginUserId);

            // dto >> entity
            GrpCode grpCode = GrpCodeMapper.toEntity(dto);
    
            grpCodeRepository.save(grpCode);
        } catch (Exception e) {
            log.error("[saveGrpCode] error: {}", e.getMessage());
        }
    }

    // 그룹코드 조회 - 단건
    public GrpCodeDto findGrpOne(String grpcode) {
        try {
            GrpCode findOne = grpCodeRepository.findByGrpCode(grpcode);
            return findOne == null ? null : GrpCodeMapper.toDto(findOne);
        } catch (Exception e) {
            log.error("[findGrpOne] error: {}", e.getMessage());
            return null;
        }
    }

    // 그룹코드 조회 - by grpcodeName
    public GrpCodeDto findGrpCodeByName(String grpcdName) {
        try {
            GrpCode findOne = grpCodeRepository.findByGrpCodeName(grpcdName);
            return findOne == null ? null : GrpCodeMapper.toDto(findOne);
        } catch (Exception e) {
            log.error("[findGrpCodeByName] error: {}", e.getMessage());
            return null;
        }
    }

    // 그룹코드 조회 - 전체
    public List<CodeDto.GrpCodeDto> findGrpCodeAll() {
        try {
            List<GrpCode> grpcdList = grpCodeRepository.findAll();
            return grpcdList.isEmpty() ? Collections.emptyList() : GrpCodeMapper.toDtoList(grpcdList);
        } catch (Exception e) {
            log.error("[findGrpCodeAll] error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 그룹코드명의 일부분이 일치하는 그룹코드 조회
    public List<GrpCodeDto> findGrpCodeByGrpcdName(String grpcdName) {
        try {
            List<GrpCode> grpCodeList = grpCodeRepository.findByGrpcdNameLike(grpcdName);
            return GrpCodeMapper.toDtoList(grpCodeList);
        } catch (Exception e) {
            log.error("[findGrpCodeByGrpcdName] error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 공통코드 저장
    public void saveCommonCode(CodeDto.CommCodeDto cdto, String loginUserId) {
        try {
            cdto.setRegUserId(loginUserId);

            GrpCode grpfind = grpCodeRepository.findByGrpCode(cdto.getGrpCode());
    
            // dto >> entity
            CommonCode commonCode = CommonCodeMapper.toEntity(cdto, grpfind);
    
            commonCodeRepository.save(commonCode);
        } catch (Exception e) {
            log.error("[saveCommonCode] error: {}", e.getMessage());
        }
        
    }

    // 그룹,공통코드 저장
    public void saveGrpAndCommonCode(CodeDto.CommCodeDto cdto, GrpCodeDto gdto) {
        try {
            // dto >> entity
            GrpCode grpCode = GrpCodeMapper.toEntity(gdto);

            // dto >> entity
            CommonCode commonCode = CommonCodeMapper.toEntity(cdto, grpCode);

            commonCodeRepository.save(commonCode);
        } catch (Exception e) {
            log.error("[saveGrpAndCommonCode] error: {}", e.getMessage());
        }
    }

    // 공통코드 조회 (그룹코드, 공통코드)
    public CodeDto.CommCodeDto findCommonOne(String grpcd, String commoncd) {
        try {
            CommonCode findOne = commonCodeRepository.findCommonCodeOne(grpcd, commoncd);
            return CommonCodeMapper.toDto(findOne);
        } catch (Exception e) {
            log.error("[findCommonOne] error: {}", e.getMessage());
            return null;
        }
    }

    // 공통코드 조회 - 전체
    public List<CodeDto.CommCodeDto> findCommonCodeAll() {
        try {
            List<CommonCode> findList = commonCodeRepository.findAll();
            return findList.isEmpty() ? Collections.emptyList() : CommonCodeMapper.toDtoList(findList);
        } catch (Exception e) {
            log.error("[findCommonCodeAll] error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 공통코드 조회 - 조건(그룹코드)
    public List<CommonCode> findCommonCdListWithGrpCode(String grpcd) {
        return commonCodeRepository.findAllByGrpCode(grpcd);
    }

    // 공통코드 조회
    public CodeDto.CommCodeDto findCommCdGrpCd(String grpcd, String commoncd) {
        try {
            CommonCode findOne = commonCodeRepository.findCommonCodeOne(grpcd, commoncd);
            return CommonCodeMapper.toDto(findOne);
        } catch (Exception e) {
            log.error("[findCommonOne] error: {}", e.getMessage());
            return null;
        }
    }

    // 공통코드 조회 - 그룹코드 선택 시
    @Transactional(readOnly = true)
    public List<CommCodeDto> findCommonCdByGrpCd(String grpcd) {
        List<CommonCode> commonCodes = commonCodeRepository.findAllByGrpCode(grpcd);

        if (commonCodes.isEmpty())
            return Collections.emptyList();

        // entity >> dto
        return CommonCodeMapper.toDtoList(commonCodes);
    }

    public List<CommCdBaseDto> findCommonCdNamesByGrpcd(String grpcd) {
        return commonCodeRepository.findCommonCdNamesByGrpCode(grpcd);
    }

    // 공통코드 - 문자열 >> 정수
    public List<CommCdBaseDto> commonCodeStringToNum(String grpcd) {
        return commonCodeRepository.commonCodeStringSort(grpcd);
    }

    // 공통코드 - 메뉴권한
    public List<CommCdBaseDto> commonCodeMenuAuthority(String grpcd) {
        return commonCodeRepository.commonCodeMenuAuthority(grpcd);
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
    public CodeDto.CommCodeDto updateCommonCodeInfo(CommCodeDto cdto, String loginUserId) {
        cdto.setModUserId(loginUserId);

        // @Transactional 어노테이션으로 조회시 영속성 상태로 전환
        CommonCode findOne = commonCodeRepository.findCommonCodeOne(cdto.getGrpCode(), cdto.getCommonCode());

        log.info("=== before update: {}", findOne.toString());

        findOne.updateCommonCodeName(cdto);

        log.info("=== after update: {}", findOne.toString());

        return CommonCodeMapper.toDto(findOne);
    }

    /*
     * 삭제 - 그룹코드
     */
    @Transactional
    public void deleteGroupCode(String grpCode) {
        try {
            // 공통코드 먼저 삭제(그룹코드 FK로 참조하고 있기 때문)
            List<CommonCode> common = commonCodeRepository.findAllByGrpCode(grpCode);
            if (common.size() > 0) {
                Long count = commonCodeRepository.deleteAllCommonCodeByGrpCode(grpCode);
                log.info("=== commoncode deleted : {}", count);
            }

            Long grpcdDelCount = grpCodeRepository.deleteGrpCode(grpCode);
            log.info("=== grpcode deleted : {}", grpcdDelCount);
        } catch (Exception e) {
            log.error("[deleteGroupCode] error: {}", e.getMessage());
        }
    }

    @Transactional
    public void deleteCommonCode(String commonCode) {
        try {
            Long count = commonCodeRepository.deleteCommonCodeOne(commonCode);
            log.info("===commoncode deleted : {} ", count);
        } catch (Exception e) {
            log.error("[deleteCommonCode] error: {}", e.getMessage());
        }
    }

    /*
     * 그룹코드 - 그룹코드명 수정
     */
    @Transactional
    public GrpCodeDto updateGrpCode(GrpCodeDto grpcode, String loginUserId) {
        try {
            grpcode.setModUserId(loginUserId);

            GrpCode findOne = grpCodeRepository.findByGrpCode(grpcode.getGrpCode());
    
            log.info("=== before update: {}", findOne.toString());
    
            findOne.updateGrpcdCode(grpcode);
    
            log.info("=== after update: {}", findOne.toString());
    
            return GrpCodeMapper.toDto(findOne);
        } catch (Exception e) {
            log.error("[updateGrpCode] error: {}", e.getMessage());
            return null;
        }
    }
}
