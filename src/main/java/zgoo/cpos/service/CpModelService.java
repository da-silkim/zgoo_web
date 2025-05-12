package zgoo.cpos.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.cp.CpConnector;
import zgoo.cpos.domain.cp.CpModel;
import zgoo.cpos.domain.cp.CpModelDetail;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.cp.CpModelDto.CpConnectorDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelDetailDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelListDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelRegDto;
import zgoo.cpos.mapper.CpModelMapper;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.cpmodel.CpConnectorRepository;
import zgoo.cpos.repository.cpmodel.CpModelDetailRepository;
import zgoo.cpos.repository.cpmodel.CpModelRepository;
import zgoo.cpos.repository.users.UsersRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpModelService {

    private final UsersRepository usersRepository;
    private final CompanyRepository companyRepository;
    private final CpModelRepository cpModelRepository;
    private final CpModelDetailRepository cpModelDetailRepository;
    private final CpConnectorRepository cpConnectorRepository;
    private final ComService comService;

    // 충전기 모델 조회
    public Page<CpModelListDto> findCpModelInfoWithPagination(Long companyId, String manuf, String chgSpeed, int page,
            int size, String userId) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return Page.empty(pageable);
            }

            Page<CpModelListDto> modelList;

            if (companyId == null && (manuf == null || manuf.isEmpty()) && (chgSpeed == null || chgSpeed.isEmpty())) {
                log.info("Executing the [findCpModelWithPagination]"); // 전체 조회
                modelList = this.cpModelRepository.findCpModelWithPagination(pageable, levelPath, isSuperAdmin);
            } else {
                log.info("Executing the [searchCpModelWithPagination]"); // 검색 조회
                modelList = this.cpModelRepository.searchCpModelWithPagination(companyId, manuf, chgSpeed, pageable,
                        levelPath, isSuperAdmin);
            }

            return modelList;
        } catch (Exception e) {
            log.error("[findCpModelInfoWithPagination] error: {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // 충전기 모델 단건 조회
    public CpModelRegDto findCpModelOne(Long modelId) {
        try {
            CpModelRegDto cpModel = this.cpModelRepository.findCpModelOne(modelId);
            return cpModel;
        } catch (Exception e) {
            log.error("[findCpModelOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 충전기 모델 단건 조회(모달용)
    public CpModelListDto findCpModelModalOne(String modelCode) {
        try {
            CpModelListDto cpModel = this.cpModelRepository.findCpModelModalOne(modelCode);
            return cpModel;
        } catch (Exception e) {
            log.error("[findCpModelModalOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 충전기 모델 list 조회 by companyId
    public List<CpModelListDto> findCpModelListByCompany(Long companyId) {
        try {
            List<CpModelListDto> cpModelList = this.cpModelRepository.findCpModelListByCompanyId(companyId);
            return cpModelList;
        } catch (Exception e) {
            log.error("[findCpModelListByCompany] error : {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 충전기 모델 단건 조회(detail)
    public CpModelDetailDto findCpModelDetailOne(Long modelId) {
        try {
            CpModelDetailDto cpModel = this.cpModelRepository.findCpModelDetailOne(modelId);
            return cpModel;
        } catch (Exception e) {
            log.error("[findCpModelDetailOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 충전기 모델 저장
    @Transactional
    public void saveCpModelInfo(CpModelRegDto dto) {
        try {
            Users user = this.usersRepository.findUserOne(dto.getUserId());
            Company company = this.companyRepository.findCompanyOne(user.getCompany().getId());
            CpModel model = CpModelMapper.toEntityModel(dto, company);
            CpModel saved = this.cpModelRepository.save(model);

            CpModelDetail modelDetail = CpModelMapper.toEntityModelDetail(dto, saved);
            this.cpModelDetailRepository.save(modelDetail);

            saveConnectorInfo(saved, dto.getConnector());
        } catch (Exception e) {
            log.error("[saveCpModelInfo] error: {}", e.getMessage());
        }
    }

    // 충전기 커넥터 저장
    public void saveConnectorInfo(CpModel model, List<CpConnectorDto> dtos) {
        for (CpConnectorDto dto : dtos) {
            CpConnector connector = CpModelMapper.toEntityConnector(dto, model);
            this.cpConnectorRepository.save(connector);
        }
    }

    // 충전기 모델 수정
    @Transactional
    public CpModel updateCpModelInfo(CpModelRegDto dto, Long modelId) {
        CpModel model = this.cpModelRepository.findById(modelId)
                .orElseThrow(() -> new IllegalArgumentException("cp model not found with id: " + modelId));
        try {
            if (model != null)
                model.updateCpModelInfo(dto);
        } catch (Exception e) {
            log.error("[updateCpModelInfo] error: {}", e.getMessage());
        }
        return model;
    }

    // 충전기 모델 상세 수정
    @Transactional
    public void updateCpModelDetailInfo(CpModelRegDto dto, CpModel model) {
        try {
            CpModelDetail modelDetail = this.cpModelDetailRepository.findByModelId(model.getId());

            if (modelDetail == null) {
                log.info("No information CpModelDetailInfo.. create new cp model detail info start");

                CpModelDetail saved = CpModelMapper.toEntityModelDetail(dto, model);
                this.cpModelDetailRepository.save(saved);
            } else {
                modelDetail.updateCpModelDetailInfo(dto);
            }

        } catch (Exception e) {
            log.error("[updateCpModelDetailInfo] error: {}", e.getMessage());
        }
    }

    // 충전기 커넥터 수정
    @Transactional
    public void updateCpConnectorInfo(CpModelRegDto dto, CpModel model) {
        try {
            if (dto.getConnector() != null && !dto.getConnector().isEmpty()) {
                List<CpConnector> connectorList = this.cpConnectorRepository.findAllByModelId(model.getId());
                log.info("=== updateCpConnectorInfo >> count: {}", connectorList.size());

                // 업데이트와 추가 처리를 위한 Set
                Set<Integer> updatedConnectorId = new HashSet<>();

                for (CpConnectorDto connectorDto : dto.getConnector()) {
                    CpConnector matchedOne = connectorList.stream()
                            .filter(c -> c.getConnectorId().equals(connectorDto.getConnectorId()))
                            .findFirst()
                            .orElse(null);

                    if (matchedOne != null) {
                        matchedOne.updateCpConnectorInfo(connectorDto);
                        updatedConnectorId.add(connectorDto.getConnectorId());
                    } else {
                        // 매칭되는 커넥터 정보가 없으면 새로 추가
                        CpConnector newConnector = CpConnector.builder()
                                .connectorId(connectorDto.getConnectorId())
                                .connectorType(connectorDto.getConnectorType())
                                .cpModel(model)
                                .build();
                        this.cpConnectorRepository.save(newConnector);
                    }
                }

                // dto에 없는 기존 커넥터 정보 삭제
                for (CpConnector connector : connectorList) {
                    if (!updatedConnectorId.contains(connector.getConnectorId())) {
                        this.cpConnectorRepository.delete(connector);
                    }
                }
            } else {
                // dto 커넥터 정보가 없을 경우 기존 커넥터 정보 모두 삭제
                this.cpConnectorRepository.deleteAllByModelId(model.getId());
            }
        } catch (Exception e) {
            log.error("[updateCpConnectorInfo] error: {}", e.getMessage());
        }
    }

    // 충전기 모델 삭제
    @Transactional
    public void deleteCpModel(Long modelId) {
        CpModel model = this.cpModelRepository.findById(modelId)
                .orElseThrow(() -> new IllegalArgumentException("cp model not found with id: " + modelId));

        try {
            this.cpConnectorRepository.deleteAllByModelId(modelId);
            this.cpModelDetailRepository.deleteByModelId(modelId);
            this.cpModelRepository.deleteById(modelId);
            log.info("==== modelId: {} is deleted..", modelId);
        } catch (Exception e) {
            log.error("[deleteCpModel] error: {}", e.getMessage());
        }
    }
}
