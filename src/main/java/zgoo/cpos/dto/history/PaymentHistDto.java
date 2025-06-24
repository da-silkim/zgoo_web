package zgoo.cpos.dto.history;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaymentHistDto {
    private String companyName;
    private String chargerId;
    private Timestamp approvalTime;
    private Timestamp cancelTime;
    private String mid;
    private String tid;
    private String otid;
    private BigDecimal goddsAmt;
    private String stateCode;
    private String approvalNum;
    private String cardCompany;
    private String cardNum;
    private LocalDateTime regTime;
}
