package zgoo.cpos.cpcontrol.message.firmware;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateFirmwareRequest {
    private final String location;
    private final Integer retries;
    private final String retrieveDate;
    private final Integer retryInterval;
}
