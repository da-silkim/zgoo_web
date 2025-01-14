package zgoo.cpos.util;

import java.time.LocalDate;

import zgoo.cpos.dto.tariff.TariffDto.TariffRegDto;

public class DateUtils {

    public static TariffRegDto preprocessApplyDate(TariffRegDto requestDto) {
        if (requestDto.getApplyStartDate() != null) {
            requestDto.setApplyStartDate(adjustToStartOfDay(requestDto.getApplyStartDate()));
        }

        return requestDto;
    }

    private static LocalDate adjustToStartOfDay(LocalDate date) {
        // No extra conversion needed since LocalDate is already at the start of the
        // day.
        return date;
    }

}
