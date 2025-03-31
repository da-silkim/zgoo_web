package zgoo.cpos.domain.charger;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CpCurrentTxId {
    private String chargerId;
    private Integer connectorId;

}
