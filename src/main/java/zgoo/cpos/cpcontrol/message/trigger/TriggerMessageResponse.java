package zgoo.cpos.cpcontrol.message.trigger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;
import zgoo.cpos.type.ocpp.TriggerMessageStatus;

@Getter
@ToString
public class TriggerMessageResponse {
    private final TriggerMessageStatus status;

    @JsonCreator
    public TriggerMessageResponse(
            @JsonProperty(value = "status", required = true) TriggerMessageStatus status) {
        this.status = status;
    }

}
