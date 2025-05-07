package zgoo.cpos.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.cp.ChargerDto.ChargerSearchDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoDetailDto;
import zgoo.cpos.dto.cs.CsInfoDto.StationSearchDto;
import zgoo.cpos.service.ChargerService;
import zgoo.cpos.service.CsService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/map")
public class MapController {

    private final CsService csService;
    private final ChargerService chargerService;

    // 충전소 조회
    @GetMapping("/search/station/{keyword}")
    public ResponseEntity<Map<String, Object>> searchStation(@PathVariable("keyword") String keyword) {
        log.info("=== search station info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            List<StationSearchDto> csList = this.csService.saerchStationList(keyword);
            System.out.println("csList >> " + csList.toString());

            response.put("csList", csList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[searchStation] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 충전소, 충전기 상세 조회
    @GetMapping("/search/detail/{stationId}")
    public ResponseEntity<Map<String, Object>> searchDetailStation(@PathVariable("stationId") String stationId,
            Principal principal) {
        log.info("=== search station detail info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            // 충전소 정보
            CsInfoDetailDto csInfo = this.csService.findCsInfoDetailOne(stationId);
            response.put("csInfo", csInfo);

            // 충전기 정보
            List<ChargerSearchDto> cpList = this.chargerService.searchChargerList(stationId, principal.getName());

            if (cpList != null && !cpList.isEmpty()) {
                response.put("cpList", cpList);
            } else {
                response.put("message", "등록된 충전기 정보가 없습니다.");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[searchDetailStation] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 사용자 위치 기반, 주변 충전소 조회
    @GetMapping("/nearby")
    public ResponseEntity<Map<String, Object>> getNearbyStations(@RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude) {
        log.info("=== find nearby charging stations ===");

        Map<String, Object> response = new HashMap<>();

        try {
            List<CsInfoDetailDto> stationList = this.csService.findNearbyStations(latitude, longitude);
            response.put("stationsList", stationList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[getNearbyStations] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
