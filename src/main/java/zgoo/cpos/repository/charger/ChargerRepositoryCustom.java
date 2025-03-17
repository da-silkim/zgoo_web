package zgoo.cpos.repository.charger;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.charger.CpInfo;
import zgoo.cpos.dto.cp.ChargerDto.ChargerListDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerSearchDto;

public interface ChargerRepositoryCustom {
    Page<ChargerListDto> findAllChargerListPaging(Pageable page);

    Page<ChargerListDto> findChargerListPaging(Long companyId, String manufCd, String searchOp, String searchContent,
            Pageable pageable);

    Optional<CpInfo> findCpByStationId(String stationId);

    List<ChargerSearchDto> findChargerListByStationId(String stationId);

    long countByStationId(String stationId);
}
