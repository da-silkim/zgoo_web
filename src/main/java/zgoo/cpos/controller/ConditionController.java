package zgoo.cpos.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

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
import zgoo.cpos.service.ConditionService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/system/condition")
public class ConditionController {

    private final ConditionService conditionService;

    // 약관 개정 히스토리 조회
    @GetMapping("/hist/search/{conditionCode}")
    public ResponseEntity<List<ConditionVersionHistBaseDto>> searchConditionHist(
            @PathVariable("conditionCode") String conditionCode) {
        log.info("=== search condition version hist ===");

        try {
            List<ConditionVersionHistBaseDto> conList = this.conditionService.findHistAllByConditionCode(conditionCode);

            if (conList.isEmpty()) {
                log.info("조회된 개정 히스토리가 없습니다.");
                return ResponseEntity.ok(Collections.emptyList());
            }
            log.info("조회된 개정 히스토리: {}", conList);
            return ResponseEntity.ok(conList);
        } catch (Exception e) {
            log.error("[searchConditionHist] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 약관 등록
    @PostMapping("/new")
    public ResponseEntity<String> createConditionCode(@Valid @RequestBody ConditionCodeBaseDto dto) {
        log.info("=== create condition info ===");

        try {
            this.conditionService.saveConditionCode(dto);
            return ResponseEntity.ok("약관 정보가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createConditionCode] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("약관 등록 중 오류 발생");
        }
    }

    // 약관 개정 히스토리 등록
    @PostMapping("/hist/new")
    public ResponseEntity<String> createConditionHist(
            @ModelAttribute @Valid ConditionVersionHistBaseDto dto) {
        log.info("=== create condition version hist info ===");

        try {
            // System.out.println("conditionHist: " + dto.toString());

            // 약관 버전 히스토리 저장
            this.conditionService.saveConditionVersionHist(dto);

            return ResponseEntity.ok("약관 개정 정보가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createConditionHist] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("약관 개정 등록 중 오류 발생");
        }
    }

    // 약관 다운로드
    @GetMapping("/hist/download")
    public void downloadFile(@RequestParam("id") Long id, HttpServletResponse response) {
        log.info("=== download condition file info ===");

        try {
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
            response.setHeader("Content-Disposition","attachment; fileName=\""+ URLEncoder.encode(originalName,StandardCharsets.UTF_8)+"\";");
            response.setHeader("Content-Transfer-Encoding","binary");

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
    public ResponseEntity<String> deleteConditionCode(@PathVariable("conditionCode") String conditionCode) {
        log.info("=== delete condition info ===");

        try {
            this.conditionService.deleteConditionCode(conditionCode);
            return ResponseEntity.ok("약관 정보가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteConditionCode] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("약관 정보 삭제 중 오류 발생");
        }
    }

    // 약관 개정 히스토리 삭제
    @DeleteMapping("/delete/hist/{conditionVersionHistId}")
    public ResponseEntity<String> deleteConditionHist(@PathVariable("conditionVersionHistId") Long conditionVersionHistId) {
        log.info("=== delete condition version hist info ===");

        try {
            this.conditionService.deleteConditionHist(conditionVersionHistId);
            return ResponseEntity.ok("약관 개정 정보가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteConditionHist] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("약관 개정 정보 삭제 중 오류 발생");
        }
    }
}
