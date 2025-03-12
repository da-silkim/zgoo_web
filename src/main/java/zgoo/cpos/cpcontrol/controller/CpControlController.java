package zgoo.cpos.cpcontrol.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
