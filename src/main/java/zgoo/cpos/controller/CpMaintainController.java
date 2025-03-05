package zgoo.cpos.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.cp.CpMaintainDto.CpInfoDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainRegDto;
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

    // 단건 조회
    @GetMapping("/get/{cpmaintainId}")
    public ResponseEntity<Map<String, Object>> findMaintainOne(@PathVariable("cpmaintainId") Long cpmaintainId) {
        log.info("=== find maintain info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            CpMaintainRegDto maintainFineOne = this.cpMaintainService.findMaintainOne(cpmaintainId);

            if (maintainFineOne == null) {
                response.put("message", "등록된 충전기 장애 정보가 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            CpInfoDto cpInfo = this.cpMaintainService.searchCsCpInfo(maintainFineOne.getChargerId());

            if (cpInfo.getChargerId() == null) {
                response.put("message", "등록된 충전기ID 정보가 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("cpMaintain", maintainFineOne);
            response.put("cpInfo", cpInfo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[findMaintainOne] error: {}", e.getMessage());
            response.put("message", "충전기 장애 정보 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 등록
    @PostMapping("/new")
    public ResponseEntity<String> createMaintain(@ModelAttribute @Valid CpMaintainRegDto dto, Principal principal) {
        log.info("=== create maintain info ===");

        try {
            this.cpMaintainService.saveMaintain(dto, principal.getName());
            return ResponseEntity.ok("충전기 장애 정보가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createMaintain] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("충전기 장애 정보 등록 중 오류 발생");
        }
    }

    // 수정
    @PatchMapping("/update/{cpmaintainId}")
    public ResponseEntity<String> updateMaintain(@PathVariable("cpmaintainId") Long cpmaintainId, @RequestBody CpMaintainRegDto dto) {
        log.info("=== update maintain info ===");

        if (cpmaintainId == null) {
            log.error("cpmaintainId id null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("충전기 장애 정보ID가 없습니다.");
        }

        try {
            this.cpMaintainService.updateMaintain(cpmaintainId, dto);
            log.info("=== maintain info update complete ===");
            return ResponseEntity.ok("충전기 장애 정보가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateMaintain] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("충전기 장애 정보 수정 중 오류 발생");
        }
    }

    // @PatchMapping("/updates/{cpmaintainId}")
    // public ResponseEntity<String> updateMaintain2(@PathVariable("cpmaintainId") Long cpmaintainId, @ModelAttribute @Valid CpMaintainRegDto dto) {
    //     log.info("=== update maintain info ===");

    //     try {
    //         this.cpMaintainService.updateMaintain2(cpmaintainId, dto);
    //         log.info("=== maintain info update complete ===");
    //         return ResponseEntity.ok("충전기 장애 정보가 정상적으로 수정되었습니다.");
    //     } catch (Exception e) {
    //         log.error("[updateMaintain] error: {}", e.getMessage());
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                                 .body("충전기 장애 정보 수정 중 오류 발생");
    //     }
    // }

    // 삭제
    @DeleteMapping("/delete/{cpmaintainId}")
    public ResponseEntity<String> deleteMaintain(@PathVariable("cpmaintainId") Long cpmaintainId) {
        log.info("=== delete maintain info ===");

        if (cpmaintainId == null) {
            log.error("cpmaintainId id null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("충전기 장애 정보ID가 없습니다.");
        }

        try {
            this.cpMaintainService.deleteMaintain(cpmaintainId);
            return ResponseEntity.ok("충전기 장애 정보가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteMaintain] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("충전기 장애 정보 삭제 중 오류 발생");
        }
    }
}
