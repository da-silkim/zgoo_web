package zgoo.cpos.cpcontrol.message.getconfiguration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GetConfigurationResponse {
    private final List<KeyValueResponse> configurationKey;
    private final List<String> unknownKey;

    @JsonCreator
    public GetConfigurationResponse(
            @JsonProperty(value = "configurationKey") List<KeyValueResponse> configurationKey,
            @JsonProperty(value = "unknownKey") List<String> unknownKey) {
        this.configurationKey = configurationKey;
        this.unknownKey = unknownKey;
    }

}
