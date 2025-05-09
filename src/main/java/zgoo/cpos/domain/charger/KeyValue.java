package zgoo.cpos.domain.charger;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "OCPP_CP_CONFIG")
public class KeyValue {
    @EmbeddedId
    private KeyValueId id;

    @Column(name = "value")
    private String configValue;

    @Column(name = "read_only")
    private boolean readOnly;

    private LocalDateTime regDt;

    private LocalDateTime modDt;

    public void updateValue(String value) {
        this.configValue = value;
    }

}
