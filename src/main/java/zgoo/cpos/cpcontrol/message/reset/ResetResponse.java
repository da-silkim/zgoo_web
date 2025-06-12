package zgoo.cpos.cpcontrol.message.reset;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;
import zgoo.cpos.type.ocpp.ResetStatus;

@Getter
@ToString
public class ResetResponse {
    private final ResetStatus status;

    @JsonCreator
    public ResetResponse(
            @JsonProperty(value = "status", required = true) ResetStatus status) {
        this.status = status;
    }
}
