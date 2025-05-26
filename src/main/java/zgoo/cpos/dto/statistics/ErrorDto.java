package zgoo.cpos.dto.statistics;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ErrorDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorBaseDto {
        private Long total;
        private Long lowCount;
        private Long fastCount;
        private Long despnCount;
        private Integer year;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorLineChartBaseDto {
        private Integer month;
        private Long total;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorLineChartDto {
        private List<ErrorLineChartBaseDto> speedLowList;
        private List<ErrorLineChartBaseDto> speedFastList;
        private List<ErrorLineChartBaseDto> speedDespnList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorBarDto {
        private ErrorBaseDto preYear;
        private ErrorBaseDto curYear;
    }
}
