package zgoo.cpos.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
            @RequestParam("searchContent") String searchContent) {
        log.info("=== search station info ===");

        log.info("searchOp: {}, searchContent: {}", searchOp, searchContent);

        Map<String, Object> response = new HashMap<>();

        try {
            List<StationSearchDto> csList = this.csService.searchStationByOption(searchOp, searchContent);
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
