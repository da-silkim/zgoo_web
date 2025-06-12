package zgoo.cpos.domain.history;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CP_CONTROL_HIST")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CpControlHist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cpcontrolhist_id")
    private Long id;

    @Column(name = "station_id", length = 10, nullable = false)
    private String stationId;

    @Column(name = "charger_id", length = 10, nullable = false)
    private String chargerId;

    @Column(name = "control_time")
    private LocalDateTime controlTime;

    @Column(name = "action", length = 30)
    private String action;

    @Column(name = "payload")
    private String payload;

    @Column(name = "user_id", length = 30)
    private String userId;

}
