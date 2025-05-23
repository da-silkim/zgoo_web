package zgoo.cpos.controller;

import java.security.Principal;
import java.util.HashMap;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.users.FaqDto;
import zgoo.cpos.service.FaqService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/faq")
public class FaqController {

    private final FaqService faqService;

    // FAQ 등록
    @PostMapping("/new")
    public ResponseEntity<String> createFaq(@RequestBody FaqDto.FaqRegDto dto, Principal principal) {
        log.info("=== create faq info ===");

        try {
            this.faqService.saveFaq(dto, principal.getName());
            return ResponseEntity.ok("FAQ가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createFaq] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("FAQ 등록 중 오류가 발생했습니다.");
        }
    }

    // FAQ 단건 조회
    @GetMapping("/get/{id}")
    public ResponseEntity<FaqDto.FaqRegDto> findFaqOne(@PathVariable("id") Long id, Principal principal) {
        log.info("=== find faq info ===");

        try {
            FaqDto.FaqRegDto faqFindOne = this.faqService.findFaqOne(id, principal.getName());

            if (faqFindOne != null) {
                return ResponseEntity.ok(faqFindOne);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error("[findFaqOne] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // FAQ 상세내용
    @GetMapping("/detail/{id}")
    public String detailFaq(Model model, @PathVariable("id") Long id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sectionSearch", required = false) String section,
            Principal principal) {
        log.info("=== detail faq info ===");

        try {
            FaqDto.FaqDetailDto faq = this.faqService.findFaqDetailOne(id, principal.getName());
            String content = faq.getContent().replace("\n", "<br>");
            model.addAttribute("faq", faq);
            model.addAttribute("content", content);

            // 이전글, 다음글 조회
            FaqDto.FaqDetailDto previousFaq = this.faqService.findPreviousFaq(id, section, principal.getName());
            FaqDto.FaqDetailDto nextFaq = this.faqService.findNextFaq(id, section, principal.getName());
            model.addAttribute("previousFaq", previousFaq);
            model.addAttribute("nextFaq", nextFaq);

            // pagination 관련 파라미터 추가
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("selectedSection", section);
        } catch (Exception e) {
            log.error("[detailFaq] error: {}", e.getMessage());
        }

        return "pages/customer/faq_detail";
    }

    // FAQ 수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateFaq(@PathVariable("id") Long id, @RequestBody FaqDto.FaqRegDto dto,
            Principal principal) {
        log.info("=== update faq info ===");

        try {
            if (id == null) {
                log.error("FAQ id is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("FAQ ID값이 누락됐습니다.");
            }
            dto.setId(id);

            this.faqService.updateFaq(dto, principal.getName());
            return ResponseEntity.ok("FAQ가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateFaq] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("FAQ 수정 중 오류가 발생했습니다.");
        }
    }

    // FAQ 삭제
    @PatchMapping("/delete/{id}")
    public ResponseEntity<String> deleteFaq(@PathVariable("id") Long id, Principal principal) {
        log.info("=== delete faq info ===");

        try {
            if (id == null) {
                log.error("FAQ id is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("FAQ ID값이 누락됐습니다.");
            }
            this.faqService.deleteFaq(id, principal.getName());
            return ResponseEntity.ok("FAQ가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteFaq] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("FAQ 삭제 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/btncontrol/{id}")
    public ResponseEntity<Map<String, Object>> buttonControl(@PathVariable("id") Long id, Principal principal) {
        log.info("=== update & delete button authority info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            boolean btnControl = this.faqService.buttonControl(id, principal.getName());
            response.put("btnControl", btnControl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[FaqController >> buttonControl] error: {}", e.getMessage());
            response.put("message", "버튼 권한 확인 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
