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
@Table(name = "COMPANY_PG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)

public class CompanyPG {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_pg_id")
    private Long id;

    @Column(name = "mid")
    private String mid;

    @Column(name = "merchant_key")
    private String merchantKey;

    @Column(name = "ssp_mall_id")
    private String sspMallId;

    @Column(name = "reg_user_id")
    private String regUserId;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "mod_user_id")
    private String modUserId;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    public void updatePgInfo(CompanyRegDto dto) {
        this.mid = dto.getMid();
        this.merchantKey = dto.getMerchantKey();
        this.sspMallId = dto.getSspMallId();
        this.modUserId = "admin";
        this.modDt = LocalDateTime.now();
    }
}
