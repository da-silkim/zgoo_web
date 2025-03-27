package zgoo.cpos.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.code.ChgErrorCodeDto.ChgErrorCodeRegDto;
import zgoo.cpos.service.ChgErrorCodeService;
import zgoo.cpos.service.ComService;
import zgoo.cpos.util.MenuConstants;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/system/errcode")
public class ChgErrorCodeController {

    private final ChgErrorCodeService chgErrorCodeService;
    private final ComService comService;

    // 에러코드 단건 조회
    @GetMapping("/get/{errcdId}")
    public ResponseEntity<ChgErrorCodeRegDto> findErrorCodeOne(@PathVariable("errcdId") Long errcdId) {
        log.info("=== find error code info ===");

        try {
            ChgErrorCodeRegDto errorCodeOne = this.chgErrorCodeService.findErrorCodeOne(errcdId);

            if (errorCodeOne == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(errorCodeOne);
        } catch (Exception e) {
            log.error("[findErrorCodeOne] error: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 에러코드 등록
    @PostMapping("/new")
    public ResponseEntity<String> createErrorCode(@Valid @RequestBody ChgErrorCodeRegDto dto, Principal principal) {
        log.info("=== create error code info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                MenuConstants.ERRCODE);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.chgErrorCodeService.saveErrorCode(dto);
            return ResponseEntity.ok("에러코드 정보가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createErrorCode] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("에러코드 등록 중 오류가 발생했습니다.");
        }
    }

    // 에러코드 수정
    @PatchMapping("/update/{errcdId}")
    public ResponseEntity<String> updateErrorCode(@PathVariable("errcdId") Long errcdId,
            @RequestBody ChgErrorCodeRegDto dto, Principal principal) {
        log.info("=== update error code info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                MenuConstants.ERRCODE);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.chgErrorCodeService.updateErrorCode(errcdId, dto);
            log.info("=== error code info update complete ===");
            return ResponseEntity.ok("에러코드 정보가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateErrorCode] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("에러코드 수정 중 오류가 발생했습니다.");
        }
    }

    // 에러코드 삭제
    @DeleteMapping("/delete/{errcdId}")
    public ResponseEntity<String> deleteErrorCode(@PathVariable("errcdId") Long errcdId, Principal principal) {
        log.info("=== delete error code info ===");

        try {
            if (errcdId == null) {
                log.error("errcdId id is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body("에러코드ID가 없어 삭제할 수 없습니다.");
            }

            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                MenuConstants.ERRCODE);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.chgErrorCodeService.deleteErrorCode(errcdId);
            return ResponseEntity.ok("에러코드가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteErrorCode] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("에러코드 삭제 중 오류가 발생했습니다.");
        }
    }
}
