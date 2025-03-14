package zgoo.cpos.dto.cp;

import java.time.LocalDate;

import groovy.transform.ToString;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ChargerRegDto {
        // 충전소 정보
        private Long companyId;
        private String stationName;
        @NotBlank(message = "충전소 ID는 필수 입력 항목입니다.") // stationId가 null이나 공백일 경우 에러 메시지 반환
        private String stationId;

        // 충전기 정보
        private String chargerName; // 충전기 이름
        private String chargerId; // 충전기 ID
        private String modelCode; // 충전기 모델코드드
        private String chgSerialNo; // 충전기 시리얼번호
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
        private LocalDate modemContractStart; // 계약시작일
        private LocalDate modemContractEnd; // 계약만료일
        private String modemPricePlan;
        private String modemdataCapacity; // 데이터용량
        private String modemTelCorp; // 통신사
        private String modemModelName; // 모델명
        private String modemContractStatus; // 계약상태(공통코드, 미계약:MODEMNC, 계약중:MODEMUC, 계약만료:MODEMCF)
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ConnectorStatusDto {
        private String chargerId;
        private Integer connectorId;
        private String status;
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
    }
}
