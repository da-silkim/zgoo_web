package zgoo.cpos.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;
import zgoo.cpos.dto.payment.BillkeyIssueRequestDto;
import zgoo.cpos.dto.payment.BillkeyIssueResponseDto;
import zgoo.cpos.service.BizService;
import zgoo.cpos.service.PaymentService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/corp")
public class BizController {

    private final BizService bizService;
    private final PaymentService paymentService;

    // 법인 단건 조회
    @GetMapping("/get/{bizId}")
    public ResponseEntity<BizInfoRegDto> findBizOne(@PathVariable("bizId") Long bizId) {
        log.info("=== find biz info ===");

        try {
            BizInfoRegDto bizOne = this.bizService.findBizOne(bizId);

            if (bizOne == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(bizOne);
        } catch (Exception e) {
            log.error("[findBizInfoOne] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 법인 단건 조회(회원 리스트에서 사용)
    @GetMapping("/get/custom/{bizId}")
    public ResponseEntity<BizInfoRegDto> findBizOneCustom(@PathVariable("bizId") Long bizId) {
        log.info("=== find biz info(custom) ===");

        try {
            BizInfoRegDto bizOne = this.bizService.findBizOneCustom(bizId);
            return ResponseEntity.ok(bizOne);
        } catch (Exception e) {
            log.error("[findBizInfoOne] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 법인명으로 조회
    @GetMapping("/search/{bizName}")
    public ResponseEntity<?> findBizList(@PathVariable("bizName") String bizName,
            Model model) {
        log.info("=== find biz info by bizName ===");

        Map<String, Object> response = new HashMap<>();

        try {
            List<BizInfoRegDto> bizList = this.bizService.findBizList(bizName);

            if (bizList == null || bizList.isEmpty()) {
                response.put("message", "조회된 데이터가 없습니다.");
                response.put("bizList", Collections.emptyList());
                return ResponseEntity.ok(response);
            }

            response.put("bizList", bizList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[findBizInfoOne] error: {}", e.getMessage());
            response.put("message", "서버 오류가 발생했습니다. 다시 시도해 주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 법인 정보 등록
    @PostMapping("/new")
    public ResponseEntity<String> createBiz(@Valid @RequestBody BizInfoRegDto dto) {
        log.info("=== create biz request info : {}", dto);

        try {
            this.bizService.saveBiz(dto);
            return ResponseEntity.ok("법인 정보가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createBiz] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("법인 정보 등록 중 오류 발생");
        }
    }

    // 법인 정보 수정
    @PatchMapping("/update/{bizId}")
    public ResponseEntity<String> updateBiz(@PathVariable("bizId") Long bizId, @RequestBody BizInfoRegDto dto) {
        log.info("=== update biz info ===");

        try {
            this.bizService.updateBizInfo(bizId, dto);
            log.info("=== biz info update complete ===");
            return ResponseEntity.ok("법인 정보가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateBiz] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("법인 정보 수정 중 오류 발생");
        }
    }

    // 법인 정보 삭제
    @DeleteMapping("/delete/{bizId}")
    public ResponseEntity<String> deleteBiz(@PathVariable("bizId") Long bizId) {
        log.info("=== delete biz info ===");

        if (bizId == null) {
            log.error("bizId id is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("법인ID가 없습니다.");
        }

        try {
            this.bizService.deleteBiz(bizId);
            return ResponseEntity.ok("법인 정보가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteBiz] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("법인 정보 삭제 중 오류 발생");
        }
    }

    // 법인카드 빌키 발급 요청
    @PostMapping("/billkey")
    public ResponseEntity<BillkeyIssueResponseDto> issueBillkey(@RequestBody BillkeyIssueRequestDto requestDto) {
        log.info("Billkey Issue Request dto:{}", requestDto);

        try {
            // 빌키 발급 요청 로직
            BillkeyIssueResponseDto responseDto = this.paymentService.issueBillkey(requestDto);
            log.info("Billkey Issue Response Result:{}", responseDto.getResultCode());

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("[issueBillkey] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BillkeyIssueResponseDto());
        }
    }
}
