package zgoo.cpos.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.company.CompanyContract;
import zgoo.cpos.domain.company.CompanyPG;
import zgoo.cpos.domain.company.CompanyRelationInfo;
import zgoo.cpos.domain.company.CompanyRoaming;
import zgoo.cpos.dto.company.CompanyDto;
import zgoo.cpos.dto.company.CompanyDto.BaseCompnayDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyListDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyRegDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyRoamingtDto;
import zgoo.cpos.mapper.CompanyMapper;
import zgoo.cpos.repository.company.CompanyContractRepository;
import zgoo.cpos.repository.company.CompanyPgRepository;
import zgoo.cpos.repository.company.CompanyRelationInfoRepository;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.company.CompanyRoamingRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyRelationInfoRepository companyRelationInfoRepository;
    private final CompanyRoamingRepository companyRoamingRepository;
    private final CompanyContractRepository companyContractRepository;
    private final CompanyPgRepository companyPgRepository;
    private final ComService comService;

    /*
     * 조회(사업자리스트 - CompanyDto.CompanyListDto)
     */
    @Transactional(readOnly = true)
    public Page<CompanyDto.CompanyListDto> searchCompanyAll(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        boolean isSuperAdmin = comService.checkSuperAdmin(userId);
        Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
        String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
        log.info("== levelPath : {}", levelPath);
        if (levelPath == null) {
            // 관계정보가 없을경우 빈 리스트 전달
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        return companyRepository.findCompanyListPaging(pageable, levelPath, isSuperAdmin);
    }

    public Page<CompanyListDto> searchCompanyById(Long companyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return companyRepository.findCompanyListByIdPaging(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CompanyListDto> searchCompanyByType(String userId, String companyType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        boolean isSuperAdmin = comService.checkSuperAdmin(userId);
        Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
        String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
        log.info("== levelPath : {}", levelPath);
        if (levelPath == null) {
            // 관계정보가 없을경우 빈 리스트 전달
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return companyRepository.findCompanyListByTypePaging(companyType, levelPath, pageable, isSuperAdmin);

    }

    @Transactional(readOnly = true)
    public Page<CompanyListDto> searchCompanyByLevel(String userId, String companyLevel, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        boolean isSuperAdmin = comService.checkSuperAdmin(userId);
        Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
        String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
        log.info("== levelPath : {}", levelPath);
        if (levelPath == null) {
            // 관계정보가 없을경우 빈 리스트 전달
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return companyRepository.findCompanyListByLvPaging(companyLevel, levelPath, pageable, isSuperAdmin);

    }

    @Transactional(readOnly = true)
    public List<BaseCompnayDto> searchAllCompanyForSelectOpt(String userId) {

        boolean isSuperAdmin = comService.checkSuperAdmin(userId);

        Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);

        String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
        log.info("== levelPath : {}", levelPath);
        if (levelPath == null) {
            return new ArrayList<>();
        }

        return companyRepository.findCompanyListForSelectOptBc(levelPath, isSuperAdmin);

    }

    // 조회 - 사업자ID(업데이트 항목 조회시 사용)
    @Transactional
    public CompanyRegDto searchCompanyById(String companyId) {

        Optional<Company> company = companyRepository.findById(Long.parseLong(companyId));
        List<CompanyRoamingtDto> roamingInfo = companyRoamingRepository
                .findAllByCompanyIdDto(Long.parseLong(companyId));

        if (company.isPresent()) {
            Company entity = company.get();

            CompanyRegDto dto = CompanyRegDto.builder()
                    .companyId(entity.getId())
                    .companyName(entity.getCompanyName())
                    .companyLv(entity.getCompanyLv())
                    .companyType(entity.getCompanyType())
                    .bizNum(entity.getBizNum())
                    .ceoName(entity.getCeoName())
                    .headPhone(entity.getHeadPhone())
                    .bizKind(entity.getBizKind())
                    .bizType(entity.getBizType())
                    .zipcode(entity.getZipcode())
                    .address(entity.getAddress())
                    .addressDetail(entity.getAddressDetail())
                    .consignmentPayment(entity.getConsignmentPayment())
                    .parentCompanyId(Optional.ofNullable(entity.getCompanyRelationInfo())
                            .map(r -> r.getParentCompany())
                            .map(Company::getId)
                            .orElse(null))
                    .parentCompanyName(Optional.ofNullable(entity.getCompanyRelationInfo())
                            .map(r -> r.getParentCompany())
                            .map(Company::getCompanyName)
                            .orElse(null))
                    .levelPath(Optional.ofNullable(entity.getCompanyRelationInfo())
                            .map(CompanyRelationInfo::getLevelPath)
                            .orElse(null))
                    .staffName(entity.getStaffName())
                    .staffTel(entity.getStaffTel())
                    .staffPhone(entity.getStaffPhone())
                    .staffEmail(entity.getStaffEmail())
                    .romaing(roamingInfo)
                    .logoUrl(entity.getLogoUrl())
                    .companyCode(entity.getCompanyCode())
                    .mid(Optional.ofNullable(entity.getCompanyPG())
                            .map(CompanyPG::getMid)
                            .orElse(null))
                    .merchantKey(Optional.ofNullable(entity.getCompanyPG())
                            .map(CompanyPG::getMerchantKey)
                            .orElse(null))
                    .sspMallId(Optional.ofNullable(entity.getCompanyPG())
                            .map(CompanyPG::getSspMallId)
                            .orElse(null))
                    .contractStatus(Optional.ofNullable(entity.getCompanyContract())
                            .map(CompanyContract::getContractStatus)
                            .orElse(null))
                    .contractedAt(Optional.ofNullable(entity.getCompanyContract())
                            .map(CompanyContract::getContractedAt)
                            .orElse(null))
                    .contractStart(Optional.ofNullable(entity.getCompanyContract())
                            .map(CompanyContract::getContractStart)
                            .orElse(null))
                    .contractEnd(Optional.ofNullable(entity.getCompanyContract())
                            .map(CompanyContract::getContractEnd)
                            .orElse(null))
                    .asCompany(Optional.ofNullable(entity.getCompanyContract())
                            .map(CompanyContract::getAsCompany)
                            .orElse(null))
                    .asNum(Optional.ofNullable(entity.getCompanyContract())
                            .map(CompanyContract::getAsNum)
                            .orElse(null))
                    .build();

            return dto;
        }
        throw new EntityNotFoundException("Company not found for ID: " + companyId);
    }

    /**
     * 저장
     */
    @Transactional
    public Company saveCompany(CompanyDto.CompanyRegDto dto) {
        log.info("== company dto : {}", dto.toString());

        // BizType 값 확인
        log.info("bizType value: '{}'", dto.getBizType());

        // 1. 회사 기본 정보 생성
        Company company = Company.builder()
                .companyName(dto.getCompanyName())
                .companyLv(dto.getCompanyLv())
                .companyType(dto.getCompanyType())
                .bizNum(dto.getBizNum())
                .ceoName(dto.getCeoName())
                .headPhone(dto.getHeadPhone())
                .bizKind(dto.getBizKind())
                .bizType(dto.getBizType())
                .zipcode(dto.getZipcode())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .consignmentPayment(dto.getConsignmentPayment())
                .logoUrl(dto.getLogoUrl())
                .companyCode(dto.getCompanyCode())
                .staffName(dto.getStaffName())
                .staffEmail(dto.getStaffEmail())
                .staffTel(dto.getStaffTel())
                .staffPhone(dto.getStaffPhone())
                .createdAt(LocalDateTime.now())
                .build();

        // 2. 계약 정보 생성 및 저장
        CompanyContract contract = CompanyContract.builder()
                .contractStatus(dto.getContractStatus())
                .contractedAt(LocalDateTime.now())
                .contractStart(dto.getContractStart())
                .contractEnd(dto.getContractEnd())
                .asCompany(dto.getAsCompany())
                .asNum(dto.getAsNum())
                .build();
        contract = companyContractRepository.save(contract);
        company.setCompanyContract(contract);

        // 3. PG 정보 생성 및 저장
        CompanyPG pg = CompanyPG.builder()
                .mid(dto.getMid())
                .merchantKey(dto.getMerchantKey())
                .sspMallId(dto.getSspMallId())
                .regDt(LocalDateTime.now())
                .build();
        pg = companyPgRepository.save(pg);
        company.setCompanyPG(pg);

        // 4. 관계 정보 생성 및 저장
        CompanyRelationInfo relationInfo = new CompanyRelationInfo();

        // 부모 회사 설정
        Company parentCompany = null;
        if (dto.getParentCompanyId() != null) {
            parentCompany = companyRepository.findById(dto.getParentCompanyId()).orElse(null);
        } else if (dto.getParentCompanyName() != null && !dto.getParentCompanyName().isEmpty()) {
            // 부모 회사 이름으로 조회
            parentCompany = companyRepository.findByCompanyName(dto.getParentCompanyName()).orElse(null);
        }

        relationInfo.setParentCompany(parentCompany);
        relationInfo = companyRelationInfoRepository.save(relationInfo);
        company.setCompanyRelationInfo(relationInfo);

        // 회사 저장 (ID 확보)
        company = companyRepository.save(company);

        // levelPath 설정 (회사 ID가 필요하므로 회사 저장 후 설정)
        if (parentCompany != null && parentCompany.getCompanyRelationInfo() != null) {
            String parentPath = parentCompany.getCompanyRelationInfo().getLevelPath();
            if (parentPath != null && !parentPath.isEmpty()) {
                relationInfo.setLevelPath(parentPath + "." + company.getId());
            } else {
                relationInfo.setLevelPath(parentCompany.getId() + "." + company.getId());
            }
        } else {
            relationInfo.setLevelPath(company.getId().toString());
        }

        // 관계 정보 다시 저장 (levelPath 업데이트)
        relationInfo = companyRelationInfoRepository.save(relationInfo);

        // 5. 로밍 정보 생성 및 저장
        if (dto.getRomaing() != null && !dto.getRomaing().isEmpty()) {
            for (CompanyRoamingtDto roamingDto : dto.getRomaing()) {
                CompanyRoaming roaming = CompanyRoaming.builder()
                        .institutionCode(roamingDto.getInstitutionCode())
                        .institutionKey(roamingDto.getInstitutionKey())
                        .institutionEmail(roamingDto.getInstitutionEmail())
                        .company(company)
                        .build();
                companyRoamingRepository.save(roaming);
            }
        }

        // 최종 회사 정보 저장
        company = companyRepository.save(company);
        log.info(">>> Company saved : {}", company);

        return company;
    }

    // 업체 로밍정보 저장
    public void saveCompanyRoamingInfo(Company company, List<CompanyRoamingtDto> dtos) {

        for (CompanyRoamingtDto dto : dtos) {

            CompanyRoaming companyRoaming = CompanyMapper.toEntityRoaming(dto, company);

            // db save
            companyRoamingRepository.save(companyRoaming);
        }

    }

    /*
     * 수정
     */
    @Transactional
    public void updateCompanyAll(CompanyRegDto dto) {
        log.info("== updateCompanyAll: {}", dto.getCompanyId());

        // 회사 정보 조회
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("회사 정보가 없습니다."));

        // 1. 기본 회사 정보 업데이트
        updateBasicInfo(dto, company);

        // 2. 관계 정보 업데이트
        CompanyRelationInfo relationInfo = null;
        if (company.getCompanyRelationInfo() != null) {
            relationInfo = company.getCompanyRelationInfo();
        } else {
            relationInfo = new CompanyRelationInfo();
        }

        // 부모 회사 설정
        Company parentCompany = null;
        if (dto.getParentCompanyId() != null) {
            parentCompany = companyRepository.findById(dto.getParentCompanyId()).orElse(null);
        } else if (dto.getParentCompanyName() != null && !dto.getParentCompanyName().isEmpty()) {
            // 부모 회사 이름으로 조회
            parentCompany = companyRepository.findByCompanyName(dto.getParentCompanyName()).orElse(null);
        }

        relationInfo.setParentCompany(parentCompany);

        // levelPath 설정
        if (parentCompany != null && parentCompany.getCompanyRelationInfo() != null) {
            String parentPath = parentCompany.getCompanyRelationInfo().getLevelPath();
            if (parentPath != null && !parentPath.isEmpty()) {
                relationInfo.setLevelPath(parentPath + "." + company.getId());
            } else {
                relationInfo.setLevelPath(parentCompany.getId() + "." + company.getId());
            }
        } else {
            relationInfo.setLevelPath(company.getId().toString());
        }

        // 관계 정보 저장
        relationInfo = companyRelationInfoRepository.save(relationInfo);
        company.setCompanyRelationInfo(relationInfo);

        // 3. 계약 정보 업데이트
        try {
            if (company.getCompanyContract() == null) {
                log.info("== companyId:{} has no contract info", company.getId());
                CompanyContract contract = new CompanyContract();
                contract.updateContractInfo(dto);
                contract = companyContractRepository.save(contract);
                company.setCompanyContract(contract);
            } else {
                company.getCompanyContract().updateContractInfo(dto);
            }
        } catch (Exception e) {
            log.error("Update contract info error", e);
        }

        // 4. PG 정보 업데이트
        try {
            if (company.getCompanyPG() == null) {
                log.info("== companyId:{} has no PG info", company.getId());
                CompanyPG pg = new CompanyPG();
                pg.updatePgInfo(dto);
                pg.setRegDt(LocalDateTime.now());
                pg = companyPgRepository.save(pg);
                company.setCompanyPG(pg);
            } else {
                company.getCompanyPG().updatePgInfo(dto);
            }
        } catch (Exception e) {
            log.error("Update PG info error", e);
        }

        // 5. 로밍 정보 업데이트
        try {
            companyRoamingRepository.deleteAllByCompanyId(company.getId());
            if (dto.getRomaing() != null && !dto.getRomaing().isEmpty()) {
                for (CompanyRoamingtDto roamingDto : dto.getRomaing()) {
                    CompanyRoaming roaming = CompanyRoaming.builder()
                            .institutionCode(roamingDto.getInstitutionCode())
                            .institutionKey(roamingDto.getInstitutionKey())
                            .institutionEmail(roamingDto.getInstitutionEmail())
                            .company(company)
                            .build();
                    companyRoamingRepository.save(roaming);
                }
            }
        } catch (Exception e) {
            log.error("Update roaming info error", e);
        }

        // 최종 회사 정보 저장
        companyRepository.save(company);
    }

    /**
     * 회사 기본 정보 업데이트
     */
    private void updateBasicInfo(CompanyRegDto dto, Company company) {
        company.setCompanyName(dto.getCompanyName());
        company.setCompanyLv(dto.getCompanyLv());
        company.setCompanyType(dto.getCompanyType());
        company.setBizNum(dto.getBizNum());
        company.setCeoName(dto.getCeoName());
        company.setHeadPhone(dto.getHeadPhone());
        company.setBizKind(dto.getBizKind());
        company.setBizType(dto.getBizType());
        company.setZipcode(dto.getZipcode());
        company.setAddress(dto.getAddress());
        company.setAddressDetail(dto.getAddressDetail());
        company.setConsignmentPayment(dto.getConsignmentPayment());
        company.setLogoUrl(dto.getLogoUrl());
        company.setCompanyCode(dto.getCompanyCode());
        company.setStaffName(dto.getStaffName());
        company.setStaffEmail(dto.getStaffEmail());
        company.setStaffTel(dto.getStaffTel());
        company.setStaffPhone(dto.getStaffPhone());
        company.setUpdatedAt(LocalDateTime.now());
    }

    /*
     * 삭제
     */
    @Transactional
    public void deleteCompany(Long compnayId) {
        Company company = companyRepository.findById(compnayId).orElseThrow(
                () -> new IllegalArgumentException("Company not found with id: " + compnayId));

        try {
            // 1. delete roaming info related that company
            companyRoamingRepository.deleteAllByCompanyId(compnayId);

            // 2. delete company
            companyRepository.deleteById(compnayId);

            log.info("==== companyID:{} is deleted..", compnayId);

        } catch (Exception e) {
            log.error("delete company Error: ", e);
        }

    }

    // List 형태로 company 전체 조회
    @Transactional(readOnly = true)
    public List<CompanyListDto> findCompanyListAll(String userId) {
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);

            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);

            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                return new ArrayList<>();
            }

            return companyRepository.findCompanyListForSelectOptCl(levelPath, isSuperAdmin);
        } catch (Exception e) {
            log.error("[findCompanyListAll] error: {}", e.getMessage());
            return null;
        }
    }
}
