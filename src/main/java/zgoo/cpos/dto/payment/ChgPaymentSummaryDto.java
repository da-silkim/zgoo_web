package zgoo.cpos.dto.payment;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChgPaymentSummaryDto {
    // 관제 결제 데이터 합계
    private Integer totalChgPrice; // 승인금액 합계
    private Integer totalCancelCost; // 취소금액 합계
    private Integer totalRealCost; // 결제금액 합계
    private Double totalChgAmount; // 충전량 합계

    // PG 거래 데이터 합계
    private BigDecimal totalPgAppAmount; // PG 승인금액 합계
    private BigDecimal totalPgCancelAmount; // PG 취소금액 합계
    private BigDecimal totalPgPaymentAmount; // PG 결제금액 합계
}
