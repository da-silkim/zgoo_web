package zgoo.cpos.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.service.ComService;
import zgoo.cpos.service.PurchaseService;
import zgoo.cpos.util.MenuConstants;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final ComService comService;

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
