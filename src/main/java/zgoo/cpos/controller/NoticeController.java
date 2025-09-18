package zgoo.cpos.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import zgoo.cpos.dto.users.NoticeDto;
import zgoo.cpos.service.NoticeService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/system/notice")
public class NoticeController {

    private final NoticeService noticeService;
    private final MessageSource messageSource;

    // 공지사항 - 등록
    @PostMapping("/new")
    public ResponseEntity<String> createNotice(@RequestBody NoticeDto.NoticeRegDto dto, Principal principal) {
        log.info("=== create notice info ===");

        try {
            this.noticeService.saveNotice(dto, principal.getName());
            return ResponseEntity.ok(messageSource.getMessage("noticeManagement.api.registerSuccess", null,
                    LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("[createNotice] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("noticeManagement.api.registerError", null,
                            LocaleContextHolder.getLocale()));
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

    // 공지사항 - 상세내용
    @GetMapping("/detail/{id}")
    public String deatilNotice(Model model, @PathVariable("id") Long idx,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "startDateSearch", required = false) String startDate,
            @RequestParam(value = "endDateSearch", required = false) String endDate,
            Principal principal) {
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
            NoticeDto.NoticeDetailDto previousNotice = this.noticeService.findPreviousNotice(idx, companyId,
                    startDateSearch, endDateSearch, principal.getName());
            NoticeDto.NoticeDetailDto nextNotice = this.noticeService.findNextNotice(idx, companyId, startDateSearch,
                    endDateSearch, principal.getName());
            model.addAttribute("previousNotice", previousNotice);
            model.addAttribute("nextNotice", nextNotice);

            // pagination 관련 파라미터 추가
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedStartDate", startDate);
            model.addAttribute("selectedEndDate", endDate);
        } catch (Exception e) {
            log.error("[deatilNotice] error: {}", e.getMessage());
        }

        return "pages/system/notice_management_detail";
    }

    // 공지사항 - 수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateNotice(@PathVariable("id") Long idx, @RequestBody NoticeDto.NoticeRegDto dto,
            Principal principal) {
        log.info("=== update notice info ===");

        try {
            if (idx == null) {
                log.error("[updateNotice] noticeId is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(messageSource.getMessage("noticeManagement.api.noticeIdNotFound", null,
                                LocaleContextHolder.getLocale()));
            }
            dto.setIdx(idx);

            this.noticeService.updateNotice(dto, principal.getName());
            return ResponseEntity.ok(messageSource.getMessage("noticeManagement.api.updateSuccess", null,
                    LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("[updateNotice] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("noticeManagement.api.updateError", null,
                            LocaleContextHolder.getLocale()));
        }
    }

    // 공지사항 - 삭제
    @PatchMapping("/delete/{id}")
    public ResponseEntity<String> deleteNotice(@PathVariable("id") Long idx, Principal principal) {
        log.info("=== delete notice info ===");

        try {
            if (idx == null) {
                log.error("[deleteNotice] noticeId is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(messageSource.getMessage("noticeManagement.api.noticeIdNotFound", null,
                                LocaleContextHolder.getLocale()));
            }
            this.noticeService.deleteNotice(idx, principal.getName());
            return ResponseEntity.ok(messageSource.getMessage("noticeManagement.api.deleteSuccess", null,
                    LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("[deleteNotice] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("noticeManagement.api.deleteError", null,
                            LocaleContextHolder.getLocale()));
        }
    }

    @GetMapping("/btncontrol/{id}")
    public ResponseEntity<Map<String, Object>> buttonControl(@PathVariable("id") Long id, Principal principal) {
        log.info("=== update & delete button authority info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            boolean btnControl = this.noticeService.buttonControl(id, principal.getName());
            response.put("btnControl", btnControl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[NoticeController >> buttonControl] error: {}", e.getMessage());
            response.put("message", messageSource.getMessage("noticeManagement.api.buttonAuthError", null,
                    LocaleContextHolder.getLocale()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
