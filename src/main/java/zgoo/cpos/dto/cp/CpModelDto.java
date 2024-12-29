package zgoo.cpos.dto.cp;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CpModelDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class CpModelBaseDto {
        private Long companyId;
        private Long modelId;
        private Integer powerUnit;
        private String modelName;
        private String modelCode;
        private String manufCd;
        private String installationType;
        private String cpType;
        private LocalDateTime regDt;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CpModelListDto extends CpModelBaseDto {
        private String companyName;
        private String manufCdName; // 제조사 코드명
        private String cpTypeName;  // 충전기유형 코드명
        private String connectorTypeName;   // 커넥터타입 코드명
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CpModelRegDto extends CpModelBaseDto {
        private LocalDateTime modDt;
        private String userId;

        // 충전기 모델 상세
        private String inputVoltage;
        private String inputFrequency;
        private String inputType;
        private String inputCurr;
        private String inputPower;
        private Double powerFactor;
        private String outputVoltage;
        private String maxOutputCurr;
        private String ratedPower;
        private Integer peakEfficiency;
        private String thdi;
        private String grdType;
        private String opAltitude;
        private String opTemperature;
        private String temperatureDerating;
        private String storageTemperatureRange;
        private String humidity;
        private String dimensions;
        private String ipNIk;
        private String weight;
        private String material;
        private String cableLength;
        private String screen;
        private String rfid;
        private String emergencyBtn;
        private String communicationInterface;
        private String lang;
        private String coolingMethod;
        private String emc;
        private String protection;
        private String opFunc;
        private String standard;
        private String powerModule;
        private String charger;

        // 충전기 커넥터
        private List<CpConnectorDto> connector;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CpConnectorDto {
        private Integer connectorId;
        private String connectorType;
        private String connectorTypeName;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CpModelDetailDto extends CpModelRegDto {
        private String manufCdName;
        private String installationTypeName;
        private String cpTypeName;
        private String powerFactorToString;
        private String peakEfficiencyToString;
    }
}
