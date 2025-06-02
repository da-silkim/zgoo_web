package zgoo.cpos.domain.tariff;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.domain.company.CpPlanPolicy;
import zgoo.cpos.dto.tariff.TariffDto.TariffPolicyDto;

@Entity
@Table(name = "TARIFF_POLICY", uniqueConstraints = {
        @UniqueConstraint(name = "uk_tariff_policy_apply_date", columnNames = { "apply_date" })
}, indexes = {
        @Index(name = "idx_tariff_policy_apply_code", columnList = "apply_code")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class TariffPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tariff_id")
    private Long id;

    @JoinColumn(name = "policy_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CpPlanPolicy policy;

    @Column(name = "apply_date", unique = true)
    private LocalDateTime apply_date;

    @Column(name = "apply_code")
    private String apply_code;

    @Column(name = "regDt")
    private LocalDateTime regDt;

    /**
     * update logic
     */
    public void updateApplyCode(String code) {
        this.apply_code = code;
    }

    public void updateApplyDate(LocalDateTime updateDate) {
        this.apply_date = updateDate;
    }

    public void updateTariffPolicy(TariffPolicyDto dto) {
        this.apply_date = dto.getApplyDate();
        this.apply_code = dto.getApplyCode();
    }

}
