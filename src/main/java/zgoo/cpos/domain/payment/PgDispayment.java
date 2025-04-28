package zgoo.cpos.domain.payment;

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
@Table(name = "PG_DISPAYMENT")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PgDispayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dispayment_id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "result_code")
    private String resultCode;

    @Column(name = "result_msg")
    private String resultMsg;

    @Column(name = "amt")
    private String amt;

    @Column(name = "batch_cnt")
    private Integer batchCnt;

    @Column(name = "batch_time")
    private LocalDateTime batchTime;

    @Column(name = "batch_time2")
    private LocalDateTime batchTime2;

    @Column(name = "status")
    private String status; // 01:미수, 02:배치1차, 03:배치2차, 04:미수처러완료

    @Column(name = "id_tag")
    private String idTag;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

}
