package zgoo.cpos.domain.company;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zgoo.cpos.dto.company.CompanyDto.CompanyRegDto;

@Entity
@Table(name = "COMPANY_CONTRACT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CompanyContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_contract_id")
    private Long id;

    @Column(name = "contract_status")
    private String contractStatus;

    @Column(name = "contracted_at")
    private LocalDateTime contractedAt;

    @Column(name = "contract_start")
    private LocalDateTime contractStart;

    @Column(name = "contract_end")
    private LocalDateTime contractEnd;

    @Column(name = "as_company")
    private String asCompany;

    @Column(name = "as_num")
    private String asNum;

    public void updateContractInfo(CompanyRegDto dto) {
        this.contractStatus = dto.getContractStatus();
        this.contractedAt = dto.getContractedAt();
        this.contractStart = dto.getContractStart();
        this.contractEnd = dto.getContractEnd();
        this.asCompany = dto.getAsCompany();
        this.asNum = dto.getAsNum();
    }
}
