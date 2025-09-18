package zgoo.cpos.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.member.ConditionDto.ConditionCodeBaseDto;
import zgoo.cpos.dto.member.ConditionDto.ConditionVersionHistBaseDto;
import zgoo.cpos.service.ComService;
import zgoo.cpos.service.ConditionService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/system/condition")
public class ConditionController {

    private final ConditionService conditionService;
    private final ComService comService;
    private final MessageSource messageSource;

    // 약관 개정 히스토리 조회
    @GetMapping("/hist/search/{conditionCode}")
    public ResponseEntity<List<ConditionVersionHistBaseDto>> searchConditionHist(
            @PathVariable("conditionCode") String conditionCode) {
        log.info("=== search condition version hist ===");

        try {
            List<ConditionVersionHistBaseDto> conList = this.conditionService.findHistAllByConditionCode(conditionCode);

            if (conList.isEmpty()) {
                log.info("No revision history has been viewed.");
                return ResponseEntity.ok(Collections.emptyList());
            }
            log.info("Viewed revision history: {}", conList);
            return ResponseEntity.ok(conList);
        } catch (Exception e) {
            log.error("[searchConditionHist] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 약관 등록
    @PostMapping("/new")
    public ResponseEntity<String> createConditionCode(@Valid @RequestBody ConditionCodeBaseDto dto,
            Principal principal) {
        log.info("=== create condition info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkSuperAdminPermissions(principal);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.conditionService.saveConditionCode(dto);
            return ResponseEntity.ok(messageSource.getMessage("conditionmgmt.api.messages.registSuccess", null,
                    LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("[createConditionCode] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("conditionmgmt.api.messages.registError", null,
                            LocaleContextHolder.getLocale()));
        }
    }

    // 약관 개정 히스토리 등록
    @PostMapping("/hist/new")
    public ResponseEntity<String> createConditionHist(
            @ModelAttribute @Valid ConditionVersionHistBaseDto dto, Principal principal) {
        log.info("=== create condition version hist info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkSuperAdminPermissions(principal);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.conditionService.saveConditionVersionHist(dto);

            return ResponseEntity
                    .ok(messageSource.getMessage("conditionmgmt.api.messages.termsrevisionAddSuccess", null,
                            LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("[createConditionHist] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("conditionmgmt.api.messages.termsrevisionAddError", null,
                            LocaleContextHolder.getLocale()));
        }
    }

    // 약관 다운로드
    @GetMapping("/hist/download")
    public void downloadFile(@RequestParam("id") Long id, HttpServletResponse response, Principal principal) {
        log.info("=== download condition file info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkSuperAdminPermissions(principal);
            if (permissionCheck != null) {
                return;
            }

            ConditionVersionHistBaseDto conDto = this.conditionService.findHistOne(id);
            if (conDto == null) {
                log.error("[downloadFile] condition hist is null");
                return;
            }

            String originalName = conDto.getOriginalName();
            String filePath = conDto.getFilePath();
            byte[] files = Files.readAllBytes(Paths.get(filePath));

            response.setContentType("application/octet-stream");
            response.setContentLength(files.length);
            response.setHeader("Content-Disposition",
                    "attachment; fileName=\"" + URLEncoder.encode(originalName, StandardCharsets.UTF_8) + "\";");
            response.setHeader("Content-Transfer-Encoding", "binary");

            response.getOutputStream().write(files);
            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (IOException e) {
            log.error("[downloadFile] File read/write error: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("[downloadFile] Invalid input or missing data: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // 약관 삭제
    @DeleteMapping("/delete/{conditionCode}")
    public ResponseEntity<String> deleteConditionCode(@PathVariable("conditionCode") String conditionCode,
            Principal principal) {
        log.info("=== delete condition info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkSuperAdminPermissions(principal);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.conditionService.deleteConditionCode(conditionCode);
            return ResponseEntity.ok(messageSource.getMessage("conditionmgmt.api.messages.termsdeleteSuccess", null,
                    LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("[deleteConditionCode] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("conditionmgmt.api.messages.termsdeleteError", null,
                            LocaleContextHolder.getLocale()));
        }
    }

    // 약관 개정 히스토리 삭제
    @DeleteMapping("/delete/hist/{conditionVersionHistId}")
    public ResponseEntity<String> deleteConditionHist(
            @PathVariable("conditionVersionHistId") Long conditionVersionHistId,
            Principal principal) {
        log.info("=== delete condition version hist info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkSuperAdminPermissions(principal);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.conditionService.deleteConditionHist(conditionVersionHistId);
            return ResponseEntity
                    .ok(messageSource.getMessage("conditionmgmt.api.messages.termsrevisiondeleteSuccess", null,
                            LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("[deleteConditionHist] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("conditionmgmt.api.messages.termsrevisiondeleteError", null,
                            LocaleContextHolder.getLocale()));
        }
    }
}
