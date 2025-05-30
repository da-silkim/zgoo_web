package zgoo.cpos.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.fw.CpFwVersion;
import zgoo.cpos.dto.fw.CpFwversionDto;
import zgoo.cpos.dto.fw.FwVersionRegDto;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.cpmodel.CpModelRepository;
import zgoo.cpos.repository.fw.CpFwVersionRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class FwService {

    private final CpFwVersionRepository cpFwVersionRepository;
    private final CompanyRepository companyRepository;
    private final ComService comService;
    private final CpModelRepository cpModelRepository;

    public Page<CpFwversionDto> findFwVersionList(int page, int size, String loginUserId) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return Page.empty(pageable);
            }

            Page<CpFwversionDto> fwVersionList = cpFwVersionRepository.findAll(pageable, levelPath, isSuperAdmin);
            log.info("===FwVersion_PageInfo >> totalPages:{}, totalCount:{}", fwVersionList.getTotalPages(),
                    fwVersionList.getTotalElements());

            return fwVersionList;
        } catch (Exception e) {
            log.error("Error occurred while fetching firmware version list: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    /**
     * 펌웨어 버전 등록
     * 
     * @param reqdto
     * @param loginUserId
     * @return
     */
    @Transactional
    public String registFwVersion(FwVersionRegDto reqdto, String loginUserId) {
        try {
            // 필수 데이터 검증
            if (reqdto.getCompanyId() == null || reqdto.getCpModelCode() == null ||
                    reqdto.getVersion() == null || reqdto.getUrl() == null) {
                log.error("필수 데이터가 누락되었습니다: {}", reqdto);
                return null;
            }

            // CpFwVersion 엔티티 생성 및 저장
            CpFwVersion fwVersion = CpFwVersion.builder()
                    .companyId(reqdto.getCompanyId())
                    .modelCode(reqdto.getCpModelCode())
                    .fwVersion(reqdto.getVersion())
                    .url(reqdto.getUrl())
                    .regId(loginUserId)
                    .regDate(LocalDateTime.now())
                    .build();

            cpFwVersionRepository.save(fwVersion);

            log.info("펌웨어 버전이 성공적으로 등록되었습니다: {}", fwVersion);
            return "success";
        } catch (Exception e) {
            log.error("펌웨어 버전 등록 중 오류 발생: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 펌웨어 버전 삭제
     * 
     * @param fwId
     */
    @Transactional
    public void deleteFwVersion(String fwId) {
        CpFwVersion fwVersion = cpFwVersionRepository.findById(Long.parseLong(fwId))
                .orElseThrow(() -> new IllegalArgumentException("fwId not found: " + fwId));
        log.info("=== delete fwversion data >> {}", fwVersion.toString());
        try {
            cpFwVersionRepository.deleteById(fwVersion.getId());
            log.info("펌웨어 버전이 성공적으로 삭제되었습니다: {}", fwId);
        } catch (Exception e) {
            log.error("펌웨어 버전 삭제 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    /**
     * 펌웨어 버전List 조회
     * 
     * @param companyId
     * @param modelCode
     * @return
     */
    public List<CpFwversionDto> findFwVersionListByCompanyAndModel(Long companyId, String modelCode) {
        try {
            List<CpFwversionDto> versionList = cpFwVersionRepository.findFwVersionListByCompanyAndModel(companyId,
                    modelCode);
            return versionList;
        } catch (Exception e) {
            log.error("펌웨어 버전 조회 중 오류 발생: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * 펌웨어 버전 url 조회
     * 
     * @param companyId
     * @param modelCode
     * @param version
     * @return
     */
    public String findFwUrlByCompanyAndModelAndVersion(Long companyId, String modelCode, String version) {
        try {
            String url = cpFwVersionRepository.findFwUrlByCompanyAndModelAndVersion(companyId, modelCode, version);
            return url;
        } catch (Exception e) {
            log.error("펌웨어 버전 url 조회 중 오류 발생: {}", e.getMessage(), e);
            return "";
        }
    }
}
