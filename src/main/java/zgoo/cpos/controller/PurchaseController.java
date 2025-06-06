package zgoo.cpos.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseAccountDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseDetailDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseElecDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseRegDto;
import zgoo.cpos.dto.cs.CsInfoDto.StationSearchDto;
import zgoo.cpos.service.ComService;
import zgoo.cpos.service.CsService;
import zgoo.cpos.service.PurchaseService;
import zgoo.cpos.util.MenuConstants;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final ComService comService;
    private final CsService csService;

    // 매입 단건 조회
    @GetMapping("/get/{id}")
    public ResponseEntity<PurchaseRegDto> findPurchaseOne(@PathVariable("id") Long id) {
        log.info("=== find purchase info ===");

        try {
            PurchaseRegDto purchase = this.purchaseService.findPurchaseOne(id);

            if (purchase == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(purchase);
        } catch (Exception e) {
            log.error("[findPurchaseOne] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 계정과목 정보 조회
    @GetMapping("/search/account")
    public ResponseEntity<PurchaseAccountDto> searchPurchaseAccount(@RequestParam("accountCode") String accountCode,
            @RequestParam("stationId") String stationId) {
        log.info("=== search purchase account info ===");

        try {
            PurchaseAccountDto purchase = this.purchaseService.searchPurchaseAccount(accountCode, stationId);
            return ResponseEntity.ok(purchase);
        } catch (Exception e) {
            log.error("[searchPurchaseAccount] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 충전소 조회
    @GetMapping("/search/station")
    public ResponseEntity<Map<String, Object>> searchStationByOption(@RequestParam("searchOp") String searchOp,
            @RequestParam("searchContent") String searchContent, Principal principal) {
        log.info("=== search station info ===");

        log.info("searchOp: {}, searchContent: {}", searchOp, searchContent);

        Map<String, Object> response = new HashMap<>();

        try {
            List<StationSearchDto> csList = this.csService.searchStationByOption(searchOp, searchContent,
                    principal.getName());
            log.info("[searchStationByOption] csList >> {}", csList.toString());

            if (csList.isEmpty()) {
                response.put("message", "조회된 데이터가 없습니다.");
            }

            response.put("csList", csList != null ? csList : Collections.emptyList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[searchStationByOption] error: {}", e.getMessage());
            response.put("message", "서버 오류가 발생했습니다. 다시 시도해 주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 매입 상세 조회
    @GetMapping("/detail/{id}")
    public String detailPurchase(Model model, @PathVariable("id") Long id,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("=== Detail Purchase Management Page ===");

        model.addAttribute("selectedOpSearch", searchOp);
        model.addAttribute("selectedContentSearch", searchContent);
        model.addAttribute("selectedStartDate", startDate);
        model.addAttribute("selectedEndDate", endDate);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);

        try {
            PurchaseDetailDto purchase = this.purchaseService.findPurchaseDetailOne(id);
            model.addAttribute("purchase", purchase);
        } catch (Exception e) {
            log.error("[detailPurchase] error: {}", e.getMessage());
        }

        return "pages/calc/purchase_detail";
    }

    // 매입 등록
    @PostMapping("/new")
    public ResponseEntity<String> createPurchase(@Valid @RequestBody PurchaseRegDto dto, Principal principal) {
        log.info("=== create purchase info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.CALC_PURCHASE);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.purchaseService.savePurchase(dto, principal.getName());
            return ResponseEntity.ok("매입 정보가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createPurchase] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("매입 정보 등록 중 오류가 발생했습니다.");
        }
    }

    // 한전 등록
    @PostMapping("/new/elec")
    public ResponseEntity<String> createPurchaseElec(@Valid @RequestBody PurchaseElecDto dto, Principal principal) {
        log.info("=== create putchase electricity info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.CALC_PURCHASE);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            log.info("dot >> {}", dto.toString());
            int success = this.purchaseService.savePurchaseElec(dto, principal.getName());
            int total = dto.getElectricity() != null ? dto.getElectricity().size() : 0;

            if (success == total) {
                return ResponseEntity.ok("모든 정보가 정상적으로 등록되었습니다.");
            }

            return ResponseEntity.ok(String.format("총 %d건 중 %d건이 저장되었습니다.", total, success));
        } catch (Exception e) {
            log.error("[createPurchaseElec] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("한전전기요금 정보 등록 중 오류가 발생했습니다.");
        }
    }

    // 매입 수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updatePurchase(@PathVariable("id") Long id, @RequestBody PurchaseRegDto dto,
            Principal principal) {
        log.info("=== update purchase info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.CALC_PURCHASE);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.purchaseService.updatePurchaseInfo(id, dto, principal.getName());
            return ResponseEntity.ok("매입 정보가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updatePurchase] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("매입 정보 수정 중 오류가 발생했습니다.");
        }
    }

    // 매입 삭제 (delYn: N -> Y)
    @PatchMapping("/delete/{id}")
    public ResponseEntity<String> deletePurchaseInfo(@PathVariable("id") Long id, Principal principal) {
        log.info("=== delete purchase info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.CALC_PURCHASE);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.purchaseService.deletePurchaseInfo(id);
            return ResponseEntity.ok("매입 정보가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deletePurchaseInfo] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("매입 정보 삭제 중 오류가 발생했습니다.");
        }
    }
}
