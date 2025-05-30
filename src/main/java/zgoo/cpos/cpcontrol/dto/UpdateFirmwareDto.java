package zgoo.cpos.cpcontrol.dto;

import lombok.Data;

@Data
public class UpdateFirmwareDto extends BaseDto {
    private String location;
    private Integer retires;
    private String retrieveDate;
    private Integer retryInterval;
}
