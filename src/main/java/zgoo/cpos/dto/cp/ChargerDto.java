package zgoo.cpos.dto.cp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import zgoo.cpos.type.ChargePointStatus;
import zgoo.cpos.type.ConnectionStatus;

@Data
@SuperBuilder
public class ChargerDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseChargerDto {
        private String chargerId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChargerListDto {
        private String companyName;
        private String stationName;
        private String stationId;
        private String chargerName;
        private String chargerId;
        private String commonTypeCode;
        private String commonTypeName;
        private String modelName;
        private String connectorStatus;
        private String policyName;
        private LocalDate installDate;
        private String manufCd;
        private String manufCdName;
        private String protocol;
        private String modelCode;
        private String fwVersion;
        private LocalDateTime lastUpdateDt;

    }

    /**
     * 기본 충전기 정보를 담는 부모 DTO 클래스
     */
    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class ChargerRegDto {
        // 충전소 정보
        private Long companyId;
        private String stationName;
        @NotBlank(message = "충전소 ID는 필수 입력 항목입니다.")
        private String stationId;

        // 충전기 정보
        private String chargerName;
        private String chargerId;
        private String modelCode;
        private String chgSerialNo;
        private String fwversion;
        private String anydeskId;
        private String commonType;
        private LocalDate installDate;
        private Long pricePolicyId;
        private String useYn;
        private String reason;
        private String location;

        // 모뎀정보
        private String modemSerialNo;
        private String modemNo;
        private LocalDate modemContractStart;
        private LocalDate modemContractEnd;
        private String modemPricePlan;
        private String modemdataCapacity;
        private String modemTelCorp;
        private String modemModelName;
        private String modemContractStatus;
    }

    /**
     * 상세 충전기 정보를 담는 자식 DTO 클래스
     */
    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class ChargerDetailListDto extends ChargerRegDto {
        // 충전기 모델 정보 (추가 필드)
        private String companyName;
        private String cpType;
        private String manufCd;
        private String manufCdName;
        private Integer chgKw;
        private String dualYn;
        private String cpPlanName;
        private String commonTypeName;
        private String reasonName;
        private String modemContractStatusNm;

        // 모든 필드를 포함하는 생성자는 @SuperBuilder가 자동으로 생성
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ConnectorStatusDto {
        private String chargerId;
        private Integer connectorId;
        private ChargePointStatus status;
        private ConnectionStatus connectionYn;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChargerSearchDto {
        private String chargerId;
        private String chargerName;
        private String fwVersion;
        private String location;
        private String modelName;
        private String manufCdName;
        private String cpTypeName;
        private LocalDateTime recentDt;
        private List<ConnectorStatusDto> connector;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ConnectorStatusCountDto {
        private Long availableCount;
        private Long preparingCount;
        private Long chargingCount;
        private Long suspendedEvCount;
        private Long suspendedEvseCount;
        private Long finishingCount;
        private Long reservedCount;
        private Long unavailableCount;
        private Long faultedCount;
        private Long disconnectedCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChargerCountBySidoDto {
        private String sido;
        private Long speedFastCount;
        private Long speedLowCount;
        private Long speedDespnCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FacilityCountDto {
        private String facility;
        private Long count;
    }
}
