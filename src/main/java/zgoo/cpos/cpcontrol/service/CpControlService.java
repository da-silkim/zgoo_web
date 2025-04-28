package zgoo.cpos.cpcontrol.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
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
    @Value("${nicepay.merchant.id}")
    private String merchantID;

    @Value("${nicepay.merchant.key}")
    private String merchantKey;

    @Value("${nicepay.billing.url}")
    private String billingUrl;
    private final String paymentTestUrl = "http://192.168.30.120:9999/api/payment/test";
    private final String cancelTestUrl = "http://192.168.30.120:9999/api/payment/cancel";
    private final String tradeDataUrl = "https://data.nicepay.co.kr/recon/api";
    private final String transUrl = "https://data.nicepay.co.kr/trans/api";

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

    public void getSettlementData() {
        try {
            // 공통 header
            // sid
            String sid = "0301003"; // 정산대사 요청 SID
            // trDtm
            String trDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            // gubun
            String gubun = "S";
            // resCode
            String resCode = "";
            // resMsg
            String resMsg = "";

            // body
            // usrId
            String usrId = "dongatest";
            // encKey
            String encKey = generateEncKey(sid, usrId, trDtm, merchantKey);
            // svcCd
            String svcCd = "01";
            // dtDiv (거래기준, 0:정산일 기준, 1:거래일기준)
            String dtDiv = "0";
            // dt
            // String dt =
            // LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String dt = "20250410";
            // frTm
            // toTm
            // idCl
            String idCl = "2";
            // searchID
            String searchID = merchantID;

            // 요청데이터준비
            Map<String, Object> requestMap = new HashMap<>();

            // header정보
            Map<String, Object> header = new HashMap<>();
            header.put("sid", sid);
            header.put("trDtm", trDtm);
            header.put("gubun", gubun);
            header.put("resCode", resCode);
            header.put("resMsg", resMsg);

            // body 정보
            Map<String, Object> body = new HashMap<>();
            body.put("usrId", usrId);
            body.put("encKey", encKey);
            body.put("svcCd", svcCd);
            body.put("dtDiv", dtDiv);
            body.put("dt", dt);
            body.put("idCl", idCl);
            body.put("searchID", searchID);

            // 요청데이터 준비
            requestMap.put("header", header);
            requestMap.put("body", body);

            // 요청데이터 로깅
            log.info("요청데이터: {}", objectMapper.writeValueAsString(requestMap));

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // HTTP 요청 엔티티 생성
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

            // 외부 서버로 POST 요청전송
            log.info("외부 서버({})로 POST 요청 전송", tradeDataUrl);
            long startTime = System.currentTimeMillis();

            ResponseEntity<String> response = restTemplate.postForEntity(tradeDataUrl, entity, String.class);

            long endTime = System.currentTimeMillis();
            log.info("외부 서버 응답 수신 - 소요시간: {}ms, 상태코드: {}", (endTime - startTime), response.getStatusCodeValue());

            // 응답 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                // 응답 데이터 로깅
                log.info("정산대사 조회 응답 데이터: {}", response.getBody());
                log.info("정산대사 조회 요청 성공");
            } else {
                log.error("정산대사 조회 요청 실패 - 상태코드: {}, 응답: {}",
                        response.getStatusCodeValue(), response.getBody());
                throw new RuntimeException("정산대사 조회 요청이 실패했습니다. 상태 코드: " + response.getStatusCodeValue());
            }

        } catch (Exception e) {
            log.error("정산대사 조회 처리 중 예외 발생", e);
            throw new RuntimeException("정산대사 조회 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * TID 데이터 조회 메서드 - 결과 반환 기능 추가
     * 
     * @param tid          거래 ID
     * @param approvalDate 승인일자(YYYYMMDD)
     * @return 조회 결과 데이터
     */
    public Map<String, Object> getTidData(String tid, String approvalDate) {
        try {
            // 공통 header
            // sid
            String sid = "0601001"; // 실시간 TID조회
            // trDtm
            String trDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            // gubun
            String gubun = "S";
            // resCode
            String resCode = "";
            // resMsg
            String resMsg = "";

            // body
            // mid
            String mid = merchantID;
            // encKey
            String encKey = generateEncKey(sid, mid, trDtm, merchantKey);
            // appDt(승인일자(YYYYMMDD))
            String appDt = approvalDate;
            // svcCd
            String svcCd = "01"; // 신용카드
            // trNo (PG거래번호)
            String trNo = tid;
            // trType (PG거래번호 : 0, 가맹점 주문번호:1)
            String trType = "0";
            // trGubn
            String trGubun = "1";

            // 요청데이터준비
            Map<String, Object> requestMap = new HashMap<>();

            // header정보
            Map<String, Object> header = new HashMap<>();
            header.put("sid", sid);
            header.put("trDtm", trDtm);
            header.put("gubun", gubun);
            header.put("resCode", resCode);
            header.put("resMsg", resMsg);

            // body 정보
            Map<String, Object> body = new HashMap<>();
            body.put("mid", mid);
            body.put("encKey", encKey);
            body.put("appDt", appDt);
            body.put("svcCd", svcCd);
            body.put("trNo", trNo);
            body.put("trType", trType);
            body.put("trGubun", trGubun);

            // 요청데이터 준비
            requestMap.put("header", header);
            requestMap.put("body", body);

            // 요청데이터 로깅
            log.info("요청데이터: {}", objectMapper.writeValueAsString(requestMap));

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // HTTP 요청 엔티티 생성
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

            // 외부 서버로 POST 요청전송
            log.info("외부 서버({})로 POST 요청 전송", transUrl);
            long startTime = System.currentTimeMillis();

            ResponseEntity<String> response = restTemplate.postForEntity(transUrl, entity, String.class);

            long endTime = System.currentTimeMillis();
            log.info("외부 서버 응답 수신 - 소요시간: {}ms, 상태코드: {}", (endTime - startTime), response.getStatusCodeValue());

            // 응답 처리 및 결과 반환
            if (response.getStatusCode().is2xxSuccessful()) {
                // 응답 데이터 로깅
                log.info("실시간 TID 조회 응답 데이터: {}", response.getBody());
                log.info("실시간 TID 조회 요청 성공");

                // JSON 응답을 Map으로 변환
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(),
                        new TypeReference<Map<String, Object>>() {
                        });

                // 응답 데이터 가공 및 필요한 정보만 추출
                Map<String, Object> result = new HashMap<>();

                // 응답 헤더 정보 추출
                if (responseMap.containsKey("header")) {
                    Map<String, Object> responseHeader = (Map<String, Object>) responseMap.get("header");
                    result.put("resCode", responseHeader.get("resCode"));
                    result.put("resMsg", responseHeader.get("resMsg"));
                }

                // 응답 바디 정보 추출 및 가공
                if (responseMap.containsKey("body")) {
                    Map<String, Object> responseBody = (Map<String, Object>) responseMap.get("body");

                    // 클라이언트에 필요한 정보만 추출하여 반환
                    result.put("tid", tid);
                    result.put("approvalDate", appDt);

                    // 거래 정보 추출
                    if (responseBody.containsKey("trInfo")) {
                        result.put("trInfo", responseBody.get("trInfo"));
                    }

                    // 카드 정보 추출 (마스킹 처리 등 보안 고려)
                    if (responseBody.containsKey("cardInfo")) {
                        Map<String, Object> cardInfo = (Map<String, Object>) responseBody.get("cardInfo");
                        Map<String, Object> maskedCardInfo = new HashMap<>();

                        // 카드번호는 마스킹 처리
                        if (cardInfo.containsKey("cardNo")) {
                            String cardNo = (String) cardInfo.get("cardNo");
                            if (cardNo != null && cardNo.length() > 8) {
                                String maskedCardNo = cardNo.substring(0, 6) + "******" +
                                        (cardNo.length() > 12 ? cardNo.substring(cardNo.length() - 4) : "");
                                maskedCardInfo.put("cardNo", maskedCardNo);
                            } else {
                                maskedCardInfo.put("cardNo", cardNo);
                            }
                        }

                        // 기타 필요한 카드 정보
                        if (cardInfo.containsKey("cardNm"))
                            maskedCardInfo.put("cardNm", cardInfo.get("cardNm"));
                        if (cardInfo.containsKey("issuerNm"))
                            maskedCardInfo.put("issuerNm", cardInfo.get("issuerNm"));
                        if (cardInfo.containsKey("acquirerNm"))
                            maskedCardInfo.put("acquirerNm", cardInfo.get("acquirerNm"));

                        result.put("cardInfo", maskedCardInfo);
                    }

                    // 금액 정보 추출
                    if (responseBody.containsKey("amount")) {
                        result.put("amount", responseBody.get("amount"));
                    }

                    // 승인 정보 추출
                    if (responseBody.containsKey("approvalInfo")) {
                        result.put("approvalInfo", responseBody.get("approvalInfo"));
                    }

                    // 취소 정보 추출 (있는 경우)
                    if (responseBody.containsKey("cancelInfo")) {
                        result.put("cancelInfo", responseBody.get("cancelInfo"));
                    }
                }

                return result;
            } else {
                log.error("실시간 TID 조회 요청 실패 - 상태코드: {}, 응답: {}",
                        response.getStatusCodeValue(), response.getBody());

                // 오류 정보를 담은 결과 반환
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("error", true);
                errorResult.put("statusCode", response.getStatusCodeValue());
                errorResult.put("message", "실시간 TID 조회 요청이 실패했습니다.");

                try {
                    // 응답 본문이 JSON인 경우 파싱하여 추가 정보 제공
                    Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(),
                            new TypeReference<Map<String, Object>>() {
                            });
                    errorResult.put("errorDetails", errorResponse);
                } catch (Exception e) {
                    // JSON 파싱 실패 시 원본 응답 텍스트 포함
                    errorResult.put("responseBody", response.getBody());
                }

                return errorResult;
            }

        } catch (Exception e) {
            log.error("실시간 TID 조회 처리 중 예외 발생", e);

            // 예외 정보를 담은 결과 반환
            Map<String, Object> exceptionResult = new HashMap<>();
            exceptionResult.put("error", true);
            exceptionResult.put("message", "실시간 TID 조회 처리 중 오류가 발생했습니다: " + e.getMessage());
            exceptionResult.put("exceptionType", e.getClass().getSimpleName());

            return exceptionResult;
        }
    }

    public void getTradeData() {
        try {
            // 공통 header
            // sid
            String sid = "0301001"; // 거래대사 요청 SID
            // trDtm
            String trDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            // gubun
            String gubun = "S";
            // resCode
            String resCode = "";
            // resMsg
            String resMsg = "";

            // body
            // usrId
            String usrId = "dongatest";
            // encKey
            String encKey = generateEncKey(sid, usrId, trDtm, merchantKey);
            // svcCd
            String svcCd = "01";
            // trCl
            String trCl = "0";
            // dt
            String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            // frTm
            // toTm
            // idCl
            String idCl = "2";
            // searchID
            String searchID = merchantID;

            // 요청데이터준비
            Map<String, Object> requestMap = new HashMap<>();

            // header정보
            Map<String, Object> header = new HashMap<>();
            header.put("sid", sid);
            header.put("trDtm", trDtm);
            header.put("gubun", gubun);
            header.put("resCode", resCode);
            header.put("resMsg", resMsg);

            // body 정보
            Map<String, Object> body = new HashMap<>();
            body.put("usrId", usrId);
            body.put("encKey", encKey);
            body.put("svcCd", svcCd);
            body.put("trCl", trCl);
            body.put("dt", dt);
            body.put("frTm", "");
            body.put("toTm", "");
            body.put("idCl", idCl);
            body.put("searchID", searchID);

            // 요청데이터 준비
            requestMap.put("header", header);
            requestMap.put("body", body);

            // 요청데이터 로깅
            log.info("요청데이터: {}", objectMapper.writeValueAsString(requestMap));

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // HTTP 요청 엔티티 생성
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

            // 외부 서버로 POST 요청전송
            log.info("외부 서버({})로 POST 요청 전송", tradeDataUrl);
            long startTime = System.currentTimeMillis();

            ResponseEntity<String> response = restTemplate.postForEntity(tradeDataUrl, entity, String.class);

            long endTime = System.currentTimeMillis();
            log.info("외부 서버 응답 수신 - 소요시간: {}ms, 상태코드: {}", (endTime - startTime), response.getStatusCodeValue());

            // 응답 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                // 응답 데이터 로깅
                log.info("거래대사 조회 응답 데이터: {}", response.getBody());
                log.info("거래대사 조회 요청 성공");
            } else {
                log.error("거래대사 조회 요청 실패 - 상태코드: {}, 응답: {}",
                        response.getStatusCodeValue(), response.getBody());
                throw new RuntimeException("거래대사 조회 요청이 실패했습니다. 상태 코드: " + response.getStatusCodeValue());
            }

        } catch (Exception e) {
            log.error("거래대사 조회 처리 중 예외 발생", e);
            throw new RuntimeException("거래대사 조회 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    private String generateEncKey(String sid, String userid, String trDtm, String merchantKey) {
        try {
            // 평문 생성
            String plainText = sid + userid + trDtm + merchantKey;

            // SHA-256 해시
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plainText.getBytes(StandardCharsets.UTF_8));

            // Hex 인코딩
            return bytesToHex(md.digest());
        } catch (Exception e) {
            log.error("SignData 생성 오류", e);
            throw new RuntimeException("위변조 검증 데이터 생성 오류", e);
        }
    }

    /**
     * 바이트 배열을 16진수 문자열로 변환
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
