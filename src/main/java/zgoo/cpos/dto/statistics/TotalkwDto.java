package zgoo.cpos.dto.statistics;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TotalkwDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalkwBaseDto {
        private String speedLow;
        private String speedFast;
        private String speedDespn;
        private BigDecimal lowChgAmount;
        private BigDecimal fastChgAmount;
        private BigDecimal despnChgAmount;
        private BigDecimal totalkw;
        private Integer year;
        private Integer month;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalkwLineChartBaseDto {
        private String cpType;
        private Integer year;
        private Integer month;
        private BigDecimal totalkw;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalkwLineChartDto {
        private List<TotalkwLineChartBaseDto> speedLowList;
        private List<TotalkwLineChartBaseDto> speedFastList;
        private List<TotalkwLineChartBaseDto> speedDespnList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalkwBarDto {
        private TotalkwBaseDto preYear;
        private TotalkwBaseDto curYear;
    }
}
