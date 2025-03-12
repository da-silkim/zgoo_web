package zgoo.cpos.cpcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTestRequestDto {
    private String amount;
    private String orderId;
}
