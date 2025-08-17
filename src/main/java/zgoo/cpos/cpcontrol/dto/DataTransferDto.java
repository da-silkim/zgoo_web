package zgoo.cpos.cpcontrol.dto;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DataTransferDto extends BaseDto {
    private String vendorId;
    private String messageId;
    private JsonNode data;
}
