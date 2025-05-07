package zgoo.cpos.repository.charger;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.charger.CpInfo;
import zgoo.cpos.dto.cp.ChargerDto.ChargerCountBySidoDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerListDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerSearchDto;
import zgoo.cpos.dto.cp.ChargerDto.FacilityCountDto;

public interface ChargerRepositoryCustom {
    Page<ChargerListDto> findAllChargerListPaging(Pageable page, String levelPath, boolean isSuperAdmin);

    Page<ChargerListDto> findChargerListPaging(Long companyId, String manufCd, String searchOp,
            String searchContent,
            Pageable pageable, String levelPath, boolean isSuperAdmin);

    Optional<CpInfo> findCpByStationId(String stationId);

    CpInfo findCpInfoByChargerId(String chargerId);

    List<ChargerSearchDto> findChargerListByStationId(String stationId, String levelPath, boolean isSuperAdmin);

    long countByStationId(String stationId);

    List<ChargerListDto> findAllChargerListWithoutPagination(Long companyId, String manufCd, String searchOp,
            String searchContent, String levelPath, boolean isSuperAdmin);

    long countCharger(String levelPath, boolean isSuperAdmin);

    List<ChargerCountBySidoDto> countChargerBySidoAndType(String levelPath, boolean isSuperAdmin);

    List<FacilityCountDto> countFacilityBySidoAndType(String sido, String type, String levelPath, boolean isSuperAdmin);
}
