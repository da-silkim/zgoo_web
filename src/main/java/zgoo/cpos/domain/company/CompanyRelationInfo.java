package zgoo.cpos.domain.company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zgoo.cpos.dto.company.CompanyDto.CompanyRegDto;

@Entity
@Table(name = "COMPANY_RELATION_INFO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CompanyRelationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_relation_info_id")
    private Long id;

    @OneToOne(mappedBy = "companyRelationInfo")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_company_id")
    private Company parentCompany;

    @Column(name = "level_path")
    private String levelPath;

    public void updateRelationInfo(CompanyRegDto dto, Company parentCompany) {
        this.parentCompany = parentCompany;
        if (parentCompany != null) {
            String parentPath = parentCompany.getCompanyRelationInfo().getLevelPath();
            this.levelPath = parentPath + "." + company.getId();
        } else {
            this.levelPath = String.valueOf(company.getId());
        }
    }

    // 회사 설정 메서드 (양방향 관계 설정)
    public void setCompany(Company company) {
        this.company = company;
    }
}
