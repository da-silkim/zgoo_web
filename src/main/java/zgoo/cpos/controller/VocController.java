package zgoo.cpos.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import zgoo.cpos.dto.member.MemberDto.MemberListDto;
import zgoo.cpos.dto.member.VocDto.VocRegDto;
import zgoo.cpos.service.ComService;
import zgoo.cpos.service.VocService;
import zgoo.cpos.util.MenuConstants;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/voc")
public class VocController {

    private final VocService vocService;
    private final ComService comService;
    private final MessageSource messageSource;

    // 1:1 단건 조회
    @GetMapping("/get/{vocId}")
    public ResponseEntity<Map<String, Object>> findVocOne(@PathVariable("vocId") Long vocId) {
        log.info("=== find voc info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            VocRegDto vocOne = this.vocService.findVocOne(vocId);

            if (vocOne == null) {
                response.put("message",
                        messageSource.getMessage("voc.api.messages.loadFail", null, LocaleContextHolder.getLocale()));
                response.put("vocInfo", Collections.emptyList());
                return ResponseEntity.ok(response);
            }

            response.put("vocInfo", vocOne);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[findVocOne] error: {}", e.getMessage());
            response.put("message",
                    messageSource.getMessage("voc.api.messages.serverError", null, LocaleContextHolder.getLocale()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 전화 문의 등록
    @PostMapping("/new/call")
    public ResponseEntity<String> createVocCall(@Valid @RequestBody VocRegDto dto, Principal principal) {
        log.info("=== create voc info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.VOC);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            this.vocService.saveVocCall(dto, principal.getName());
            return ResponseEntity.ok(messageSource.getMessage("voc.api.messages.memInfoAddSuccess", null,
                    LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("[createVocCall] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("voc.api.messages.vocregFailed", null,
                            LocaleContextHolder.getLocale()));
        }
    }

    // 1:1 문의 답변 등록
    @PatchMapping("/update/{vocId}")
    public ResponseEntity<String> updateVocAnswer(@PathVariable("vocId") Long vocId, @RequestBody VocRegDto dto,
            Principal principal) {
        log.info("=== update voc answer info ===");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.VOC);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            Integer result = this.vocService.updateVocAnswer(vocId, dto, principal.getName());
            log.info("=== voc answer update complete ===");
            return switch (result) {
                case -1 -> ResponseEntity.status(HttpStatus.OK).body(messageSource
                        .getMessage("voc.api.messages.emptyReplyContent", null, LocaleContextHolder.getLocale()));
                case 1 -> ResponseEntity.status(HttpStatus.OK).body(messageSource
                        .getMessage("voc.api.messages.replaySuccess", null, LocaleContextHolder.getLocale()));
                default -> ResponseEntity.status(HttpStatus.OK).body(messageSource
                        .getMessage("voc.api.messages.replayFailed", null, LocaleContextHolder.getLocale()));
            };
        } catch (Exception e) {
            log.error("[updateVocAnswer] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("voc.api.messages.replayFailed", null,
                            LocaleContextHolder.getLocale()));
        }
    }

    // 회원정보 검색
    @GetMapping("/search/member")
    public ResponseEntity<Map<String, Object>> searchMember(
            @RequestParam("memName") String memName, @RequestParam("memPhone") String memPhone, Principal principal) {
        log.info("=== search member info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            List<MemberListDto> memberList = this.vocService.findMemberList(memName, memPhone);

            if (memberList == null) {
                response.put("message",
                        messageSource.getMessage("voc.messages.nodata", null, LocaleContextHolder.getLocale()));
                response.put("memberList", Collections.emptyList());
                return ResponseEntity.ok(response);
            }

            response.put("memberList", memberList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[searchMember] error: {}", e.getMessage());
            response.put("message",
                    messageSource.getMessage("voc.api.messages.serverError", null, LocaleContextHolder.getLocale()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
