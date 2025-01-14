package zgoo.cpos.dto.tariff;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TariffDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseTariffDto {
        private Long tariffId;
        private Long policyId;
        private String policyName;
        private Long companyId;
        private String companyName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class TariffPolicyDto extends BaseTariffDto {
        private LocalDateTime applyDate;
        private String applyCode; // 적용상태 >> 적용상태는 적용시작/마감일자 계산해서 코드 적용
        private String applyCodeName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TariffInfoDto {
        private Long tariffinfoId;
        private int hour;
        private BigDecimal memSlowUnitCost;
        private BigDecimal nomemSlowUnitCost;
        private BigDecimal memFastUnitCost;
        private BigDecimal nomemFastUnitCost;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TariffRegDto {
        private Long companyId;
        private Long tariffId;
        @NotBlank(message = "요금제 명을 입력해 주세요.")
        private String policyName;
        @FutureOrPresent(message = "현재 혹은 미래시간만 가능합니다.")
        private LocalDate applyStartDate; // 적용시작일자

        private String applyCode; // 적용상태 >> 적용상태는 Mapper에서 적용시작/마감일자 계산해서 코드 적용

        @JsonDeserialize(using = TariffInfoDtoDeserializer.class)
        List<TariffInfoDto> tariffInfo;
    }
}
