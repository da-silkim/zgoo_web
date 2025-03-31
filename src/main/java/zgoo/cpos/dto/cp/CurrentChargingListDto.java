package zgoo.cpos.dto.cp;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CurrentChargingListDto {
    private String companyName;
    private String stationName;
    private String stationId;
    private String chargerId;
    private Integer connectorId;
    private LocalDateTime chgStartTime;
    private String memberName;
    private String memberCardNo;
    private Double chgAmount;
    private String remainTime;
    private Integer soc;
    private Integer transactionId;
}
