package zgoo.cpos.domain.history;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zgoo.cpos.type.Reason;
import zgoo.cpos.type.UseYn;

@Entity
@Table(name = "CHARGING_HIST", indexes = {
        @Index(name = "idx_charging_hist_charger_id", columnList = "charger_id"),
        @Index(name = "idx_charging_hist_start_time", columnList = "start_time"),
        @Index(name = "idx_charging_hist_id_tag", columnList = "id_tag"),
        @Index(name = "idx_charging_hist_approval_num", columnList = "approval_num")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChargingHist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charginghist_id")
    private Long id;

    @Column(name = "charger_id", nullable = false)
    private String chargerID;

    @Column(name = "id_tag", nullable = false)
    private String idTag;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "transaction_id", nullable = false)
    private Integer transactionId;

    @Column(name = "connector_id", nullable = false)
    private Integer connectorId;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "stop_reason")
    @Enumerated(EnumType.STRING)
    private Reason stopReason;

    @Column(name = "charging_time")
    private Integer chargingTime;

    @Column(name = "start_meter_val")
    private Integer startMeterVal;

    @Column(name = "stop_meter_val")
    private Integer stopMeterVal;

    @Column(name = "soc")
    private Integer soc;

    @Column(name = "unit_cost", precision = 5, scale = 1)
    private BigDecimal unitCost;

    @Column(name = "charge_amount", precision = 12, scale = 5)
    private BigDecimal chargeAmount;

    @Column(name = "charge_price", precision = 12, scale = 1)
    private BigDecimal chargePrice;

    @Column(name = "pre_amout")
    private Integer preAmount; // 최초결제금액

    @Column(name = "cancel_amount")
    private Integer cancelAmount;

    @Column(name = "real_amount")
    private Integer realAmount;

    @Column(name = "approval_num")
    private String approvalNum;

    @Column(name = "payment_yn", columnDefinition = "CHAR(1)")
    @Enumerated(EnumType.STRING)
    private UseYn paymentYn;

}
