package zgoo.cpos.domain.company;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zgoo.cpos.dto.company.CompanyDto.CompanyRegDto;

@Entity
@Table(name = "COMPANY", uniqueConstraints = {
        @UniqueConstraint(columnNames = "company_name")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    // @EmbeddedId
    // private CompanyCompositeKey compkey;

    @Column(nullable = false)
    private String companyType; // 사업자유형

    @Column(nullable = false)
    private String companyLv; // 사업자레벨

    @Column(nullable = false)
    private String companyName; // 사업자명

    @Column(length = 20)
    private String bizNum; // 사업자번호

    @Column(length = 10, nullable = false)
    private String bizType; // 사업자구분(법인/개인)

    @Column()
    private String bizKind; // 업종

    @Column(length = 20)
    private String ceoName; // 대표자명

    @Column(length = 20)
    private String headPhone; // 대표전화

    @Column(length = 10)
    private String zipcode;

    @Column()
    private String address;

    @Column()
    private String addressDetail;

    @Column(length = 20)
    private String staffName; // 담당자명

    @Column(length = 50)
    private String staffEmail;

    @Column(length = 30)
    private String staffTel;

    @Column(length = 30)
    private String staffPhone;

    @Column(nullable = false)
    private String consignmentPayment; // 위탁결제구분

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "company_code", length = 2, unique = true)
    private String companyCode;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_relation_info_id")
    private CompanyRelationInfo companyRelationInfo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_contract_id")
    private CompanyContract companyContract;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_pg_id")
    private CompanyPG companyPG;

    // ==== update logic ====//
    public void updateCompanyBasicInfo(CompanyRegDto dto) {
        this.companyType = dto.getCompanyType();
        this.companyLv = dto.getCompanyLv();
        this.companyName = dto.getCompanyName();
        this.bizNum = dto.getBizNum();
        this.bizType = dto.getBizType();
        this.bizKind = dto.getBizKind();
        this.ceoName = dto.getCeoName();
        this.headPhone = dto.getHeadPhone();
        this.zipcode = dto.getZipcode();
        this.address = dto.getAddress();
        this.addressDetail = dto.getAddressDetail();
        this.staffName = dto.getStaffName();
        this.staffEmail = dto.getStaffEmail();
        this.staffTel = dto.getStaffTel();
        this.staffPhone = dto.getStaffPhone();
        this.consignmentPayment = dto.getConsignmentPayment();
        this.updatedAt = LocalDateTime.now();
        this.logoUrl = dto.getLogoUrl();
        this.companyCode = dto.getCompanyCode();
    }

}
