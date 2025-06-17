package zgoo.cpos.cpcontrol.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RemoteStopTransactionDto extends BaseDto {
    private Long transactionId;
}
