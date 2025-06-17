package zgoo.cpos.cpcontrol.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RemoteStartTransactionDto extends BaseDto {
    private String idTag;
    private Integer connectorId;
    private Long chargingProfileId;
}
