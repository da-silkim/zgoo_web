package zgoo.cpos.cpcontrol.message.datatransfer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zgoo.cpos.type.ocpp.DataTransferStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DataTransferResponse {
    private DataTransferStatus status;
    private String data;

    @JsonCreator
    public DataTransferResponse(
            @JsonProperty(value = "status", required = true) DataTransferStatus status,
            @JsonProperty(value = "data", required = false) String data) {
        this.status = status;
        this.data = data;
    }
}
