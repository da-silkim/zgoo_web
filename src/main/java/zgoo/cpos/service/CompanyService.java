package zgoo.cpos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.Company;
import zgoo.cpos.domain.CompanyRelationInfo;
import zgoo.cpos.dto.company.CompanyLvInfoDto;
import zgoo.cpos.dto.company.CompanyRegDto;
import zgoo.cpos.repository.CompanyRelationRepository;
import zgoo.cpos.repository.CompanyRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyRelationRepository companyRelationRepository;

    /**
     * 업체저장
     */
    @Transactional
    public void saveCompany(CompanyRegDto dto) {

        // dto > compnay entity
        Company company = new Company();
        company.setCompanyType(dto.getCompanyType());
        company.setCompanyLv(dto.getCompanyLv());
        company.setCompanyName(dto.getCompanyName());
        company.setBizNum(dto.getBizNum());
        company.setBizType(dto.getBizType());
        company.setBizKind(dto.getBizType());
        company.setCeoName(dto.getCeoName());
        company.setHeadPhone(dto.getHeadPhone());
        company.setZipcode(dto.getZipcode());
        company.setAddress(dto.getAddress());
        company.setAddressDetail(dto.getAddressDetail());
        company.setStaffName(dto.getStaffName());
        company.setStaffEmail(dto.getStaffEmail());
        company.setStaffTel(dto.getStaffTel());
        company.setStaffPhone(dto.getStaffPhone());
        company.setConsignmentPayment(dto.getConsignmentPayment());
        company.setCreatedAt(LocalDateTime.now());

        // dto >> companyrelation entity
        CompanyRelationInfo companyRelationInfo = new CompanyRelationInfo();
        companyRelationInfo.setParentId(dto.getParentId().isEmpty() ? null : Long.valueOf(dto.getParentId()));

        company.setCompanyRelationInfo(companyRelationInfo);

        companyRepository.save(company);

    }

    /*
     * 전체 업체 조회
     */
    public List<Company> findAllCompany() {
        return companyRepository.findAll();
    }

    /*
     * CompanyRelationInfo table 전체조회
     */
    public List<CompanyRelationInfo> findAllCompanyWithRelation() {
        return companyRelationRepository.findAll();
    }

    /*
     * 특정 업체 조회(id)
     */
    public Company findOne(Long companyId) {
        return companyRepository.findOne(companyId);
    }

    /*
     * 업체레벨 조건에따른 조회
     */
    public List<CompanyLvInfoDto> findCompanyByLv(String lv) {
        return companyRepository.findCompanyByLevel(lv);
    }

}
