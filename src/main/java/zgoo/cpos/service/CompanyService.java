package zgoo.cpos.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public List<BaseCompnayDto> searchAllCompanyForSelectOpt() {
        return companyRepository.findAllCompanyForSelectOpt();
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

        // bizType 값 검증 및 로깅
        if (dto.getBizType() == null || dto.getBizType().isEmpty()) {
            log.error("bizType is null or empty!");
            // 기본값 설정 또는 예외 처리
            dto.setBizType("CB"); // 기본값 설정
        }
        log.info("bizType value: '{}'", dto.getBizType());

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
    public void updateCompanyAll(CompanyRegDto dto) {
        // 한 번의 조회로 회사 정보 가져오기
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + dto.getCompanyId()));

        try {
            // 기본 정보 업데이트
            company.updateCompanyBasicInfo(dto);

            // 관계 정보 업데이트
            updateEntityInfo(company, dto,
                    company.getCompanyRelationInfo(),
                    CompanyMapper::toEntityRelation,
                    (c, r) -> c.toBuilder().companyRelationInfo(r).build(),
                    "relation");

            // 계약 정보 업데이트
            updateEntityInfo(company, dto,
                    company.getCompanyContract(),
                    CompanyMapper::toEntityContract,
                    (c, contract) -> c.toBuilder().companyContract(contract).build(),
                    "contract");

            // PG 정보 업데이트
            updateEntityInfo(company, dto,
                    company.getCompanyPG(),
                    CompanyMapper::toEntityPg,
                    (c, pg) -> c.toBuilder().companyPG(pg).build(),
                    "PG");

            // 로밍 정보 업데이트
            updateRoamingInfo(dto, company);

        } catch (Exception e) {
            log.error("Company update failed", e);
            throw e;
        }
    }

    /**
     * 엔티티 정보 업데이트를 위한 일반화된 메서드
     */
    private <T> void updateEntityInfo(Company company, CompanyRegDto dto,
            T entityInfo,
            Function<CompanyRegDto, T> toEntityMapper,
            BiFunction<Company, T, Company> companyUpdater,
            String infoType) {
        try {
            if (entityInfo == null) {
                log.info("== companyId:{} has no {} info", company.getId(), infoType);
                // 새 정보 생성
                T newEntity = toEntityMapper.apply(dto);
                Company updatedCompany = companyUpdater.apply(company, newEntity);
                companyRepository.save(updatedCompany);
            } else {
                // 기존 정보가 있는 경우 업데이트 로직
                // 이 부분은 각 엔티티 타입에 따라 다르므로 별도 처리 필요
                if (entityInfo instanceof CompanyRelationInfo) {
                    ((CompanyRelationInfo) entityInfo).updateRelationInfo(dto);
                } else if (entityInfo instanceof CompanyContract) {
                    ((CompanyContract) entityInfo).updateContractInfo(dto);
                } else if (entityInfo instanceof CompanyPG) {
                    ((CompanyPG) entityInfo).updatePgInfo(dto);
                }
            }
        } catch (Exception e) {
            log.error("Update {} info error", infoType, e);
        }
    }

    /**
     * 로밍 정보 업데이트를 위한 단순화된 메서드
     */
    private void updateRoamingInfo(CompanyRegDto dto, Company company) {
        try {
            if (dto.getRomaing() != null && !dto.getRomaing().isEmpty()) {
                List<CompanyRoaming> existingRoamings = companyRoamingRepository.findAllByCompanyId(company.getId());
                Map<String, CompanyRoaming> existingRoamingMap = existingRoamings.stream()
                        .collect(Collectors.toMap(CompanyRoaming::getInstitutionCode, r -> r));

                // 새로운 로밍 정보 처리
                Set<String> processedCodes = new HashSet<>();
                for (CompanyRoamingtDto roamingDto : dto.getRomaing()) {
                    String code = roamingDto.getInstitutionCode();
                    processedCodes.add(code);

                    if (existingRoamingMap.containsKey(code)) {
                        // 기존 정보 업데이트
                        existingRoamingMap.get(code).updateRoamingInfo(roamingDto);
                    } else {
                        // 새 정보 추가
                        companyRoamingRepository.save(CompanyRoaming.builder()
                                .institutionCode(code)
                                .institutionKey(roamingDto.getInstitutionKey())
                                .institutionEmail(roamingDto.getInstitutionEmail())
                                .company(company)
                                .build());
                    }
                }

                // 삭제할 로밍 정보 처리
                existingRoamings.stream()
                        .filter(r -> !processedCodes.contains(r.getInstitutionCode()))
                        .forEach(companyRoamingRepository::delete);
            } else {
                // 모든 로밍 정보 삭제
                companyRoamingRepository.deleteAllByCompanyId(company.getId());
            }
        } catch (Exception e) {
            log.error("Update roaming info error", e);
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
