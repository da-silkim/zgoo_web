package zgoo.cpos.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.cp.CpMaintainDto.CpInfoDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainDetailDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainRegDto;
import zgoo.cpos.service.ComService;
import zgoo.cpos.service.CpMaintainService;
import zgoo.cpos.util.MenuConstants;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/errlist")
public class CpMaintainController {

    private final CpMaintainService cpMaintainService;
    private final ComService comService;

    @GetMapping("/search/{chargerId}")
    public ResponseEntity<Map<String, Object>> searchCsCpInfo(@PathVariable("chargerId") String chargerId,
            Principal principal) {
        log.info("=== search cs/cp info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            CpInfoDto cpInfo = this.cpMaintainService.searchCsCpInfo(chargerId, principal.getName());

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
    public ResponseEntity<Map<String, Object>> findMaintainOne(@PathVariable("cpmaintainId") Long cpmaintainId,
            Principal principal) {
        log.info("=== find maintain info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            CpMaintainRegDto maintainFineOne = this.cpMaintainService.findMaintainOne(cpmaintainId);

            if (maintainFineOne == null) {
                response.put("message", "등록된 충전기 장애 정보가 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            CpInfoDto cpInfo = this.cpMaintainService.searchCsCpInfo(maintainFineOne.getChargerId(),
                    principal.getName());

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

    // 상세 조회
    @GetMapping("/detail/{cpmaintainId}")
    public String detailMaintain(Model model, @PathVariable("cpmaintainId") Long cpmaintainId,
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "processStatusSearch", required = false) String processStatus,
            @RequestParam(value = "startDateSearch", required = false) LocalDate startDate,
            @RequestParam(value = "endDateSearch", required = false) LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("=== detail maintain info ===");

        try {
            CpMaintainDetailDto cpmaintain = this.cpMaintainService.findMaintainDetailOne(cpmaintainId);
            String errorContent = cpmaintain.getErrorContent().replace("\n", "<br>");
            String processContent = cpmaintain.getProcessContent().replace("\n", "<br>");
            model.addAttribute("cpmaintain", cpmaintain);
            model.addAttribute("errorContent", errorContent);
            model.addAttribute("processContent", processContent);

            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);
            model.addAttribute("selectedProcessStatus", processStatus);
            model.addAttribute("selectedStartDate", startDate);
            model.addAttribute("selectedEndDate", endDate);
        } catch (Exception e) {
            log.error("[detailMaintain] error: {}", e.getMessage());
        }

        return "pages/maintenance/error_management_detail";
    }

    // 등록
    @PostMapping("/new")
    public ResponseEntity<String> createMaintain(@ModelAttribute @Valid CpMaintainRegDto dto, Principal principal) {
        log.info("=== create maintain info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.MAINTEN_ERR);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.cpMaintainService.saveMaintain(dto, principal.getName());
            return ResponseEntity.ok("충전기 장애 정보가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createMaintain] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("충전기 장애 정보 등록 중 오류가 발생했습니다.");
        }
    }

    // 수정
    @PatchMapping("/update/{cpmaintainId}")
    public ResponseEntity<String> updateMaintain2(@PathVariable("cpmaintainId") Long cpmaintainId,
            @ModelAttribute @Valid CpMaintainRegDto dto, Principal principal) {
        log.info("=== updates maintain info ===");

        try {
            if (cpmaintainId == null) {
                log.error("cpmaintainId is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("충전기 장애 정보ID가 없어 수정할 수 없습니다.");
            }

            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.MAINTEN_ERR);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.cpMaintainService.updateMaintain(cpmaintainId, dto, principal.getName());
            log.info("=== maintain info update complete ===");
            return ResponseEntity.ok("충전기 장애 정보가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateMaintain] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("충전기 장애 정보 수정 중 오류가 발생했습니다.");
        }
    }

    // 삭제
    @DeleteMapping("/delete/{cpmaintainId}")
    public ResponseEntity<String> deleteMaintain(@PathVariable("cpmaintainId") Long cpmaintainId, Principal principal) {
        log.info("=== delete maintain info ===");

        try {
            if (cpmaintainId == null) {
                log.error("cpmaintainId id null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("충전기 장애 정보ID가 없어 삭제할 수 없습니다.");
            }

            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.MAINTEN_ERR);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.cpMaintainService.deleteMaintain(cpmaintainId, principal.getName());
            return ResponseEntity.ok("충전기 장애 정보가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteMaintain] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("충전기 장애 정보 삭제 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/btncontrol/{cpmaintainId}")
    public ResponseEntity<Map<String, Object>> buttonControl(@PathVariable("cpmaintainId") Long cpmaintainId,
            Principal principal) {
        log.info("=== update & delete button authority info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            boolean btnControl = this.cpMaintainService.buttonControl(cpmaintainId, principal.getName());
            System.out.println("btnControl >> " + btnControl);
            response.put("btnControl", btnControl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[CpMaintainControll >> buttonControl] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
