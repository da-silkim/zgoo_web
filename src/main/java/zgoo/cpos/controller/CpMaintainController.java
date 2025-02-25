package zgoo.cpos.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.cp.CpMaintainDto.CpInfoDto;
import zgoo.cpos.service.CpMaintainService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/errlist")
public class CpMaintainController {

    private final CpMaintainService cpMaintainService;

    @GetMapping("/search/{chargerId}")
    public ResponseEntity<Map<String, Object>> searchCsCpInfo(@PathVariable("chargerId") String chargerId) {
        log.info("=== search cs/cp info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            CpInfoDto cpInfo = this.cpMaintainService.searchCsCpInfo(chargerId);
            
            if (cpInfo.getChargerId() == null) {
                response.put("message", "등록된 충전기ID 정보가 없습니다.");
            }

            response.put("cpInfo", cpInfo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[searchCsCpInfo] error: {}", e.getMessage());
            response.put("message", "충전기ID 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
