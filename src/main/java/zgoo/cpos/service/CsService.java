package zgoo.cpos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
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
import zgoo.cpos.mapper.CsMapper;
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

    // 충전소 전체 조회
    @Transactional
    public Page<CsInfoListDto> findCsInfoAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CsInfoListDto> csList = this.csRepository.findCsInfoWithPagination(pageable);
            return csList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching csList with pagination: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching csList with pagination: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    // 충전소 검색 조회
    @Transactional
    public Page<CsInfoListDto> searchCsInfoListWithPagination(Long companyId, String searchOp, String searchContent, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CsInfoListDto> csList = this.csRepository.searchCsInfoWithPagination(companyId, searchOp, searchContent, pageable);
            return csList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching csList with pagination: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching csList with pagination: {}", e.getMessage(), e);
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

    // 이전글 조회
    public CsInfoDetailDto findPreviousCsInfo(String stationId, Long companyId, String searchOp, String searchContent) {
        try {
            CsInfoDetailDto csInfo = this.csRepository.findPreviousCsInfo(stationId, companyId, searchOp, searchContent);
            return csInfo;
        } catch (Exception e) {
            log.error("[findPreviousCsInfo] error : {}", e.getMessage());
            return null;
        }
    }

    // 다음글 조회
    public CsInfoDetailDto findNextCsInfo(String stationId, Long companyId, String searchOp, String searchContent) {
        try {
            CsInfoDetailDto csInfo = this.csRepository.findNextCsInfo(stationId, companyId, searchOp, searchContent);
            return csInfo;
        } catch (Exception e) {
            log.error("[findNextCsInfo] error : {}", e.getMessage());
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
            if (csInfo != null) {
                csInfo.updateCsInfo(dto);
            }
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
        Long count = this.csRepository.deleteCsInfoOne(stationId);
        log.info("=== delete cs Info: {}", count);
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
}
