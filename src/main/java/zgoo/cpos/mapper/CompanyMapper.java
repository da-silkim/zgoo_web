package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.company.CompanyContract;
import zgoo.cpos.domain.company.CompanyPG;
import zgoo.cpos.domain.company.CompanyRelationInfo;
import zgoo.cpos.domain.company.CompanyRoaming;
import zgoo.cpos.dto.company.CompanyDto.CompanyRegDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyRoamingtDto;

public class CompanyMapper {
    /*
     * company(dto >> entity)
     */
    public static Company toEntityCompany(CompanyRegDto dto, CompanyRelationInfo rinfo, CompanyPG pginfo,
            CompanyContract contractinfo) {
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
                .companyRelationInfo(rinfo)
                .companyPG(pginfo)
                .companyContract(contractinfo)
                .logoUrl(dto.getLogoUrl())
                .companyCode(dto.getCompanyCode())
                .build();

        return company;
    }
    /*
     * company(entity >> dto)
     */

    /*
     * contract(dto >> entity)
     */
    public static CompanyContract toEntityContract(CompanyRegDto dto) {
        CompanyContract companyContract = CompanyContract.builder()
                .contractStatus(dto.getContractStatus())
                .contractedAt(LocalDateTime.now())
                .contractStart(dto.getContractStart())
                .contractEnd(dto.getContractEnd())
                .asCompany(dto.getAsCompany())
                .asNum(dto.getAsNum())
                .build();

        return companyContract;
    }
    /*
     * contract(entity >> dto)
     */

    /*
     * pg(dto >> entity)
     */
    public static CompanyPG toEntityPg(CompanyRegDto dto) {
        CompanyPG companyPG = CompanyPG.builder()
                .sspMallId(dto.getSspMallId())
                .mid(dto.getMid())
                .merchantKey(dto.getMerchantKey())
                .regDt(LocalDateTime.now())
                .build();

        return companyPG;
    }

    /*
     * pg(entity >> dto)
     */

    /*
     * relation(dto >> entity)
     */
    public static CompanyRelationInfo toEntityRelation(CompanyRegDto dto) {
        CompanyRelationInfo companyRelationInfo = CompanyRelationInfo.builder()
                .parentCompanyName(dto.getParentCompanyName())
                .build();

        return companyRelationInfo;
    }
    /*
     * relation(entity >> dto)
     */

    /*
     * roaming(dto >> entity)
     */
    public static CompanyRoaming toEntityRoaming(CompanyRoamingtDto dto, Company company) {
        return CompanyRoaming.builder()
                .institutionCode(dto.getInstitutionCode())
                .institutionKey(dto.getInstitutionKey())
                .institutionEmail(dto.getInstitutionEmail())
                .company(company)
                .build();

    }

    /*
     * roaming(entity >> dto)
     */
}
