package zgoo.cpos.domain.charger;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zgoo.cpos.type.ocpp.ConfigurationKey;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueId {
    @Column(nullable = false, name = "charger_id", columnDefinition = "VARCHAR(8)")
    private String chargerId;

    @Column(nullable = false, name = "configkey_id")
    @Enumerated(EnumType.STRING)
    private ConfigurationKey configKey;

}
