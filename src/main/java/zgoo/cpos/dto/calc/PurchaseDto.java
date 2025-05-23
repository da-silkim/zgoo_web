package zgoo.cpos.dto.calc;

import java.time.LocalDate;
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
public class PurchaseDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class PurchaseBaseDto {
        private String stationId;
        private String approvalNo;
        private String accountCode;
        private String bizNum;
        private String bizName;
        private String paymentMethod;
        private Integer supplyPrice;
        private Integer vat;
        private Integer charge;
        private Integer surcharge;
        private Integer cutoffAmount;
        private Integer unpaidAmount;
        private Integer totalAmount;
        private Integer power;
        private LocalDate purchaseDate;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class PurchaseListDto extends PurchaseBaseDto {
        private Long purchaseId;
        private String stationName;
        private String accountCodeName;
        private String paymentMethodName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class PurchaseRegDto extends PurchaseBaseDto {
        private String item;
        private Integer unitPrice;
        private Integer amount;
        private String kepcoCustNo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseAccountDto {
        private Integer unitPrice;
        private Integer supplyPrice;
        private Integer vat;
        private Integer totalAmount;
        
        // 토지사용료
        private String landUseType;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class PurchaseDetailDto extends PurchaseRegDto {
        private String accountCodeName;
        private String paymentMethodName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseElecDto {
        private List<PurchaseRegDto> electricity;
    }
}
