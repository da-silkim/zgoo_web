package zgoo.cpos.domain.payment;

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
@Table(name = "PG_RESULT_LOG")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PgResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chgbillpmt_id")
    private Long id;

    @Column(name = "app_result_code")
    private String appResultCode;

    @Column(name = "app_result_msg")
    private String appResultMsg;

    @Column(name = "app_tid")
    private String appTid;

    @Column(name = "app_moid")
    private String appMoid;

    @Column(name = "app_amt")
    private String appAmt;

    @Column(name = "auth_code")
    private String authCode;

    @Column(name = "auth_date")
    private String authDate;

    @Column(name = "acqu_card_code")
    private String acquCardCode;

    @Column(name = "acqu_card_name")
    private String acquCardName;

    @Column(name = "card_no")
    private String cardNo;

    @Column(name = "card_code")
    private String cardCode;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "card_quota")
    private String cardQuota;

    @Column(name = "card_cl")
    private String cardCl;

    @Column(name = "card_interest")
    private String cardInterest;

    @Column(name = "cc_partcl")
    private String ccPartcl;

    @Column(name = "partial_cancel_code")
    private String partialCancelCode;

    @Column(name = "ccl_result_code")
    private String cclResultCode;

    @Column(name = "ccl_result_msg")
    private String cclResultMsg;

    @Column(name = "ccl_error_cd")
    private String cclErrorCd;

    @Column(name = "ccl_err_msg")
    private String cclErrMsg;

    @Column(name = "ccl_amt")
    private String cclAmt;

    @Column(name = "ccl_mid")
    private String cclMid;

    @Column(name = "ccl_moid")
    private String cclMoid;

    @Column(name = "ccl_paymethod")
    private String cclPaymethod;

    @Column(name = "ccl_tid")
    private String cclTid;

    @Column(name = "ccl_date")
    private String cclDate;

    @Column(name = "ccl_time")
    private String cclTime;

    @Column(name = "ccl_num")
    private String cclNum;

    @Column(name = "ccl_remain_amt")
    private String cclRemainAmt;

    @Column(name = "state_cd")
    private String stateCd;

}
