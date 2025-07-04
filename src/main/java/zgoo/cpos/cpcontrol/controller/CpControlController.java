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
import zgoo.cpos.cpcontrol.dto.ChangeConfigurationReqDto;
import zgoo.cpos.cpcontrol.dto.GetConfigurationReqDto;
import zgoo.cpos.cpcontrol.dto.PaymentTestRequestDto;
import zgoo.cpos.cpcontrol.dto.PaymentTestResponseDto;
import zgoo.cpos.cpcontrol.dto.RemoteStartTransactionDto;
import zgoo.cpos.cpcontrol.dto.RemoteStopTransactionDto;
import zgoo.cpos.cpcontrol.dto.ResetRequestDto;
import zgoo.cpos.cpcontrol.dto.TidSearchRequest;
import zgoo.cpos.cpcontrol.dto.TriggerMessageReqDto;
import zgoo.cpos.cpcontrol.dto.UpdateFirmwareDto;
import zgoo.cpos.cpcontrol.message.changeconfiguration.ChangeConfigurationResponse;
import zgoo.cpos.cpcontrol.message.firmware.UpdateFirmwareResponse;
import zgoo.cpos.cpcontrol.message.getconfiguration.GetConfigurationResponse;
import zgoo.cpos.cpcontrol.message.remotecharging.RemoteStartTransactionResponse;
import zgoo.cpos.cpcontrol.message.remotecharging.RemoteStopTransactionResponse;
import zgoo.cpos.cpcontrol.message.reset.ResetResponse;
import zgoo.cpos.cpcontrol.message.trigger.TriggerMessageResponse;
import zgoo.cpos.cpcontrol.service.CpControlService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/control")
public class CpControlController {

    private final CpControlService cpControlService;

    @PostMapping("/remoteStartTransaction")
    public ResponseEntity<?> remoteStartTransaction(@RequestBody RemoteStartTransactionDto request) {
        log.info("Remote Start Transaction Request(/remoteStartTransaction) : {}", request.toString());
        try {
            RemoteStartTransactionResponse response = cpControlService.remoteStartTransaction(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/remoteStopTransaction")
    public ResponseEntity<?> remoteStopTransaction(@RequestBody RemoteStopTransactionDto request) {
        log.info("Remote Stop Transaction Request(/remoteStopTransaction) : {}", request.toString());
        try {
            RemoteStopTransactionResponse response = cpControlService.remoteStopTransaction(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/fwupdate")
    public ResponseEntity<?> fwupdate(@RequestBody UpdateFirmwareDto request) {
        log.info("Firmware Update Request(/fwupdate) : {}", request.toString());
        try {
            UpdateFirmwareResponse response = cpControlService.updateFirmware(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody ResetRequestDto request) {
        log.info("Reset Request(/reset) : {}", request.toString());
        try {
            ResetResponse response = cpControlService.reset(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/changeConfiguration")
    public ResponseEntity<?> changeConfiguration(@RequestBody ChangeConfigurationReqDto request) {
        log.info("ChangeConfiguration Request(/changeConfiguration) : {}", request.toString());
        try {
            ChangeConfigurationResponse response = cpControlService.changeConfiguration(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/getConfiguration")
    public ResponseEntity<?> getConfiguration(@RequestBody GetConfigurationReqDto request) {
        log.info("Get Configuration Request(/getConfiguration) : {}", request.toString());
        try {
            GetConfigurationResponse response = cpControlService.getConfiguration(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/trigger")
    public ResponseEntity<?> trigger(@RequestBody TriggerMessageReqDto request) {
        log.info("Trigger Request(/trigger) : {}", request.toString());
        try {
            TriggerMessageResponse response = cpControlService.trigger(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

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

    @PostMapping("/payment/tid")
    public ResponseEntity<?> getTidData(@RequestBody TidSearchRequest request) {
        try {
            String tid = request.getTid();
            String approvalDate = request.getApprovalDate();

            log.info("TID 조회 API 호출 Start - TID: {}, 승인일자: {}", tid, approvalDate);

            // 서비스 메서드 호출 (서비스 메서드도 수정 필요)
            Map<String, Object> result = cpControlService.getTidData(tid, approvalDate);

            if (result != null && !result.isEmpty()) {
                // 조회 결과가 있는 경우
                return ResponseEntity.ok(result);
            } else {
                // 조회 결과가 없는 경우
                Map<String, String> response = new HashMap<>();
                response.put("status", "no_data");
                response.put("message", "해당 TID와 승인일자로 조회된 데이터가 없습니다.");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("TID 조회 API 처리 중 오류 발생", e);
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

    @GetMapping("/payment/settlement/data")
    public ResponseEntity<?> getSettlementData() {
        log.info("정산대사 조회 API 호출 Start");
        try {
            cpControlService.getSettlementData();

            // 성공 응답 반환
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "정산대사 조회 요청이 성공적으로 처리되었습니다. 서버 로그를 확인하세요.");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("정산대사 조회 API 처리 중 오류 발생", e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
