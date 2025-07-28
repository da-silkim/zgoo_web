package zgoo.cpos.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "PG_TRX_RECON", indexes = {
        @Index(name = "IDX_PG_TRX_RECON_TID", columnList = "tid"),
        @Index(name = "IDX_PG_TRX_RECON_OTID", columnList = "otid")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PgTrxRecon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recon_id")
    @Comment("idx")
    private Long reconId;

    @Column(name = "app_dt", nullable = false, length = 8)
    @Comment("승인일자(YYYYMMDD)")
    private String appDt;

    @Column(name = "app_tm", length = 6)
    @Comment("승인시간(HHMMSS)")
    private String appTm;

    @Column(name = "cc_dt", length = 8)
    @Comment("취소일자(YYYYMMDD)")
    private String ccDt;

    @Column(name = "cc_tm", length = 6)
    @Comment("취소시간(HHMMSS)")
    private String ccTm;

    @Column(name = "mid", length = 20)
    @Comment("상점ID")
    private String mid;

    @Column(name = "service_id", length = 20)
    @Comment("서비스ID")
    private String serviceId;

    @Column(name = "tid", length = 50)
    @Comment("TID")
    private String tid;

    @Column(name = "otid", length = 50)
    @Comment("원거래TID")
    private String otid;

    @Column(name = "cart_app_tid", length = 50)
    @Comment("서버원거래TID")
    private String cartAppTid;

    @Column(name = "goods_nm", length = 100)
    @Comment("구매품명")
    private String goodsNm;

    @Column(name = "goods_amt")
    @Comment("구매금액")
    private BigDecimal goodsAmt;

    @Column(name = "state_cd", length = 1)
    @Comment("상태")
    private String stateCd;

    @Column(name = "app_no", length = 30)
    @Comment("승인번호")
    private String appNo;

    @Column(name = "moid", length = 100)
    @Comment("주문번호")
    private String moid;

    @Column(name = "cc_moid", length = 100)
    @Comment("취소주문번호")
    private String ccMoid;

    @Column(name = "subid", length = 20)
    @Comment("서브ID")
    private String subId;

    @Column(name = "fn_cd", length = 10)
    @Comment("제휴사코드(카드사)")
    private String fnCd;

    @Column(name = "payment_no", length = 30)
    @Comment("결제수단번호")
    private String paymentNo;

    @Column(name = "coupon_amt")
    @Comment("쿠폰금액")
    private BigDecimal couponAmt;

    @Column(name = "join_type", length = 1)
    @Comment("중계/대행")
    private String joinType;

    @Column(name = "deposit_nm", length = 50)
    @Comment("입금자명")
    private String depositNm;

    @Column(name = "part_svc_cd", length = 10)
    @Comment("간편결제코드")
    private String partSvcCd;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

    // 상태 코드를 위한 Enum 정의
    public enum StateCode {
        APPROVED("0", "승인"),
        CANCELED_BEFORE_PURCHASE("1", "매입전취소"),
        CANCELED_AFTER_PURCHASE("2", "매입후취소");

        private final String code;
        private final String description;

        StateCode(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static StateCode fromCode(String code) {
            for (StateCode stateCode : StateCode.values()) {
                if (stateCode.getCode().equals(code)) {
                    return stateCode;
                }
            }
            throw new IllegalArgumentException("Unknown state code: " + code);
        }
    }

}
