package zgoo.cpos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.domain.cs.CsKepcoContractInfo;
import zgoo.cpos.domain.cs.CsLandInfo;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoDetailDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoListDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoRegDto;
import zgoo.cpos.dto.cs.CsInfoDto.StationOpStatusDto;
import zgoo.cpos.dto.cs.CsInfoDto.StationSearchDto;
import zgoo.cpos.mapper.CsMapper;
import zgoo.cpos.repository.charger.ChargerRepository;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.cs.CsKepcoContractInfoRepository;
import zgoo.cpos.repository.cs.CsLandInfoRepository;
import zgoo.cpos.repository.cs.CsRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsService {

    private final CsRepository csRepository;
    private final CompanyRepository companyRepository;
    private final CsKepcoContractInfoRepository csKepcoContractInfoRepository;
    private final CsLandInfoRepository csLandInfoRepository;
    private final ChargerRepository chargerRepository;
    private final ComService comService;

    // 충전소 조회
    public Page<CsInfoListDto> findCsInfoWithPagination(Long companyId, String searchOp, String searchContent, int page,
            int size, String userId) {
        Pageable pageable = PageRequest.of(page, size);

        try {

            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);

            Page<CsInfoListDto> csList;

            if (companyId == null && (searchOp == null || searchOp.isEmpty())
                    && (searchContent == null || searchContent.isEmpty())) {
                log.info("Executing the [findCsInfoWithPagination]");
                csList = this.csRepository.findCsInfoWithPagination(pageable, levelPath, isSuperAdmin);
            } else {
                log.info("Executing the [searchCsInfoWithPagination]");
                csList = this.csRepository.searchCsInfoWithPagination(companyId, searchOp, searchContent, pageable,
                        levelPath, isSuperAdmin);
            }

            for (CsInfoListDto station : csList) {
                long cpCount = this.chargerRepository.countByStationId(station.getStationId(), levelPath, isSuperAdmin);
                station.setCpCount(cpCount);
            }

            return csList;
        } catch (Exception e) {
            log.error("[findCsInfoWithPagination] error: {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // 충전소 단건 조회
    public CsInfoRegDto findCsInfoOne(String stationId) {
        try {
            CsInfoRegDto csInfo = this.csRepository.findCsInfoOne(stationId);
            return csInfo;
        } catch (Exception e) {
            log.error("[findCsInfoOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 충전소 단건 조회(detail)
    public CsInfoDetailDto findCsInfoDetailOne(String stationId) {
        try {
            CsInfoDetailDto csInfo = this.csRepository.findCsInfoDetailOne(stationId);
            return csInfo;
        } catch (Exception e) {
            log.error("[findCsInfoDetailOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 충전소명 중복 검사
    public boolean isStationNameDuplicate(String stationName) {
        return this.csRepository.isStationNameDuplicate(stationName);
    }

    // 충전소 저장
    @Transactional
    public String saveCsInfo(CsInfoRegDto dto) {
        try {
            Company company = this.companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("company not found with id: " + dto.getCompanyId()));

            CsKepcoContractInfo kepcoInfo = CsMapper.toEntityKepco(dto);
            CsLandInfo landInfo = CsMapper.toEntityLand(dto);
            String lastCsId = this.csRepository.findRecentStationId(dto.getCompanyId());

            String stationId;
            if (lastCsId == null) {
                // 해당 사업자의 충전소 정보가 1건도 없음
                stationId = company.getCompanyCode() + "0001";
            } else {
                try {
                    // DA0001 형식에서 숫자 부분만 추출
                    int numericPart = Integer.parseInt(lastCsId.substring(2));
                    // 새로운 충전소 ID 생성, 숫자는 4자리로 포맷
                    stationId = String.format(company.getCompanyCode() + "%04d", numericPart + 1);
                } catch (NumberFormatException e) {
                    throw new IllegalStateException("잘못된 충전소 ID 형식입니다: " + lastCsId, e);
                }
            }

            CsInfo csInfo = CsMapper.toEntityCs(dto, stationId, company, landInfo, kepcoInfo);
            this.csRepository.save(csInfo);
            return stationId;
        } catch (NumberFormatException e) {
            log.error("[saveCsInfo] NumberFormatException: {}", e.getMessage());
            return null;
        } catch (IllegalStateException e) {
            log.error("[saveCsInfo] IllegalStateException: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("[saveCsInfo] error: {}", e.getMessage());
            return null;
        }
    }

    // 충전소 수정
    @Transactional
    public CsInfo updateCsInfo(CsInfoRegDto dto) {
        CsInfo csInfo = this.csRepository.findById(dto.getStationId())
                .orElseThrow(() -> new IllegalArgumentException("cs not found with id: " + dto.getStationId()));

        try {
            csInfo.updateCsInfo(dto);
            updateCsKepcoContractInfo(csInfo, dto);
            updateCsLandInfo(csInfo, dto);
        } catch (Exception e) {
            log.error("[updateCsInfo] error: {}", e.getMessage());
        }

        return csInfo;
    }

    @Transactional
    public void updateCsKepcoContractInfo(CsInfo csInfo, CsInfoRegDto dto) {
        try {
            if (csInfo.getCsKepcoContractInfo() == null) {
                log.info("No information CsKepcoContractInfo.. create new cs kepco info start");

                CsKepcoContractInfo kepco = CsMapper.toEntityKepco(dto);
                csInfo = csInfo.toBuilder()
                        .csKepcoContractInfo(kepco)
                        .build();
                this.csRepository.save(csInfo);
            } else {
                Long kepcoId = csInfo.getCsKepcoContractInfo().getId();
                CsKepcoContractInfo findOne = this.csKepcoContractInfoRepository.findById(kepcoId)
                        .orElseThrow(() -> new IllegalArgumentException("cs kepco Id not found: " + kepcoId));

                findOne.updateCsKepcoContractInfo(dto);
            }
        } catch (Exception e) {
            log.error("[updateCsKepcoContractInfo] error: {}", e.getMessage());
        }
    }

    @Transactional
    public void updateCsLandInfo(CsInfo csInfo, CsInfoRegDto dto) {
        try {
            if (csInfo.getCsLandInfo() == null) {
                log.info("No information CsLandInfo.. create new cs land info start");

                CsLandInfo land = CsMapper.toEntityLand(dto);
                csInfo = csInfo.toBuilder()
                        .csLandInfo(land)
                        .build();

                this.csRepository.save(csInfo);
            } else {
                Long landId = csInfo.getCsLandInfo().getId();
                CsLandInfo findOne = this.csLandInfoRepository.findById(landId)
                        .orElseThrow(() -> new IllegalArgumentException("cs land Id not found: " + landId));

                findOne.updateCsLandInfo(dto);
            }
        } catch (Exception e) {
            log.error("[updateCsLandInfo] error: {}", e.getMessage());
        }
    }

    // 충전소 삭제
    @Transactional
    public void deleteCsInfo(String stationId) {
        try {
            Long count = this.csRepository.deleteCsInfoOne(stationId);
            log.info("=== delete cs Info: {}", count);
        } catch (Exception e) {
            log.error("[deleteCsInfo] error: {}", e.getMessage());
        }
    }

    // 충전소 조회
    public List<CsInfoDetailDto> findCsInfo() {
        try {
            List<CsInfoDetailDto> csList = this.csRepository.findCsInfo();
            return csList;
        } catch (Exception e) {
            log.error("[findCsInfo] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 키워드로 충전소 조회
    public List<StationSearchDto> saerchStationList(String keyword, String userId) {
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);

            List<StationSearchDto> csList = this.csRepository.findCsInfoContainKeyword(keyword, levelPath,
                    isSuperAdmin);
            return csList;
        } catch (Exception e) {
            log.error("[saerchStationList] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 사용자 위치 기반, 주변 충전소 조회
    public List<CsInfoDetailDto> findNearbyStations(double latitude, double longitude, String userId) {
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);

            List<CsInfoDetailDto> csList = this.csRepository.findStationsWithinRadius(latitude, longitude, 10.0,
                    levelPath, isSuperAdmin); // 10km
                                              // 이내
            System.out.println("주변 충전소 >> " + csList.toString());
            return csList;
        } catch (Exception e) {
            log.error("[findNearbyStations] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 충전소 조회
    public List<CsInfoListDto> findAllStationWithoutPagination(Long companyId, String searchOp, String searchContent,
            String userId) {
        log.info("=== Finding all station list: searchOp={}, searchContent={} ===", searchOp, searchContent);

        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);

            List<CsInfoListDto> csList = this.csRepository.findAllStationWithoutPagination(companyId, searchOp,
                    searchContent, levelPath, isSuperAdmin);

            for (CsInfoListDto station : csList) {
                long cpCount = this.chargerRepository.countByStationId(station.getStationId(), levelPath, isSuperAdmin);
                station.setCpCount(cpCount);
            }

            return csList;
        } catch (Exception e) {
            log.error("[findAllStationWithoutPagination] error: {}", e.getMessage());
            return new ArrayList<>();
        }

    }

    public StationOpStatusDto getStationOpStatusCount(String userId) {
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);

            StationOpStatusDto dto = this.csRepository.getStationOpStatusCount(levelPath, isSuperAdmin);
            log.info("[getStationOpStatusCount] dto >> {}", dto.toString());
            Long total = dto.getOpStopCount() + dto.getOpTestCount() + dto.getOperatingCount();
            dto.setOpTestPer((double) dto.getOpTestCount() / total * 100);
            dto.setOpStopPer((double) dto.getOpStopCount() / total * 100);
            dto.setOperatingPer((double) dto.getOperatingCount() / total * 100);
            return dto;
        } catch (Exception e) {
            log.error("[getStationOpStatusCount] error: {}", e.getMessage());
            return null;
        }
    }

    public List<StationSearchDto> searchStationByOption(String searchOp, String searchContent, String userId) {
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);

            return this.csRepository.searchStationByOption(searchOp, searchContent, levelPath, isSuperAdmin);
        } catch (Exception e) {
            log.error("[searchStationByOption] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}
