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
import zgoo.cpos.dto.statistics.UsageDto.UsageLineChartBaseDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageLineChartDto;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.history.ChargingHistRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final ChargingHistRepository chargingHistRepository;
    private final ComService comService;
    private final CompanyRepository companyRepository;

    // totalkw >> bar chart
    public TotalkwBarDto searchYearChargeAmount(Long companyId, String searchOp, String searchContent,
            Integer yearSearch, String loginUserId) {
        if (yearSearch == null) {
            yearSearch = LocalDate.now().getYear();
        }

        try {

            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return null;
            }

            TotalkwBaseDto preYear = this.chargingHistRepository.searchYearChargeAmount(companyId, searchOp,
                    searchContent, yearSearch - 1, levelPath, isSuperAdmin);
            TotalkwBaseDto curYear = this.chargingHistRepository.searchYearChargeAmount(companyId, searchOp,
                    searchContent, yearSearch, levelPath, isSuperAdmin);

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
    public TotalkwLineChartDto searchMonthlyChargeAmount(Long companyId, String searchOp, String searchContent,
            Integer yearSearch, String loginUserId) {
        try {
            if (yearSearch == null) {
                yearSearch = LocalDate.now().getYear();
            }

            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return null;
            }

            List<TotalkwLineChartBaseDto> speedLowList = this.chargingHistRepository.searchMonthlyChargeAmount(
                    companyId, searchOp,
                    searchContent, yearSearch, "SPEEDLOW", levelPath, isSuperAdmin);
            List<TotalkwLineChartBaseDto> speedFastList = this.chargingHistRepository.searchMonthlyChargeAmount(
                    companyId, searchOp,
                    searchContent, yearSearch, "SPEEDFAST", levelPath, isSuperAdmin);
            List<TotalkwLineChartBaseDto> speedDespnList = this.chargingHistRepository.searchMonthlyChargeAmount(
                    companyId, searchOp,
                    searchContent, yearSearch, "SPEEDDESPN", levelPath, isSuperAdmin);

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
    public UsageBarDto searchYearUsage(Long companyId, String searchOp, String searchContent, Integer yearSearch,
            String loginUserId) {
        if (yearSearch == null) {
            yearSearch = LocalDate.now().getYear();
        }

        try {

            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return null;
            }

            UsageBaseDto preYear = this.chargingHistRepository.searchYearUsage(companyId, searchOp, searchContent,
                    yearSearch - 1, levelPath, isSuperAdmin);
            UsageBaseDto curYear = this.chargingHistRepository.searchYearUsage(companyId, searchOp, searchContent,
                    yearSearch, levelPath, isSuperAdmin);

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
    public UsageLineChartDto searchMonthlyUsage(Long companyId, String searchOp, String searchContent,
            Integer yearSearch, String loginUserId) {
        try {
            if (yearSearch == null) {
                yearSearch = LocalDate.now().getYear();
            }

            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return null;
            }

            List<UsageLineChartBaseDto> speedLowList = this.chargingHistRepository.searchMonthlyUsage(companyId,
                    searchOp,
                    searchContent, yearSearch, "SPEEDLOW", levelPath, isSuperAdmin);
            List<UsageLineChartBaseDto> speedFastList = this.chargingHistRepository.searchMonthlyUsage(companyId,
                    searchOp,
                    searchContent, yearSearch, "SPEEDFAST", levelPath, isSuperAdmin);
            List<UsageLineChartBaseDto> speedDespnList = this.chargingHistRepository.searchMonthlyUsage(companyId,
                    searchOp,
                    searchContent, yearSearch, "SPEEDDESPN", levelPath, isSuperAdmin);

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
