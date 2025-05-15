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
public class PurchaseSalesDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseSalesBaseDto {
        private Integer purchase;
        private BigDecimal sales;
        private Integer year;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseSalesLineChartBaseDto {
        private Integer year;
        private Integer month;
        private BigDecimal totalPrice;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseSalesLineChartDto {
        private List<PurchaseSalesLineChartBaseDto> purchaseList;
        private List<PurchaseSalesLineChartBaseDto> salesList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseSalesBarDto {
        private PurchaseSalesBaseDto preYear;
        private PurchaseSalesBaseDto curYear;
        private BigDecimal totalPrice;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalesDashboardDto {
        private Integer lowCheck;
        private Integer fastCheck;
        private Integer despnCheck;
        private BigDecimal lowSales;
        private BigDecimal fastSales;
        private BigDecimal despnSales;
        private BigDecimal compareLow;
        private BigDecimal compareFast;
        private BigDecimal compareDespn;
        private String period;
    } 
}
