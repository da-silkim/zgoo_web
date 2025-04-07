package zgoo.cpos.dto.history;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChgCommlogDto {
    private String chargerId;
    private int connectorId;
    private String action;
    private Timestamp recvTime;
    private String recvUuid; // uuid
    private String recvPayload;

    private Timestamp sendTime;
    private String sendUuid; // uuid
    private String sendPayload;
}
