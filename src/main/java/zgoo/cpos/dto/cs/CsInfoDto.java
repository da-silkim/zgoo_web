package zgoo.cpos.dto.cs;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CsInfoDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class CsInfoBaseDto {
        private Long companyId;
        private String stationId;
        private String stationName;
        private String address;
        private String opStatus;
        private LocalTime openStartTime;
        private LocalTime openEndTime;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CsInfoListDto extends CsInfoBaseDto {
        private String companyName;
        private Integer cpCount;        // 충전기수
        private String opStatusName;    // 운영상태명
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CsInfoRegDto extends CsInfoBaseDto {
        // 충전소 정보
        private String stationType;
        private String facilityType;
        private String asNum;
        private Double latitude;
        private Double longitude;
        private String zipcode;
        private String addressDetail;
        private String parkingFeeYn;

        // 충전소 부지 정보
        private String institutionName;
        private String landType;
        private String staffName;
        private String staffPhone;
        private LocalDate contractDate;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer landUseRate;
        private LocalDate billDate;

        // 충전소 한전계약 정보
        private String kepcoCustNo;
        private LocalDate openingDate;
        private Integer contPower;
        private String rcvCapacityMethod;
        private Integer rcvCapacity;
        private String voltageType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CsInfoDetailDto extends CsInfoRegDto {
        private String companyName;
        private String stationTypeName;  // 충전소유형 코드명
        private String facilityTypeName; // 충전소소시설구분 코드명
        private String opStatusName;     // 운영상태 코드명
        private String landTypeName;     // 계약부지구분 코드명
        private String rcvCapacityMethodName;   // 수전방식 코드명
        private String voltageTypeName;  // 전압종류 코드명
        private String openStartTimeFormatted;
        private String openEndTimeFormatted;

        // '정보없음' 출력을 위한 데이터 형변환
        private String contractDateString;
        private String rcvCapacityString;
        private String billDateString;
        private String parkingFeeString;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StationSearchDto {
        private String stationId;
        private String stationName;
        private String address;
        private Double latitude;
        private Double longitude;
    }
}
