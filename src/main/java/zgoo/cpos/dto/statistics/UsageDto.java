package zgoo.cpos.dto.statistics;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UsageDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsageBaseDto {
        private Long lowCount;
        private Long fastCount;
        private Long despnCount;
        private Long totalUsage;
        private Integer year;
        private Integer month;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsageLineChartBaseDto {
        private Integer year;
        private Integer month;
        private Long totalUsage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsageLineChartDto {
        private List<UsageLineChartBaseDto> speedLowList;
        private List<UsageLineChartBaseDto> speedFastList;
        private List<UsageLineChartBaseDto> speedDespnList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsageBarDto {
        private UsageBaseDto preYear;
        private UsageBaseDto curYear;
    }
}
