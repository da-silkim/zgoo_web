package zgoo.cpos.cpcontrol.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.cpcontrol.dto.CancelTestRequestDto;
import zgoo.cpos.cpcontrol.dto.CancelTestRsponseDto;
import zgoo.cpos.cpcontrol.dto.PaymentTestRequestDto;
import zgoo.cpos.cpcontrol.dto.PaymentTestResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class CpControlService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String paymentTestUrl = "http://192.168.30.120:9999/api/payment/test";
    private final String cancelTestUrl = "http://192.168.30.120:9999/api/payment/cancel";

    public PaymentTestResponseDto processTestPayment(PaymentTestRequestDto request) {
        try {
            log.info("결제 테스트 요청 시작 : 금액:{}, orderId:{}", request.getAmount(), request.getOrderId());
            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 데이터 준비
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("amount", request.getAmount());
            requestMap.put("orderId", request.getOrderId());

            // 요청 데이터 로깅
            log.info("외부 서버로 전송할 요청 데이터: {}", objectMapper.writeValueAsString(requestMap));

            // HTTP 요청 엔티티 생성
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

            // 외부 서버로 POST 요청 전송
            log.info("외부 서버({})로 POST 요청 전송", paymentTestUrl);
            long startTime = System.currentTimeMillis();

            // 외부 서버로 POST 요청 전송
            ResponseEntity<String> response = restTemplate.postForEntity(
                    paymentTestUrl,
                    entity,
                    String.class);

            long endTime = System.currentTimeMillis();
            log.info("외부 서버 응답 수신 - 소요시간: {}ms, 상태코드: {}",
                    (endTime - startTime), response.getStatusCodeValue());

            // 응답 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                // 응답 데이터 로깅
                log.info("외부 서버 응답 데이터: {}", response.getBody());
                // JSON 응답을 PaymentTestResponse 객체로 변환
                // JSON 응답을 PaymentTestResponse 객체로 변환
                PaymentTestResponseDto paymentResponse = objectMapper.readValue(response.getBody(),
                        PaymentTestResponseDto.class);

                log.info("결제 테스트 요청 성공 - 결과코드: {}, 결과메시지: {}, 트랜잭션ID: {}",
                        paymentResponse.getResultCode(),
                        paymentResponse.getResultMsg(),
                        paymentResponse.getAuthCode());

                return paymentResponse;
            } else {
                log.error("결제 테스트 요청 실패 - 상태코드: {}, 응답: {}",
                        response.getStatusCodeValue(), response.getBody());
                throw new RuntimeException("결제 테스트 요청이 실패했습니다. 상태 코드: " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("결제 테스트 처리 중 예외 발생", e);
            throw new RuntimeException("결제 테스트 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public CancelTestRsponseDto processTestCancelPayment(CancelTestRequestDto request) {
        try {
            log.info("결제 취소 테스트 요청 시작 : 금액:{}, orderId:{}", request.getAmount(), request.getOrderId());

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 데이터 준비
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("amount", request.getAmount());
            requestMap.put("orderId", request.getOrderId());
            requestMap.put("tid", request.getTid());
            // 요청 데이터 로깅
            log.info("외부 서버로 전송할 요청 데이터: {}", objectMapper.writeValueAsString(requestMap));

            // HTTP 요청 엔티티 생성
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

            // 외부 서버로 POST 요청 전송
            log.info("외부 서버({})로 POST 요청 전송", cancelTestUrl);
            long startTime = System.currentTimeMillis();

            // 외부 서버로 POST 요청 전송
            ResponseEntity<String> response = restTemplate.postForEntity(
                    cancelTestUrl,
                    entity,
                    String.class);

            long endTime = System.currentTimeMillis();
            log.info("외부 서버 응답 수신 - 소요시간: {}ms, 상태코드: {}",
                    (endTime - startTime), response.getStatusCodeValue());

            // 응답 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                // 응답 데이터 로깅
                log.info("외부 서버 응답 데이터: {}", response.getBody());
                // JSON 응답을 PaymentTestResponse 객체로 변환
                CancelTestRsponseDto paymentResponse = objectMapper.readValue(response.getBody(),
                        CancelTestRsponseDto.class);

                log.info("결제 취소 테스트 요청 성공 - 결과코드: {}, 결과메시지: {}, 트랜잭션ID: {}",
                        paymentResponse.getResultCode(),
                        paymentResponse.getResultMsg(),
                        paymentResponse.getTid());

                return paymentResponse;
            } else {
                log.error("결제 취소 테스트 요청 실패 - 상태코드: {}, 응답: {}",
                        response.getStatusCodeValue(), response.getBody());
                throw new RuntimeException("결제 취소 테스트 요청이 실패했습니다. 상태 코드: " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("결제 취소 테스트 처리 중 예외 발생", e);
            throw new RuntimeException("결제 취소 테스트 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}
