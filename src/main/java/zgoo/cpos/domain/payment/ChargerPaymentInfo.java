package zgoo.cpos.domain.payment;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CHG_PAYMENT_HIST", uniqueConstraints = {
        @UniqueConstraint(name = "UK_CHG_PAYMENT_HIST_TRANSACTION_ID", columnNames = { "transaction_id" })
}, indexes = {
        @Index(name = "IDX_CHG_PAYMENT_HIST_TIMESTAMP", columnList = "timestamp"),
        @Index(name = "IDX_CHG_PAYMENT_HIST_CHARGER_ID", columnList = "charger_id"),
        @Index(name = "IDX_CHG_PAYMENT_HIST_ID_TAG", columnList = "id_tag"),
        @Index(name = "IDX_CHG_PAYMENT_HIST_PRE_APPROVAL_NO", columnList = "pre_approval_no")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChargerPaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chargepmt_id")
    private Long id;

    @Column(name = "charger_id")
    private String chargerId;

    @Column(name = "transaction_id", unique = true)
    private Integer transactionId;

    @Column(name = "connector_id")
    private Integer connectorId;

    @Column(name = "member_type")
    private String memberType;

    @Column(name = "state_cd")
    private String stateCd; // 공통코드(PAYKNDCD, PREPAY:승인, CANCEL:전체취소, PCANCEL:부분취소)

    // 승인정보
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "id_tag")
    private String idTag;

    @Column(name = "pre_tid")
    private String preTid;

    @Column(name = "pre_amount")
    private Integer preAmount;

    @Column(name = "pre_approval_no")
    private String preApprovalNo;

    @Column(name = "pre_transaction_date")
    private String preTransactionDate;

    @Column(name = "pre_transaction_time")
    private String preTransactionTime;

    @Column(name = "pre_card_num")
    private String preCardNum;

    // 취소정보
    @Column(name = "cancel_timestamp")
    private LocalDateTime cancelTimestamp;

    @Column(name = "cancel_tid")
    private String cancelTid;

    @Column(name = "chg_start_time")
    private LocalDateTime chgStartTime;

    @Column(name = "chg_stop_time")
    private LocalDateTime chgStopTime;

    @Column(name = "cancel_amount")
    private Integer cancelAmount;

    @Column(name = "cancel_result")
    private String cancelResult;

    @Column(name = "result_price")
    private Integer resultPrice;

}
