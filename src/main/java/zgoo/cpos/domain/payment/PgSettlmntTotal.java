package zgoo.cpos.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "PG_SETTLEMENT_TOTAL", indexes = {
        @Index(name = "IDX_PG_SETTLEMENT_TOTAL_REQ_DT", columnList = "req_dt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PgSettlmntTotal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlmnt_id")
    private Long id;

    @Column(name = "req_dt")
    private LocalDate reqDt;

    @Column(name = "usr_id", length = 20)
    private String usrId;

    @Column(name = "search_id", length = 10)
    private String searchId;

    @Column(name = "svc_cd", length = 4)
    private String svcCd;

    @Column(name = "app_cnt")
    private Integer appCnt;

    @Column(name = "app_amt")
    private BigDecimal appAmt;

    @Column(name = "cc_cnt")
    private Integer ccCnt;

    @Column(name = "cc_amt")
    private BigDecimal ccAmt;

    @Column(name = "deposit_amt")
    private BigDecimal depositAmt;

    @Column(name = "fee")
    private BigDecimal fee;

    @Column(name = "vat")
    private BigDecimal vat;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

}
