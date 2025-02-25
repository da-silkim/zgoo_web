package zgoo.cpos.dto.cp;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CpMaintainDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class CpMaintainRegDto {
        private Long cpmaintainId;
        private String chargerId;
        private LocalDateTime regDt;
        private String errorType;
        private String errorContent;
        private String pictureLoc1;
        private String pictureLoc2;
        private String pictureLoc3;
        private LocalDateTime processDate;
        private String processStatus;
        private String processContent;
        private String regUserId;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class CpInfoDto {
        private Long companyId;
        private String companyName;
        private String stationId;
        private String stationName;
        private String address;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CpMaintainListDto extends CpMaintainRegDto {
        private Long companyId;
        private String companyName;
        private String stationName;
        private String errorTypeName;
        private String processStatusName;
    }
}
