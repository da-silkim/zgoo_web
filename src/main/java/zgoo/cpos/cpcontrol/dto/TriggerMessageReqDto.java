package zgoo.cpos.cpcontrol.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import zgoo.cpos.type.ocpp.MessageTrigger;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TriggerMessageReqDto extends BaseDto {
    private MessageTrigger requestedMessage;
    private Integer connectorId;
}
