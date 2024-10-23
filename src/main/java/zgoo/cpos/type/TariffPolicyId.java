package zgoo.cpos.type;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
public class TariffPolicyId implements Serializable {
    @Column(name = "tariff_id")
    private Long tariffId;
    @Column(name = "policy_id")
    private Long policyId;
    @Column(name = "company_id")
    private Long companyId;

}
