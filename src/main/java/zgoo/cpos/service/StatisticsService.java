package zgoo.cpos.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwBarDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwBaseDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwLineChartBaseDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwLineChartDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageBarDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageBaseDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageBaseDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageLineChartBaseDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageLineChartBaseDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageLineChartDto;
import zgoo.cpos.repository.history.ChargingHistRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final ChargingHistRepository chargingHistRepository;

    // totalkw >> bar chart
    public TotalkwBarDto searchYearChargeAmount(Long companyId, String searchOp, String searchContent, Integer yearSearch) {
        if (yearSearch == null) {
            yearSearch = LocalDate.now().getYear();
        }

        try {

            TotalkwBaseDto preYear = this.chargingHistRepository.searchYearChargeAmount(companyId, searchOp, searchContent, yearSearch-1);
            TotalkwBaseDto curYear = this.chargingHistRepository.searchYearChargeAmount(companyId, searchOp, searchContent, yearSearch);

            if (preYear == null || preYear.getTotalkw() == null) {
                preYear = getDefaultDto(yearSearch - 1);
            }

            if (curYear == null || curYear.getTotalkw() == null) {
                curYear = getDefaultDto(yearSearch);
            }

            return TotalkwBarDto.builder()
                .preYear(preYear)
                .curYear(curYear)
                .build();
        } catch (Exception e) {
            log.error("[searchYearChargeAmount] error: {}", e.getMessage());
        }

        return TotalkwBarDto.builder()
            .preYear(getDefaultDto(yearSearch - 1))
            .curYear(getDefaultDto(yearSearch))
            .build();
    }

    private TotalkwBaseDto getDefaultDto(Integer year) {
        return TotalkwBaseDto.builder()
            .lowChgAmount(BigDecimal.ZERO)
            .fastChgAmount(BigDecimal.ZERO)
            .despnChgAmount(BigDecimal.ZERO)
            .totalkw(BigDecimal.ZERO)
            .year(year)
            .build();
    }

    // totalkw >> line chart
    public TotalkwLineChartDto searchMonthlyChargeAmount(Long companyId, String searchOp, String searchContent, Integer yearSearch) {
        try {
            if (yearSearch == null) {
                yearSearch = LocalDate.now().getYear();
            }

            List<TotalkwLineChartBaseDto> speedLowList = this.chargingHistRepository.searchMonthlyChargeAmount(companyId, searchOp, 
                searchContent, yearSearch, "SPEEDLOW");
            List<TotalkwLineChartBaseDto> speedFastList = this.chargingHistRepository.searchMonthlyChargeAmount(companyId, searchOp, 
                searchContent, yearSearch, "SPEEDFAST");
            List<TotalkwLineChartBaseDto> speedDespnList = this.chargingHistRepository.searchMonthlyChargeAmount(companyId, searchOp, 
                searchContent, yearSearch, "SPEEDDESPN");

            return TotalkwLineChartDto.builder()
                .speedLowList(speedLowList)
                .speedFastList(speedFastList)
                .speedDespnList(speedDespnList)
                .build();
        } catch (Exception e) {
            log.error("[searchMonthlyChargeAmount] error: {}", e.getMessage());
            return null;
        }
    }

    // usage >> bar chart
    public UsageBarDto searchYearUsage(Long companyId, String searchOp, String searchContent, Integer yearSearch) {
        if (yearSearch == null) {
            yearSearch = LocalDate.now().getYear();
        }

        try {
            UsageBaseDto preYear = this.chargingHistRepository.searchYearUsage(companyId, searchOp, searchContent, yearSearch-1);
            UsageBaseDto curYear = this.chargingHistRepository.searchYearUsage(companyId, searchOp, searchContent, yearSearch);

            if (preYear == null || preYear.getTotalUsage() == null) {
                preYear = getDefaultUsageDto(yearSearch - 1);
            }

            if (curYear == null || curYear.getTotalUsage() == null) {
                curYear = getDefaultUsageDto(yearSearch);
            }

            return UsageBarDto.builder()
                .preYear(preYear)
                .curYear(curYear)
                .build();
        } catch (Exception e) {
            log.error("[searchYearUsage] error: {}", e.getMessage());
        }
        
        return UsageBarDto.builder()
            .preYear(getDefaultUsageDto(yearSearch - 1))
            .curYear(getDefaultUsageDto(yearSearch))
            .build();
    }

    private UsageBaseDto getDefaultUsageDto(Integer year) {
        return UsageBaseDto.builder()
            .lowCount(0L)
            .fastCount(0L)
            .despnCount(0L)
            .totalUsage(0L)
            .year(year)
            .build();
    }

    // usage >> line chart
    public UsageLineChartDto searchMonthlyUsage(Long companyId, String searchOp, String searchContent, Integer yearSearch) {
        try {
            if (yearSearch == null) {
                yearSearch = LocalDate.now().getYear();
            }

            List<UsageLineChartBaseDto> speedLowList = this.chargingHistRepository.searchMonthlyUsage(companyId, searchOp, 
                searchContent, yearSearch, "SPEEDLOW");
            List<UsageLineChartBaseDto> speedFastList = this.chargingHistRepository.searchMonthlyUsage(companyId, searchOp, 
                searchContent, yearSearch, "SPEEDFAST");
            List<UsageLineChartBaseDto> speedDespnList = this.chargingHistRepository.searchMonthlyUsage(companyId, searchOp, 
                searchContent, yearSearch, "SPEEDDESPN");

            return UsageLineChartDto.builder()
                .speedLowList(speedLowList)
                .speedFastList(speedFastList)
                .speedDespnList(speedDespnList)
                .build();
        } catch (Exception e) {
            log.error("[searchMonthlyUsage] error: {}", e.getMessage());
            return null;
        }
    }
}
