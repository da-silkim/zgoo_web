package zgoo.cpos.dto.history;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorHistDto {
    private String companyName;
    private String manfName;
    private String stationName;
    private String chargerId;
    private Integer connectorId;
    private String errcd;
    private String errName;
    private LocalDateTime occurDateTime;
}
