package zgoo.cpos.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import zgoo.cpos.domain.company.CpPlanPolicy;
import zgoo.cpos.domain.tariff.TariffInfo;
import zgoo.cpos.domain.tariff.TariffPolicy;
import zgoo.cpos.dto.tariff.TariffDto.TariffInfoDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffRegDto;

public class TariffMapper {

    public static TariffPolicy toEntityTariffPolicy(TariffRegDto dto, CpPlanPolicy policy) {

        LocalDateTime applyStart = dto.getApplyStartDate().atStartOfDay();

        // 단가 상태코드
        String applyCode;
        // Extract LocalDate for date-only comparison
        LocalDate applyStartDate = applyStart.toLocalDate();
        LocalDate nowDate = LocalDate.now();

        if (applyStartDate.isAfter(nowDate)) { // Case 1: applyStart is in the future
            applyCode = "TFFUTR"; // Example: Future tariff code
        } else if (applyStartDate.isEqual(nowDate)) { // Case 2: applyStart is the same as current time
            applyCode = "TFCURR"; // Example: Current tariff code
        } else { // Case 3: applyStart is in the past
            applyCode = "TFPAST"; // Example: Past tariff code

        }

        TariffPolicy entity = TariffPolicy.builder()
                .policy(policy)
                .apply_date(applyStart)
                .apply_code(applyCode)
                .regDt(LocalDateTime.now())
                .build();

        return entity;
    }

    public static List<TariffInfo> toEntityListTariffInfo(TariffPolicy tpolicy, TariffRegDto reqdto) {

        return reqdto.getTariffInfo().stream().map(dto -> TariffInfo.builder()
                .tariffPolicy(tpolicy)
                .hour(dto.getHour())
                .memSlowUnitCost(dto.getMemSlowUnitCost())
                .nomemSlowUnitCost(dto.getNomemSlowUnitCost())
                .memFastUnitCost(dto.getMemFastUnitCost())
                .nomemFastUnitCost(dto.getNomemFastUnitCost())
                .build()).collect(Collectors.toList());
    }

    public static List<TariffInfoDto> toDtoListTariffInfo(List<TariffInfo> entityList) {
        return entityList.stream()
                .map(entity -> TariffInfoDto.builder()
                        .tariffinfoId(entity.getId())
                        .hour(entity.getHour())
                        .memSlowUnitCost(entity.getMemSlowUnitCost())
                        .nomemSlowUnitCost(entity.getNomemSlowUnitCost())
                        .memFastUnitCost(entity.getMemFastUnitCost())
                        .nomemFastUnitCost(entity.getNomemFastUnitCost())
                        .build())
                .collect(Collectors.toList());
    }
}
