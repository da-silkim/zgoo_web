package zgoo.cpos.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import zgoo.cpos.domain.member.MemberAuth;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;
import zgoo.cpos.dto.member.MemberDto;
import zgoo.cpos.dto.member.MemberDto.MemberAuthDto;
import zgoo.cpos.dto.member.MemberDto.MemberDetailDto;
import zgoo.cpos.dto.member.MemberDto.MemberPasswordDto;
import zgoo.cpos.dto.member.MemberDto.MemberRegDto;
import zgoo.cpos.service.BizService;
import zgoo.cpos.service.ConditionService;
import zgoo.cpos.service.MemberService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final BizService bizService;
    private final ConditionService conditionService;

    // 회원 단건 조회
    @GetMapping("/get/{memberId}")
    public ResponseEntity<?> findMemberOne(@PathVariable("memberId") Long memberId) {
        log.info("=== find member info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            MemberRegDto memberOne = this.memberService.findMemberOne(memberId);

            if (memberOne == null) {
                response.put("message", "조회된 데이터가 없습니다.");
                response.put("bizInfo", Collections.emptyList());
                return ResponseEntity.ok(response);
            }

            if ("CB".equals(memberOne.getBizType())) {
                BizInfoRegDto bizOne = this.bizService.findBizOneCustom(memberOne.getBizId());
                log.info("[findMemberOne] 법인정보: {}", bizOne.toString());
                response.put("bizInfo", bizOne);
            } else {
                response.put("bizInfo", Collections.emptyList());
            }

            response.put("memberInfo", memberOne);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[findMemberOne] error: {}", e.getMessage());
            response.put("message", "서버 오류가 발생했습니다. 다시 시도해 주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 사용자ID 중복 검사
    @GetMapping("/memLoginId")
    public ResponseEntity<Boolean> checkMemLoginId(@RequestParam String memLoginId) {
        log.info("=== duplicate check memLoginId ===");

        try {
            boolean response = this.memberService.isMemLoginIdDuplicate(memLoginId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[checkMemLoginId] error : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 회원 상세 조회
    @GetMapping("/detail/{memberId}")
    public String detailMember(Model model, @PathVariable("memberId") Long memberId,
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "idTagSearch", required = false) String idTag,
            @RequestParam(value = "nameSearch", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("=== detail member info ===");

        try {
            MemberDetailDto member = this.memberService.findMemberDetailOne(memberId);
            model.addAttribute("member", member);

            if ("CB".equals(member.getBizType())) {
                log.info("=== [detailMember] detail member info : 법인 ===");
                BizInfoRegDto bizOne = this.bizService.findBizOneCustom(member.getBizId());
                model.addAttribute("biz", bizOne);
            } else {
                log.info("=== [detailMember] detail member info : 개인 ===");
                model.addAttribute("biz", null);
            }

            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedIdTag", idTag);
            model.addAttribute("selectedName", name);
        } catch (Exception e) {
            log.error("[detailMember] error: {}", e.getMessage());
        }

        return "pages/member/member_list_detail";
    }

    // 회원 등록
    @PostMapping("/new")
    public ResponseEntity<String> createMember(@Valid @RequestBody MemberRegDto dto) {
        log.info("=== create member info ===");

        try {
            this.memberService.saveMember(dto);
            return ResponseEntity.ok("회원 정보가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createMember] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("회원 정보 등록 중 오류 발생");
        }
    }

    // 회원 수정
    @PatchMapping("/update/{memberId}")
    public ResponseEntity<String> updateMember(@PathVariable("memberId") Long memberId, @RequestBody MemberRegDto dto) {
        log.info("=== update member info ===");

        try {
            this.memberService.updateMemberInfo(memberId, dto);
            log.info("=== member info update complete ===");
            return ResponseEntity.ok("회원 정보가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateMember] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("회원 정보 수정 중 오류 발생");
        }
    }

    // 비밀번호 변경
    @PatchMapping("/update/password/{memberId}")
    public ResponseEntity<Map<String, Object>> updateMemberPassword(@PathVariable("memberId") Long memberId, @RequestBody MemberPasswordDto dto) {
        log.info("=== update member password info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            Integer result = this.memberService.updateMemberPasswordInfo(memberId, dto);

            switch (result) {
                case 0-> response.put("message", "현재 비밀번호와 값이 일치하지 않습니다.");
                case 1 -> response.put("message", "비밀번호가 변경되었습니다.");
                case 2 -> response.put("message", "새 비밀번호 값이 일치하지 않습니다.");
                default -> response.put("message", "비밀번호 변경에 실패했습니다.");
            };
            
            response.put("state", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[updateMemberPassword] error: {}", e.getMessage());
            response.put("message", "비밀번호 변경 중 오류 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 회원 삭제
    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable("memberId") Long memberId) {
        log.info("=== delete member info ===");

        if (memberId == null) {
            log.error("memberId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("회원ID가 없습니다.");
        }

        try {
            this.memberService.deleteMember(memberId);
            return ResponseEntity.ok("회원 정보가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteMember] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("회원 정보 삭제 중 오류 발생");
        }
    }

    // 회원카드 조회
    @GetMapping("/tag/get/{idTag}")
    public ResponseEntity<?> findMemberAuthOne(@PathVariable("idTag") String idTag) {
        log.info("=== find member auth info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            MemberAuthDto authOne = this.memberService.findMemberAuthOne(idTag);

            if (authOne == null) {
                response.put("message", "조회된 데이터가 없습니다.");
                response.put("authInfo", Collections.emptyList());
                return ResponseEntity.ok(response);
            }

            response.put("authInfo", authOne);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[findMemberAuthOne] error: {}", e.getMessage());
            response.put("message", "서버 오류가 발생했습니다. 다시 시도해 주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 회원카드정보 수정
    @PatchMapping("/tag/update")
    public ResponseEntity<String> updateMemberAuth(@Valid @RequestBody MemberAuthDto dto) {
        log.info("=== update member auth info ===");
        
        try {
            MemberAuth auth = this.memberService.updateMemberAuthInfo(dto);

            if (auth == null) {
                log.info("=== member auth info update failure ===");
                return ResponseEntity.ok("회원카드정보 수정에 실패하였습니다.");
            }

            log.info("=== member auth info update complete ===");
            return ResponseEntity.ok("회원카드정보가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateMemberAuth] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("회원카드정보 수정 중 오류 발생");
        }
    }

    // 회원카드정보 삭제
    @DeleteMapping("/tag/delete/{idTag}")
    public ResponseEntity<String> deleteMemberAuth(@PathVariable("idTag") String idTag) {
        log.info("=== delete member auth info ===");

        if (idTag == null) {
            log.error("idTag is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("회원카드번호 정보가 없습니다.");
        }

        try {
            this.memberService.deleteMemberAuth(idTag);
            return ResponseEntity.ok("회원카드정보가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteMemberAuth] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("회원카드정보 삭제 중 오류 발생");
        }
    }

    // 회원약관 내용조회
    @GetMapping("/file/{conditionCode}")
    public ResponseEntity<Map<String, Object>> readFile(@PathVariable String conditionCode) {
        log.info("=== read condition file info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            String fileContent = this.memberService.readDocxFile(conditionCode);
            response.put("fileContent", fileContent);

            String conditionName = this.conditionService.findConditionName(conditionCode);
            response.put("conditionName", conditionName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[readFile] error: {}", e.getMessage());
            response.put("message", "회원약관 내용 조회 중 오류 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
