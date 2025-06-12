package zgoo.cpos.cpcontrol.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import zgoo.cpos.type.ocpp.ConfigurationKey;
import zgoo.cpos.type.ocpp.GetConfigSearchType;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GetConfigurationReqDto extends BaseDto {
    private ConfigurationKey key;
    private GetConfigSearchType searchOption;
}
