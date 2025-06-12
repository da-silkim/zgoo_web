package zgoo.cpos.cpcontrol.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import zgoo.cpos.type.ocpp.ResetType;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResetRequestDto extends BaseDto {
    private ResetType resetType;
}
