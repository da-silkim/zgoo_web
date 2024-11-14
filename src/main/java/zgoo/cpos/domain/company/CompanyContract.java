package zgoo.cpos.domain.company;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zgoo.cpos.dto.company.CompanyDto.CompanyRegDto;

@Entity
@Table(name = "COMPANY_CONTRACT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class CompanyContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_contract_id")
    private Long id;

    private String contractStatus;
    private LocalDateTime contractedAt;
    private LocalDateTime contractStart;
    private LocalDateTime contractEnd;
    private String asCompany;
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
