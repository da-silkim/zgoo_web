package zgoo.cpos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.company.CompanyContract;
import zgoo.cpos.domain.company.CompanyPG;
import zgoo.cpos.domain.company.CompanyRelationInfo;
import zgoo.cpos.domain.company.CompanyRoaming;
import zgoo.cpos.dto.company.CompanyDto;
import zgoo.cpos.repository.company.CompanyRelationRepository;
import zgoo.cpos.repository.company.CompanyRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyRelationRepository companyRelationRepository;

    /*
     * 조회(사업자리스트 - CompanyDto.CompanyListDto)
     */
    public List<CompanyDto.CompanyListDto> searchCompanyList() {
        return companyRepository.findCompanyListAllCustom();
    }

    /**
     * 저장
     */
    public void saveCompany(CompanyDto.CompanyRegDto dto) {

        log.info("== company dto : {}", dto.toString());

        // dto >> entity(relation)
        CompanyRelationInfo companyRelationInfo = CompanyRelationInfo.builder()
                .parentId(dto.getParentId())
                .build();

        // dto >> entity(roaming)
        CompanyRoaming companyRoaming = CompanyRoaming.builder()
                .institutionCode(dto.getInstitutionCode())
                .institutionKey(dto.getInstitutionKey())
                .institutionEmail(dto.getInstitutionEmail())
                .build();

        // dto >> entity(pg)
        CompanyPG companyPG = CompanyPG.builder()
                .sspMallId(dto.getSspMallId())
                .mid(dto.getMid())
                .merchantKey(dto.getMerchantKey())
                .regDt(LocalDateTime.now())
                .build();

        // dto >> entity(contract)
        CompanyContract companyContract = CompanyContract.builder()
                .contractStatus(dto.getContractStatus())
                .contractedAt(LocalDateTime.now())
                .contractStart(dto.getContractStart())
                .contractEnd(dto.getContractEnd())
                .asCompany(dto.getAsCompany())
                .asNum(dto.getAsNum())
                .build();

        // dto >> entity(company)
        Company company = Company.builder()
                .companyType(dto.getCompanyType())
                .companyLv(dto.getCompanyLv())
                .companyName(dto.getCompanyName())
                .bizNum(dto.getBizNum())
                .ceoName(dto.getCeoName())
                .headPhone(dto.getHeadPhone())
                .bizKind(dto.getBizKind())
                .bizType(dto.getBizType())
                .zipcode(dto.getZipcode())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .staffName(dto.getStaffName())
                .staffEmail(dto.getStaffEmail())
                .staffTel(dto.getStaffTel())
                .staffPhone(dto.getStaffPhone())
                .consignmentPayment(dto.getConsignmentPayment())
                .createdAt(LocalDateTime.now())
                .companyRelationInfo(companyRelationInfo)
                .companyRoaming(companyRoaming)
                .companyPG(companyPG)
                .companyContract(companyContract)
                .build();

        Company saved = companyRepository.save(company);

        log.info(">>> Company saved : {}", saved.toString());

    }

    /*
     * 수정
     */

    /*
     * 삭제
     */

}
