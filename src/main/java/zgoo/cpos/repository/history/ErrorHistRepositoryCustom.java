package zgoo.cpos.repository.history;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.history.ErrorHistDto;
import zgoo.cpos.dto.statistics.ErrorDto.ErrorBaseDto;
import zgoo.cpos.dto.statistics.ErrorDto.ErrorLineChartBaseDto;

public interface ErrorHistRepositoryCustom {
    Page<ErrorHistDto> findAllErrorHist(Pageable pageable, String levelPath, boolean isSuperAdmin);

    Page<ErrorHistDto> findErrorHist(Long companyId, String manfCode, String startTimeFrom, String startTimeTo,
            String searchOp, String searchContent, Pageable pageable, String levelPath, boolean isSuperAdmin);

    // 에러이력 최신 4건 조회(대시보드)
    List<ErrorHistDto> findLatestErrorHist(String levelPath, boolean isSuperAdmin);

    // 연도별 에러이력 합계
    ErrorBaseDto findTotalErrorHistByYear(Long companyId, String searchOp, String searchContent, Integer year,
            String levelPath, boolean isSuperAdmin);

    // 월별 에러이력 합계
    List<ErrorLineChartBaseDto> searchMonthlyTotalErrorHist(Long companyId, String searchOp, String searchContent,
            Integer year, String cpType, String levelPath, boolean isSuperAdmin);
}
