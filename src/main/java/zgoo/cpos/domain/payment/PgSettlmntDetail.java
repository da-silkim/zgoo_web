package zgoo.cpos.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDate;

import groovy.transform.builder.Builder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "PG_SETTLEMENT_DETAIL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "total")
public class PgSettlmntDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlmnt_id", nullable = false, foreignKey = @ForeignKey(name = "fk_settlmnt_detail_total"))
    private PgSettlmntTotal total;

    @Column(name = "charger_id", length = 10)
    private String chargerId;

    @Column(name = "tr_dt")
    private LocalDate trDt;

    @Column(name = "tid", length = 30)
    private String tid;

    @Column(name = "mid", length = 10)
    private String mid;

    @Column(name = "settlmnt_dt")
    private LocalDate settlmntDt;

    @Column(name = "svc_cd", length = 4)
    private String svcCd;

    @Column(name = "state_cd", length = 2)
    private String stateCd;

    @Column(name = "trans_type", length = 2)
    private String transType;

    @Column(name = "tr_amt")
    private BigDecimal trAmt;

    @Column(name = "deposit_amt")
    private BigDecimal depositAmt;

    @Column(name = "fee")
    private BigDecimal fee;

    @Column(name = "vat")
    private BigDecimal vat;

    @Column(name = "instmnt_mon", length = 2)
    private String instmntMon;

    @Column(name = "ninst_fee")
    private BigDecimal ninstFee;

    @Column(name = "part_svc_cd", length = 4)
    private String partSvcCd;

    @Column(name = "moid", length = 64)
    private String moid;

}
