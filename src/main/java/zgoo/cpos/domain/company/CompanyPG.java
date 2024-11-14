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
@Table(name = "COMPANY_PG")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)

public class CompanyPG {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_pg_id")
    private Long id;

    private String mid;
    private String merchantKey;
    private String sspMallId;
    private String regUserId;
    private LocalDateTime regDt;
    private String modUserId;
    private LocalDateTime modDt;

    public void updatePgInfo(CompanyRegDto dto) {
        this.mid = dto.getMid();
        this.merchantKey = dto.getMerchantKey();
        this.sspMallId = dto.getSspMallId();
        this.modUserId = "admin";
        this.modDt = LocalDateTime.now();
    }
}
