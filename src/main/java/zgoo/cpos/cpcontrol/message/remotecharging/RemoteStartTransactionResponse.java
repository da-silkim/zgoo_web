package zgoo.cpos.cpcontrol.message.remotecharging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;
import zgoo.cpos.type.ocpp.RemoteStartStopStatus;

@Getter
@ToString
public class RemoteStartTransactionResponse {
    private final RemoteStartStopStatus status;

    @JsonCreator
    public RemoteStartTransactionResponse(
            @JsonProperty(value = "status", required = true) RemoteStartStopStatus status) {
        this.status = status;
    }
}
