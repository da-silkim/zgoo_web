package zgoo.cpos.cpcontrol.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.cpcontrol.dto.CancelTestRequestDto;
import zgoo.cpos.cpcontrol.dto.CancelTestRsponseDto;
import zgoo.cpos.cpcontrol.dto.PaymentTestRequestDto;
import zgoo.cpos.cpcontrol.dto.PaymentTestResponseDto;
import zgoo.cpos.cpcontrol.service.CpControlService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/corp")
public class CpControlController {

    private final CpControlService cpControlService;

    @PostMapping("/payment/test")
    public ResponseEntity<?> testPayment(@RequestBody PaymentTestRequestDto request) {
        try {
            PaymentTestResponseDto response = cpControlService.processTestPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // 에러 응답을 위한 내부 클래스
    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @PostMapping("/payment/test/cancel")
    public ResponseEntity<?> testCancelPayment(@RequestBody CancelTestRequestDto request) {
        try {
            CancelTestRsponseDto response = cpControlService.processTestCancelPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/payment/trade/data")
    public ResponseEntity<?> getTradeData() {
        log.info("거래대사 조회 API 호출 Start");
        try {
            cpControlService.getTradeData();

            // 성공 응답 반환
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "거래대사 조회 요청이 성공적으로 처리되었습니다. 서버 로그를 확인하세요.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("거래대사 조회 API 처리 중 오류 발생", e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
