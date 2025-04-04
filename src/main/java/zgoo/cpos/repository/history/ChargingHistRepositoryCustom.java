package zgoo.cpos.repository.history;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.history.ChargingHistDto;

public interface ChargingHistRepositoryCustom {
    Page<ChargingHistDto> findAllChargingHist(Pageable pageable);

    Page<ChargingHistDto> findChargingHist(Long companyId, String startTimeFrom, String startTimeTo, String searchOp,
            String searchContent, Pageable pageable);

    List<ChargingHistDto> findAllChargingHistListWithoutPagination(Long companyId, String startTimeFrom,
            String startTimeTo, String searchOp, String searchContent);
}
