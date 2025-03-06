package zgoo.cpos.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillkeyIssueRequestDto {
    private String cardNum;
    private String cardExpire; // YYMM
    private String cardPwd;
    private String bizNo;
    private String bizName;
}
