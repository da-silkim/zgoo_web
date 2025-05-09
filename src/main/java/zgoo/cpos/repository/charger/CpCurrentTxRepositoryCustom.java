package zgoo.cpos.repository.charger;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.cp.CurrentChargingListDto;

public interface CpCurrentTxRepositoryCustom {
        Page<CurrentChargingListDto> findAllChargerListPaging(Pageable page, String levelPath, boolean isSuperAdmin);

        Page<CurrentChargingListDto> findChargerListPaging(Long companyId, String chgStartTimeFrom,
                        String chgStartTimeTo,
                        String searchOp, String searchContent,
                        Pageable pageable, String levelPath, boolean isSuperAdmin);

        List<CurrentChargingListDto> findAllCurrentTxListWithoutPagination(Long companyId, String startFrom,
                        String startTo, String searchOp, String searchContent, String levelPath, boolean isSuperAdmin);
}
