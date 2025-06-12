package zgoo.cpos.cpcontrol.message.changeconfiguration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;
import zgoo.cpos.type.ocpp.ChangeConfigurationStatus;

@Data
@ToString
public class ChangeConfigurationResponse {
    private final ChangeConfigurationStatus status;

    @JsonCreator
    public ChangeConfigurationResponse(
            @JsonProperty(value = "status", required = true) ChangeConfigurationStatus status) {
        this.status = status;
    }
}
