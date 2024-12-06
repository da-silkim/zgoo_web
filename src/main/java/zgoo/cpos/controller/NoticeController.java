package zgoo.cpos.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.users.NoticeDto;
import zgoo.cpos.service.NoticeService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/system/notice")
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항 - 등록
    @PostMapping("/new")
    public ResponseEntity<String> createNotice(@RequestBody NoticeDto.NoticeRegDto dto,
            @ModelAttribute("loginUserId") String loginUserId) {
        log.info("=== create notice info ===");

        System.out.println("notice userId1: " + loginUserId);
        if (loginUserId != null) {
            dto.setUserId(loginUserId);
        }
        
        try {
            System.out.println("notice userId2: " + dto.getUserId());
            this.noticeService.saveNotice(dto);
            return ResponseEntity.ok("공지사항이 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createNotice] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("공지사항 등록 중 오류 발생");
        }
    }

    // 공지사항 - 단건 조회
    @GetMapping("/get/{id}")
    public ResponseEntity<NoticeDto.NoticeRegDto> findNoticeOne(@PathVariable("id") Long idx) {
        log.info("=== find notice info ===");

        try {
            NoticeDto.NoticeRegDto noticeFindOne = this.noticeService.findNoticeOne(idx);

            if (noticeFindOne != null) {
                return ResponseEntity.ok(noticeFindOne);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
                log.error("[findNoticeOne] error: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 공지사항 - 글 자세히
    @GetMapping("/detail/{id}")
    public String deatil(Model model, @PathVariable("id") Long idx,
                    @RequestParam(value = "page", defaultValue = "0") int page,
                    @RequestParam(value = "size", defaultValue = "10") int size,
                    @RequestParam(value = "companyIdSearch", required = false) Long companyId,
                    @RequestParam(value = "startDateSearch", required = false) String startDate,
                    @RequestParam(value = "endDateSearch", required = false) String endDate) {
        log.info("=== detail notice info ===");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateSearch = null;
        LocalDate endDateSearch = null;

        try { 
            if (startDate != null && !startDate.trim().isEmpty()) {
                startDateSearch = LocalDate.parse(startDate.trim(), formatter);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                endDateSearch = LocalDate.parse(endDate.trim(), formatter);
            }
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", e.getMessage());
        }

        try {
            this.noticeService.incrementViewCount(idx);
            NoticeDto.NoticeDetailDto notice = this.noticeService.findNoticeDetailOne(idx);
            String content = notice.getContent().replace("\n", "<br>");
            model.addAttribute("notice", notice);
            model.addAttribute("content", content);

            // 이전글, 다음글 조회
            NoticeDto.NoticeDetailDto previousNotice = this.noticeService.findPreviousNotice(idx, companyId, startDateSearch, endDateSearch);
            NoticeDto.NoticeDetailDto  nextNotice  = this.noticeService.findNextNotice(idx, companyId, startDateSearch, endDateSearch);
            model.addAttribute("previousNotice", previousNotice);
            model.addAttribute("nextNotice", nextNotice);

            // pagination 관련 파라미터 추가
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedStartDate", startDate);
            model.addAttribute("selectedEndDate", endDate);
        } catch (Exception e) {
            log.error("[findNoticeOne] error: {}", e.getMessage());
        }

        return "pages/system/notice_management_detail";
    }

    // 공지사항 - 수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateNotice(@PathVariable("id") Long idx, @RequestBody NoticeDto.NoticeRegDto dto) {
        log.info("=== update notice info ===");

        try {
            if (idx != null) {
                dto.setIdx(idx);
            } else {
                log.error("Notice idx is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("공지사항 ID가 없습니다.");
            }
            this.noticeService.updateNotice(dto);
            return ResponseEntity.ok("공지사항이 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateNotice] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("공지사항 수정 중 오류 발생");
        }
    }

    // 공지사항 - 삭제
    @PatchMapping("/delete/{id}")
    public ResponseEntity<String> deleteNotice(@PathVariable("id") Long idx) {
        log.info("=== delete notice info ===");

        try {
            if (idx == null) {
                log.error("Notice idx is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("공지사항 ID가 없습니다.");
            }
            this.noticeService.deleteNotice(idx);
            return ResponseEntity.ok("공지사항이 정상적으로 삭제되었습니다."); 
        } catch (Exception e) {
            log.error("[updateNotice] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("공지사항 수정 중 오류 발생");
        }
    }
}
