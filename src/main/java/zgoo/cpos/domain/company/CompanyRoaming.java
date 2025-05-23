package zgoo.cpos.domain.company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.company.CompanyDto.CompanyRoamingtDto;

@Entity
@Table(name = "COMPANY_ROAMING")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class CompanyRoaming {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_roaming_id")
    private Long romaing_id;

    @Column(name = "institution_email")
    private String institutionEmail;

    @Column(name = "institution_code")
    private String institutionCode;

    @Column(name = "institution_key")
    private String institutionKey;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    /*
     * update logic
     */
    public void updateRoamingInfo(CompanyRoamingtDto dto) {
        this.institutionCode = dto.getInstitutionCode();
        this.institutionKey = dto.getInstitutionKey();
        this.institutionEmail = dto.getInstitutionEmail();
    }
}
