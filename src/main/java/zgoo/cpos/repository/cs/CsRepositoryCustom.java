package zgoo.cpos.repository.cs;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoDetailDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoListDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoRegDto;
import zgoo.cpos.dto.cs.CsInfoDto.StationOpStatusDto;
import zgoo.cpos.dto.cs.CsInfoDto.StationSearchDto;

public interface CsRepositoryCustom {

    // 충전소 전체 조회
    Page<CsInfoListDto> findCsInfoWithPagination(Pageable pageable, String levelPath, boolean isSuperAdmin);

    // 충전소 검색 조회
    Page<CsInfoListDto> searchCsInfoWithPagination(Long companyId, String searchOp, String searchContent,
            Pageable pageable, String levelPath, boolean isSuperAdmin);

    // 충전소명 중복 검사
    boolean isStationNameDuplicate(String stationName);

    // 사업자의 마지막 충전소ID 추출
    String findRecentStationId(Long companyId);

    // 충전소 단건 조회
    CsInfoRegDto findCsInfoOne(String stationId);

    CsInfo findStationOne(String stationId);

    CsInfo findStationByKepcoCustNo(String kepcoCustNo);

    // 충전소 단건 상세 조회
    CsInfoDetailDto findCsInfoDetailOne(String stationId);

    // 충전소 삭제
    Long deleteCsInfoOne(String stationId);

    // 충전소 조회
    List<CsInfoDetailDto> findCsInfo();

    // 충전소 검색 조회
    List<StationSearchDto> findCsInfoContainKeyword(String keyword, String levelPath, boolean isSuperAdmin);

    List<StationSearchDto> searchStationByOption(String searchOp, String searchContent, String levelPath,
            boolean isSuperAdmin);

    // 사용자 위치 기반, 주변 충전소 조회
    List<CsInfoDetailDto> findStationsWithinRadius(double latitude, double longitude, double radiusInKm,
            String levelPath, boolean isSuperAdmin);

    List<CsInfoListDto> findAllStationWithoutPagination(Long companyId, String searchOp, String searchContent,
            String levelPath, boolean isSuperAdmin);

    // 충전소 운영상태
    StationOpStatusDto getStationOpStatusCount(String levelPath, boolean isSuperAdmin);
}
