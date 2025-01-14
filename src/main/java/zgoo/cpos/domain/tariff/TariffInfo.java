package zgoo.cpos.domain.tariff;

import java.math.BigDecimal;

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
import zgoo.cpos.dto.tariff.TariffDto.TariffInfoDto;

@Entity
@Table(name = "TARIFF_INFO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class TariffInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tariffinfo_id")
    private Long id;

    @JoinColumn(name = "tariff_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TariffPolicy tariffPolicy;

    @Column(name = "hour")
    private int hour;

    @Column(name = "mem_slow", precision = 5, scale = 1)
    private BigDecimal memSlowUnitCost;

    @Column(name = "nomem_slow", precision = 5, scale = 1)
    private BigDecimal nomemSlowUnitCost;

    @Column(name = "mem_fast", precision = 5, scale = 1)
    private BigDecimal memFastUnitCost;

    @Column(name = "nomem_fast", precision = 5, scale = 1)
    private BigDecimal nomemFastUnitCost;

    // update logic
    public void updateTariffInfo(TariffInfoDto dto) {
        this.hour = dto.getHour();
        this.memSlowUnitCost = dto.getMemSlowUnitCost();
        this.nomemSlowUnitCost = dto.getNomemSlowUnitCost();
        this.memFastUnitCost = dto.getMemFastUnitCost();
        this.nomemFastUnitCost = dto.getNomemFastUnitCost();
    }
}
