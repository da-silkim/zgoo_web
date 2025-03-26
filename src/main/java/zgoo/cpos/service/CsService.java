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
import zgoo.cpos.dto.cs.CsInfoDto.StationSearchDto;
import zgoo.cpos.mapper.CsMapper;
import zgoo.cpos.repository.charger.ChargerRepository;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.cs.CsKepcoContractInfoRepository;
import zgoo.cpos.repository.cs.CsLandInfoRepository;
import zgoo.cpos.repository.cs.CsRepository;
import zgoo.cpos.util.MenuConstants;

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
    public Page<CsInfoListDto> findCsInfoWithPagination(Long companyId, String searchOp, String searchContent, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CsInfoListDto> csList;

            if (companyId == null && (searchOp == null || searchOp.isEmpty()) && (searchContent == null || searchContent.isEmpty())) {
                log.info("Executing the [findCsInfoWithPagination]");
                csList = this.csRepository.findCsInfoWithPagination(pageable);
            } else {
                log.info("Executing the [findCsInfoWithPagination]");
                csList = this.csRepository.searchCsInfoWithPagination(companyId, searchOp, searchContent, pageable);
            }

            for (CsInfoListDto station : csList) {
                long cpCount = this.chargerRepository.countByStationId(station.getStationId());
                station.setCpCount(cpCount);
            }

            return csList;
        } catch (Exception e) {
            log.error("[findCsInfoWithPagination] error: {}", e.getMessage(), e);
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
    public String saveCsInfo(CsInfoRegDto dto, String loginUserId) {
        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException("User ID is missing. Cannot save charge station info without login user ID."); 
            }
            boolean isMod = comService.checkModYn(loginUserId, MenuConstants.STATION_LIST);
            if (!isMod) return null;

            CsKepcoContractInfo kepcoInfo = CsMapper.toEntityKepco(dto);
            CsLandInfo landInfo = CsMapper.toEntityLand(dto);
            Company company = this.companyRepository.findCompanyOne(dto.getCompanyId());
            String lastCsId = this.csRepository.findRecentStationId(dto.getCompanyId());

            String stationId;
            if (lastCsId == null) {
                // 해당 사업자의 충전소 정보가 1건도 없음
                stationId = company.getCompanyCode() + "0001";
            } else {
                // DA0001 형식에서 숫자 부분만 추출
                int numericPart = Integer.parseInt(lastCsId.substring(2));
                // 새로운 충전소 ID 생성, 숫자는 4자리로 포맷
                stationId = String.format(company.getCompanyCode() + "%04d", numericPart + 1);
            }

            CsInfo csInfo = CsMapper.toEntityCs(dto, stationId, company, landInfo, kepcoInfo);
            this.csRepository.save(csInfo);
            return stationId;
        } catch (IllegalArgumentException e) {
            log.error("[saveCsInfo] Illegal argument error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[saveCsInfo] error: {}", e.getMessage());
            return null;
        }
    }

    // 충전소 수정
    @Transactional
    public CsInfo updateCsInfo(CsInfoRegDto dto, String loginUserId) {
        CsInfo csInfo = this.csRepository.findById(dto.getStationId())
                .orElseThrow(() -> new IllegalArgumentException("cs not found with id: " + dto.getStationId()));

        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException("User ID is missing. Cannot update charge station info without login user ID."); 
            }
            boolean isMod = comService.checkModYn(loginUserId, MenuConstants.STATION_LIST);
            if (!isMod) return csInfo;

            csInfo.updateCsInfo(dto);
            updateCsKepcoContractInfo(csInfo, dto);
            updateCsLandInfo(csInfo, dto);
        } catch (IllegalArgumentException e) {
            log.error("[updateCsInfo] Illegal argument error: {}", e.getMessage());
            throw e;
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
    public void deleteCsInfo(String stationId, String loginUserId) {
        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException("User ID is missing. Cannot delete charge station without login user ID."); 
            }
            boolean isMod = comService.checkModYn(loginUserId, MenuConstants.STATION_LIST);
            if (!isMod) return;

            Long count = this.csRepository.deleteCsInfoOne(stationId);
            log.info("=== delete cs Info: {}", count);
        } catch (IllegalArgumentException e) {
            log.error("[deleteCsInfo] Illegal argument error: {}", e.getMessage());
            throw e;
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
    public List<StationSearchDto> saerchStationList(String keyword) {
        try {
            List<StationSearchDto> csList = this.csRepository.findCsInfoContainKeyword(keyword);
            return csList;
        } catch (Exception e) {
            log.error("[saerchStationList] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 사용자 위치 기반, 주변 충전소 조회
    public List<CsInfoDetailDto> findNearbyStations(double latitude, double longitude) {
        try {
            List<CsInfoDetailDto> csList = this.csRepository.findStationsWithinRadius(latitude, longitude, 10.0); // 10km 이내
            System.out.println("주변 충전소 >> " + csList.toString());
            return csList;
        } catch (Exception e) {
            log.error("[findNearbyStations] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}
