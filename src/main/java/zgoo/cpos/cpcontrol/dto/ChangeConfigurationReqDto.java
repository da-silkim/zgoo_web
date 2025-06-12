package zgoo.cpos.cpcontrol.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import zgoo.cpos.type.ocpp.ConfigurationKey;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChangeConfigurationReqDto extends BaseDto {
    private ConfigurationKey key;
    private String value;
}
