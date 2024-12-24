package zgoo.cpos.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
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

    /*
     * 조회(사업자리스트 - CompanyDto.CompanyListDto)
     */
    public Page<CompanyDto.CompanyListDto> searchCompanyAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CompanyListDto> companylist = companyRepository.findCompanyListAllCustom(pageable);

        return companylist;
    }

    public Page<CompanyListDto> searchCompanyById(Long companyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return companyRepository.findCompanyListById(companyId, pageable);
    }

    public Page<CompanyListDto> searchCompanyByType(String companyType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return companyRepository.findCompanyListByType(companyType, pageable);
    }

    public Page<CompanyListDto> searchCompanyByLevel(String companyLevel, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return companyRepository.findCompanyListByLv(companyLevel, pageable);
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
                    .parentCompanyName(Optional.ofNullable(entity.getCompanyRelationInfo())
                            .map(CompanyRelationInfo::getParentCompanyName)
                            .orElse(null)) // Lazy 로딩 해결 및 null 처리
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
    // 업체 정보 저장
    public Company saveCompany(CompanyDto.CompanyRegDto dto) {

        log.info("== company dto : {}", dto.toString());

        // dto >> entity(relation)
        CompanyRelationInfo companyRelationInfo = CompanyMapper.toEntityRelation(dto);

        // dto >> entity(pg)
        CompanyPG companyPG = CompanyMapper.toEntityPg(dto);

        // dto >> entity(contract)
        CompanyContract companyContract = CompanyMapper.toEntityContract(dto);

        // dto >> entity(company)
        Company company = CompanyMapper.toEntityCompany(dto, companyRelationInfo, companyPG, companyContract);

        Company saved = companyRepository.save(company);

        saveCompanyRoamingInfo(saved, dto.getRomaing());

        log.info(">>> Company saved : {}", saved.toString());

        return saved;

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
    public Company updateCompanyInfo(CompanyRegDto dto) {
        // 업체 정보 조회
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + dto.getCompanyId()));
        try {
            if (company != null) {

                // company 기본정보 업데이트
                company.updateCompanyBasicInfo(dto);
            }

        } catch (Exception e) {
            log.error("updateCompanyInfo Error: ", e);
        }

        return company;

    }

    @Transactional
    public void updatePgInfo(Company company, CompanyRegDto dto) {
        try {
            if (company.getCompanyPG() == null) {
                log.info("No information Compnay PG.. create new pg info start");
                // pg정보 신규저장
                CompanyPG entityPg = CompanyMapper.toEntityPg(dto);
                company = company.toBuilder()
                        .companyPG(entityPg)
                        .build();

                companyRepository.save(company);
            } else {
                Long pgId = company.getCompanyPG().getId();
                CompanyPG findOne = companyPgRepository.findById(pgId)
                        .orElseThrow(() -> new IllegalArgumentException("Company PG ID not found: " + pgId));

                findOne.updatePgInfo(dto);
            }

        } catch (Exception e) {
            log.error("updatePgInfo Error", e);
        }
    }

    @Transactional
    public void updateContractInfo(Company company, CompanyRegDto dto) {

        try {
            if (company.getCompanyContract() == null) {
                log.info("== companyId:{} ,has no contract info", company.getId());
                // 계약정보 저장
                CompanyContract entityContract = CompanyMapper.toEntityContract(dto);
                company = company.toBuilder()
                        .companyContract(entityContract)
                        .build();

                companyRepository.save(company);
            } else {
                Long contractId = company.getCompanyContract().getId();
                CompanyContract findOne = companyContractRepository.findById(contractId).orElseThrow(
                        () -> new IllegalArgumentException("Company Contract not found with id: " + contractId));

                findOne.updateContractInfo(dto);
            }
        } catch (Exception e) {
            log.error("updateContractInfo Error", e);
        }
    }

    @Transactional
    public void updateRelationInfo(Company company, CompanyRegDto dto) {

        try {
            // 업체에 해당되는 관계정보 조회
            if (company.getCompanyRelationInfo() == null) {

                log.info("== companyId:{} ,has no relation info", company.getId());
                // 관계정보 저장
                CompanyRelationInfo entityRelation = CompanyMapper.toEntityRelation(dto);
                company = company.toBuilder()
                        .companyRelationInfo(entityRelation)
                        .build();

                companyRepository.save(company);

            } else {
                Long relationId = company.getCompanyRelationInfo().getId();
                CompanyRelationInfo findOnd = companyRelationInfoRepository.findById(relationId).orElseThrow(
                        () -> new IllegalArgumentException("Company Relation not found with id: " + relationId));

                findOnd.updateRelationInfo(dto);
            }

        } catch (Exception e) {
            log.error("updateRelationInfo Error", e);
        }

    }

    @Transactional
    public void updateCompanyRoamingInfo(CompanyRegDto dto, Company company) {
        try {
            // 로밍정보 업데이트
            if (dto.getRomaing() != null && !dto.getRomaing().isEmpty()) {

                List<CompanyRoaming> roamingList = companyRoamingRepository.findAllByCompanyId(dto.getCompanyId());
                log.info("=== updateCompanyRoamingInfo >> cnt:{}", roamingList.size());

                // 업데이트와 추가 처리를 위한 Set
                Set<String> updatedInstitutionCodes = new HashSet<>();

                for (CompanyRoamingtDto romaingDto : dto.getRomaing()) {
                    // 기존 로밍 정보 중 institutionCode로 매칭되는 것이 있는지 확인
                    CompanyRoaming matchedOne = roamingList.stream()
                            .filter(r -> r.getInstitutionCode().equals(romaingDto.getInstitutionCode()))
                            .findFirst()
                            .orElse(null);

                    if (matchedOne != null) {
                        matchedOne.updateRoamingInfo(romaingDto);
                        updatedInstitutionCodes.add(romaingDto.getInstitutionCode());

                    } else {
                        // 매칭되는 로밍정보 없으면 새로 추가
                        CompanyRoaming newroaming = CompanyRoaming.builder()
                                .institutionCode(romaingDto.getInstitutionCode())
                                .institutionKey(romaingDto.getInstitutionKey())
                                .institutionEmail(romaingDto.getInstitutionEmail())
                                .company(company)
                                .build();

                        companyRoamingRepository.save(newroaming);
                    }
                }

                // 2. dto에 없는 기존 로밍 정보를 삭제
                for (CompanyRoaming companyRoaming : roamingList) {
                    if (!updatedInstitutionCodes.contains(companyRoaming.getInstitutionCode())) {
                        companyRoamingRepository.delete(companyRoaming);
                    }
                }
            } else {
                // dto에 로밍정보가 없을 경우 기존 로밍정보 모두삭제
                companyRoamingRepository.deleteAllByCompanyId(company.getId());
            }
        } catch (Exception e) {
            log.error("updateCompanyRoamingInfo Error: ", e);
        }
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
    public List<CompanyListDto> findCompanyListAll() {
        try {
            return companyRepository.findCompanyListAll();
        } catch (Exception e) {
            log.error("[findCompanyListAll] error: {}", e.getMessage());
            return null;
        }
    }
}
