package zgoo.cpos.domain.tariff;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zgoo.cpos.type.TariffId;

@Entity
@Table(name = "tariff")
@Getter
@Setter
@ToString
public class Tariff {

    @EmbeddedId
    private TariffId id;

    @Column(nullable = false)
    private int hour;

    @Column(precision = 5, scale = 1)
    private BigDecimal memSLow;

    @Column(precision = 5, scale = 1)
    private BigDecimal nomemSLow;

    @Column(precision = 5, scale = 1)
    private BigDecimal memFast;

    @Column(precision = 5, scale = 1)
    private BigDecimal nomemFast;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "tariff_id", referencedColumnName = "tariff_id", insertable = false, updatable = false),
            @JoinColumn(name = "policy_id", referencedColumnName = "policy_id", insertable = false, updatable = false),
            @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false)
    })
    private TariffPolicy tariffPolicy;
}
