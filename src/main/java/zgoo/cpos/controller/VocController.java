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

    // 1:1 단건 조회
    @GetMapping("/get/{vocId}")
    public ResponseEntity<Map<String, Object>> findVocOne(@PathVariable("vocId") Long vocId) {
        log.info("=== find voc info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            VocRegDto vocOne = this.vocService.findVocOne(vocId);

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
            return ResponseEntity.ok("회원 정보가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createVocCall] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("문의 정보 등록 중 오류 발생");
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

    // 회원정보 검색
    @GetMapping("/search/member")
    public ResponseEntity<Map<String, Object>> searchMember(
            @RequestParam("memName") String memName, @RequestParam("memPhone") String memPhone) {
        log.info("=== search member info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            List<MemberListDto> memberList = this.vocService.findMemberList(memName, memPhone);

            if (memberList == null) {
                response.put("message", "조회된 데이터가 없습니다.");
                response.put("memberList", Collections.emptyList());
                return ResponseEntity.ok(response);
            }

            response.put("memberList", memberList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[searchMember] error: {}", e.getMessage());
            response.put("message", "서버 오류가 발생했습니다. 다시 시도해 주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
