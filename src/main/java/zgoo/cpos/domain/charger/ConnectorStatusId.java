package zgoo.cpos.domain.charger;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectorStatusId {
    private String chargerId;
    private Integer connectorId;

}
