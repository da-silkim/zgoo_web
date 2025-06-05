package zgoo.cpos.cpcontrol.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateFirmwareDto extends BaseDto {
    private String location;
    private Integer retries;
    private String retrieveDate;
    private Integer retryInterval;
}
