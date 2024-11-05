package zgoo.cpos.domain.tariff;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zgoo.cpos.type.TariffPolicyId;

@Entity
@Table(name = "tariff_policy")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TariffPolicy {

    @EmbeddedId
    private TariffPolicyId id;

    @Column(nullable = false)
    private LocalDateTime applyDate;

    @Column(nullable = false, length = 1)
    private String applyCode;

    @OneToMany(mappedBy = "tariffPolicy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tariff> tariffs;
}
