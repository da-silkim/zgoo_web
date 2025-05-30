package zgoo.cpos.cpcontrol.message.firmware;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import zgoo.cpos.type.ocpp.UpdateFirmwareStatus;

@Getter
public class UpdateFirmwareResponse {
    private final UpdateFirmwareStatus status;

    @JsonCreator
    public UpdateFirmwareResponse(
            @JsonProperty(value = "status", required = true) UpdateFirmwareStatus status) {
        this.status = status;
    }

}
