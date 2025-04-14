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
    Page<ChargingHistDto> findAllChargingHist(Pageable pageable);

    Page<ChargingHistDto> findChargingHist(Long companyId, String startTimeFrom, String startTimeTo, String searchOp,
            String searchContent, Pageable pageable);

    List<ChargingHistDto> findAllChargingHistListWithoutPagination(Long companyId, String startTimeFrom,
            String startTimeTo, String searchOp, String searchContent);

    TotalkwBaseDto searchYearChargeAmount(Long companyId, String searchOp, String searchContent, Integer year);

    List<TotalkwLineChartBaseDto> searchMonthlyChargeAmount(Long companyId, String searchOp, String searchContent, Integer year, String cpType);

    UsageBaseDto searchYearUsage(Long companyId, String searchOp, String searchContent, Integer year);

    List<UsageLineChartBaseDto> searchMonthlyUsage(Long companyId, String searchOp, String searchContent, Integer year, String cpType);

    TotalkwDashboardDto findChargingHistByPeriod(LocalDateTime startDate, LocalDateTime endDate);
}
