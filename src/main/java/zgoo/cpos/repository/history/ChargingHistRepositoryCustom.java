package zgoo.cpos.repository.history;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.history.ChargingHistDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwBaseDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwDashboardDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwLineChartBaseDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageBaseDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageLineChartBaseDto;

public interface ChargingHistRepositoryCustom {
        Page<ChargingHistDto> findAllChargingHist(Pageable pageable, String levelPath, boolean isSuperAdmin);

        Page<ChargingHistDto> findChargingHist(Long companyId, String startTimeFrom, String startTimeTo,
                        String searchOp,
                        String searchContent, Pageable pageable, String levelPath, boolean isSuperAdmin);

        List<ChargingHistDto> findAllChargingHistListWithoutPagination(Long companyId, String startTimeFrom,
                        String startTimeTo, String searchOp, String searchContent, String levelPath,
                        boolean isSuperAdmin);

        TotalkwBaseDto searchYearChargeAmount(Long companyId, String searchOp, String searchContent, Integer year,
                        String levelPath, boolean isSuperAdmin);

        List<TotalkwLineChartBaseDto> searchMonthlyChargeAmount(Long companyId, String searchOp, String searchContent,
                        Integer year, String cpType, String levelPath, boolean isSuperAdmin);

        UsageBaseDto searchYearUsage(Long companyId, String searchOp, String searchContent, Integer year,
                        String levelPath, boolean isSuperAdmin);

        List<UsageLineChartBaseDto> searchMonthlyUsage(Long companyId, String searchOp, String searchContent,
                        Integer year, String cpType, String levelPath, boolean isSuperAdmin);

        TotalkwDashboardDto findChargingHistByPeriod(LocalDateTime startDate, LocalDateTime endDate,
                        String levelPath, boolean isSuperAdmin);
}
