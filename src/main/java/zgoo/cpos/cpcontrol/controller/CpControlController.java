package zgoo.cpos.cpcontrol.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.cpcontrol.dto.CancelTestRequestDto;
import zgoo.cpos.cpcontrol.dto.CancelTestRsponseDto;
import zgoo.cpos.cpcontrol.dto.ChangeConfigurationReqDto;
import zgoo.cpos.cpcontrol.dto.DataTransferDto;
import zgoo.cpos.cpcontrol.dto.GetConfigurationReqDto;
import zgoo.cpos.cpcontrol.dto.PaymentTestRequestDto;
import zgoo.cpos.cpcontrol.dto.PaymentTestResponseDto;
import zgoo.cpos.cpcontrol.dto.RemoteStartTransactionDto;
import zgoo.cpos.cpcontrol.dto.RemoteStopTransactionDto;
import zgoo.cpos.cpcontrol.dto.ResetRequestDto;
import zgoo.cpos.cpcontrol.dto.TidSearchRequest;
import zgoo.cpos.cpcontrol.dto.TriggerMessageReqDto;
import zgoo.cpos.cpcontrol.dto.UpdateFirmwareDto;
import zgoo.cpos.cpcontrol.dto.VasEncKeyRequestDto;
import zgoo.cpos.cpcontrol.dto.VasGetEncKeyDto;
import zgoo.cpos.cpcontrol.message.changeconfiguration.ChangeConfigurationResponse;
import zgoo.cpos.cpcontrol.message.datatransfer.DataTransferResponse;
import zgoo.cpos.cpcontrol.message.firmware.UpdateFirmwareResponse;
import zgoo.cpos.cpcontrol.message.getconfiguration.GetConfigurationResponse;
import zgoo.cpos.cpcontrol.message.remotecharging.RemoteStartTransactionResponse;
import zgoo.cpos.cpcontrol.message.remotecharging.RemoteStopTransactionResponse;
import zgoo.cpos.cpcontrol.message.reset.ResetResponse;
import zgoo.cpos.cpcontrol.message.trigger.TriggerMessageResponse;
import zgoo.cpos.cpcontrol.service.CpControlService;
import zgoo.cpos.type.ocpp.DataTransferStatus;

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

    @PostMapping("/requestEncKey")
    public ResponseEntity<?> requestEncKey(@RequestBody VasEncKeyRequestDto request) {
        log.info("Request Enc Key from Environment Ministry API(/requestEncKey) : {}", request.toString());
        try {
            // 환경부 API 호출
            String environmentApiUrl = "http://121.141.6.27:35083/v1/charger/battery/getEncKey";

            // 요청 body 구성 (샘플 메시지 형식에 맞춤)
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("bid", request.getBid());
            requestBody.put("bkey", request.getBkey());
            requestBody.put("chargerCnt", request.getChargerCnt()); // 현재는 단일 충전기만 처리

            // 충전기 키셋 구성
            List<Map<String, String>> chargerKeySet = new ArrayList<>();
            Map<String, String> chargerKey = new HashMap<>();
            chargerKey.put("chargerId", request.getBid() + request.getChargerId());
            chargerKey.put("keyId", ""); // keyId는 환경부에서 생성하므로 빈 값으로 전송
            chargerKeySet.add(chargerKey);
            requestBody.put("chargerKeySet", chargerKeySet);

            log.info("Environment Ministry API Request Body: {}", requestBody);

            // RestTemplate을 사용하여 외부 API 호출
            RestTemplate restTemplate = new RestTemplate();

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // HTTP 엔티티 생성
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(
                    requestBody, headers);

            // 외부 API 호출
            ResponseEntity<Map> response = restTemplate.postForEntity(environmentApiUrl, entity, Map.class);

            log.info("Environment Ministry API Response: {}", response.getBody());

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            log.error("Environment Ministry API 호출 중 오류 발생", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "환경부 API 호출 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/getEncKey")
    public ResponseEntity<?> getEncKey(@RequestBody VasGetEncKeyDto request) {
        log.info("Get Enc Key Request(/getEncKey) : {}", request.toString());
        try {
            // 환경부 API 응답 데이터 처리
            Map<String, Object> response = new HashMap<>();
            DataTransferResponse dataTransferResponse = new DataTransferResponse();

            // 성공 여부 확인
            if ("0".equals(request.getResultCode()) || "1".equals(request.getResultCode())) {
                response.put("status", "Accepted");
                response.put("message", "암호화 키 요청이 성공적으로 처리되었습니다.");

                // 환경부 응답의 주요 정보 로깅
                log.info(
                        "Environment Ministry API Success Response - resultCode: {}, successCnt: {}, errCode: {}, errMsg: {}, chargerKeySet: {}",
                        request.getResultCode(), request.getSuccessCnt(), request.getErrCode(), request.getErrMsg(),
                        request.getChargerKeySet());

                // chargerKeySet 정보가 있다면 로깅
                if (!request.getChargerKeySet().isEmpty()) {
                    // List<VasGetEncKeyDto.ChargerKeySet> chargerKeySet =
                    // request.getChargerKeySet();
                    // for (VasGetEncKeyDto.ChargerKeySet chargerKey : chargerKeySet) {
                    // log.info(
                    // "Charger Key Info - chargerId: {}, keyId: {}, retVal: {}, encryptPub:{},
                    // validTime:{}",
                    // chargerKey.getChargerId(), chargerKey.getKeyId(), chargerKey.getRetVal(),
                    // chargerKey.getEncryptPub(), chargerKey.getValidTime());
                    // }
                    DataTransferDto dataTransferDto = new DataTransferDto();
                    dataTransferDto.setChargerId(request.getChargerKeySet().get(0).getChargerId());
                    dataTransferDto.setVendorId("kr.co.zgoo");
                    dataTransferDto.setMessageId("meBattEncKey");

                    // JsonNode를 이용해 data 설정
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode dataNode = objectMapper.createObjectNode()
                            .put("resultTime", request.getResultTime())
                            .put("chargerId", request.getChargerKeySet().get(0).getChargerId())
                            .put("keyId", request.getChargerKeySet().get(0).getKeyId())
                            .put("encryptPub", request.getChargerKeySet().get(0).getEncryptPub())
                            .put("signData", request.getChargerKeySet().get(0).getSignData())
                            .put("validTime", request.getChargerKeySet().get(0).getValidTime())
                            .put("retVal", request.getChargerKeySet().get(0).getRetVal());

                    dataTransferDto.setData(dataNode);
                    dataTransferResponse = cpControlService.dataTransfer(dataTransferDto);

                    if (dataTransferResponse.getStatus() == DataTransferStatus.Accepted) {
                        response.put("status", "Accepted");
                        response.put("message", "암호화 키 요청/배포가 성공적으로 처리되었습니다.");
                    } else {
                        response.put("status", "Rejected");
                        response.put("message",
                                "암호화 키 배포 오류(by 충전기 >> status:" + dataTransferResponse.getStatus() + ")");
                    }
                }
            } else {
                response.put("status", "Rejected");
                response.put("message", "환경부 API 처리 실패(errMsg:" + request.getErrMsg() + ", errCode:"
                        + request.getErrCode() + ", resultCode:" + request.getResultCode() + ")");
                log.error("Environment Ministry API Error - resultCode: {}, errMsg: {}",
                        request.getResultCode(), request.getErrMsg());
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Get Enc Key 처리 중 오류 발생", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "Rejected");
            errorResponse.put("message", "암호화 키 요청 처리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
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
