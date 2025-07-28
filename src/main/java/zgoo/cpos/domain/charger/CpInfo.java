package zgoo.cpos.domain.charger;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.domain.company.CpPlanPolicy;
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.dto.cp.ChargerDto.ChargerRegDto;

@Entity
@Table(name = "CP_INFO", indexes = {
        @Index(name = "idx_cpinfo_cs_id", columnList = "station_id")
})
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

    public void updateCpInfo(ChargerRegDto reqdto) {
        this.cpmodemInfo.updateCpModemInfo(reqdto.getModemNo(), reqdto.getModemSerialNo(),
                reqdto.getModemContractStart(),
                reqdto.getModemContractEnd(), reqdto.getModemPricePlan(), reqdto.getModemdataCapacity(),
                reqdto.getModemTelCorp(), reqdto.getModemModelName(), reqdto.getModemContractStatus());
        this.chargerName = reqdto.getChargerName();
        this.serialNo = reqdto.getChgSerialNo();
        this.fwVersion = reqdto.getFwversion();
        this.commonType = reqdto.getCommonType();
        this.anydeskId = reqdto.getAnydeskId();
        this.installDate = reqdto.getInstallDate();
        this.regDt = LocalDateTime.now();
        this.protocol = "OCPP1_6";
        this.location = reqdto.getLocation();
        this.modelCode = reqdto.getModelCode();
        this.useYn = reqdto.getUseYn();
        this.reason = reqdto.getReason();
    }
}
