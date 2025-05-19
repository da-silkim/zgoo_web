package zgoo.cpos.repository.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.history.ErrorHistDto;

public interface ErrorHistRepositoryCustom {
    Page<ErrorHistDto> findAllErrorHist(Pageable pageable, String levelPath, boolean isSuperAdmin);

    Page<ErrorHistDto> findErrorHist(Long companyId, String manfCode, String startTimeFrom, String startTimeTo,
            String searchOp, String searchContent, Pageable pageable, String levelPath, boolean isSuperAdmin);
}
