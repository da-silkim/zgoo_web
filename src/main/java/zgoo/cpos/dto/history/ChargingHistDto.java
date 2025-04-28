package zgoo.cpos.dto.history;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zgoo.cpos.type.Reason;
import zgoo.cpos.type.UseYn;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChargingHistDto {
    private String companyName;
    private String stationName;
    private String stationId;
    private String chargerId;
    private Integer connectorId;
    private String cpType;
    private String memberName;
    private String idTag;
    private String memberType;
    private LocalDateTime chgStartTime;
    private LocalDateTime chgEndTime;
    private Integer chgTime;
    private Reason chgEndReason;
    private Integer soc;
    private BigDecimal chgAmount;
    private BigDecimal chgPrice;
    private BigDecimal unitCost;
    private Integer prepayCost;
    private Integer cancelCost;
    private Integer realCost;
    private String approvalNum; // 승인번호
    private UseYn paymentYn;

    // to entity
}
