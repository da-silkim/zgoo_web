package zgoo.cpos.domain.charger;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.domain.company.CpPlanPolicy;
import zgoo.cpos.domain.cs.CsInfo;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CpInfo {
    @Id
    @Column(name = "charger_id", length = 8)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private CsInfo stationId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cpmodem_id")
    private CpModem cpmodemInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private CpPlanPolicy planInfo;

    private String chargerName;
    private String serialNo;
    private String fwVersion;
    private String commonType;
    private String anydeskId;
    private LocalDate installDate;
    private LocalDateTime regDt;
    private String protocol;
    private String location;
    private String modelCode;
    private String useYn;
    private String reason;
}
