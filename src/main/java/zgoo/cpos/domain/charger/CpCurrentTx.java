package zgoo.cpos.domain.charger;

import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CP_CURRENT_TX")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class CpCurrentTx {
    @EmbeddedId
    private CpCurrentTxId id;

    private Integer transactionId;
    private LocalDateTime startTime;
    private Long reservationId;
    private String idTag;
    private Integer meterStart;
    private Integer soc;
    private Double chargePower;
    private Double currentA;
    private LocalDateTime meterValueTimestamp;
    private Double currentPower;
    private String roamingType;
    private String remainStopTs;

}
