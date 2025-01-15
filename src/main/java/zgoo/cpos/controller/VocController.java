package zgoo.cpos.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.member.VocDto.VocAnswerDto;
import zgoo.cpos.service.VocService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/voc")
public class VocController {

    private final VocService vocService;

    // 1:1 단건 조회
    @GetMapping("/get/{vocId}")
    public ResponseEntity<?> findVocOne(@PathVariable("vocId") Long vocId) {
        log.info("=== find voc info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            VocAnswerDto vocOne = this.vocService.findVocOne(vocId);

            if (vocOne == null) {
                response.put("message", "정보를 불러오는데 실패했습니다. 다시 시도해 주세요.");
                response.put("vocInfo", Collections.emptyList());
                return ResponseEntity.ok(response);
            }

            response.put("vocInfo", vocOne);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[findVocOne] error: {}", e.getMessage());
            response.put("message", "서버 오류가 발생했습니다. 다시 시도해 주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 1:1 문의 답변 등록
    @PatchMapping("/update/{vocId}")
    public ResponseEntity<String> updateVocAnswer(@PathVariable("vocId") Long vocId, @RequestBody VocAnswerDto dto) {
        log.info("=== update voc answer info ===");

        try {
            Integer result = this.vocService.updateVocAnswer(vocId, dto);
            log.info("=== voc answer update complete ===");
            return switch (result) {
                case -1-> ResponseEntity.status(HttpStatus.OK).body("답변이 빈 값으로 처리되지 않았습니다.");
                case 1 -> ResponseEntity.status(HttpStatus.OK).body("답변이 정상적으로 처리되었습니다.");
                default -> ResponseEntity.status(HttpStatus.OK).body("오류로 인해 답변이 처리되지 않았습니다.");
            };
        } catch (Exception e) {
            log.error("[updateVocAnswer] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("답변 저장 중 오류 발생");
        }
    }
}
