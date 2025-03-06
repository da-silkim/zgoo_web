package zgoo.cpos.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.payment.BillkeyIssueRequestDto;
import zgoo.cpos.dto.payment.BillkeyIssueResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;;

    @Value("${nicepay.merchant.id}")
    private String merchantID;

    @Value("${nicepay.merchant.key}")
    private String merchantKey;

    @Value("${nicepay.billing.url}")
    private String billingUrl;

    @Transactional
    public BillkeyIssueResponseDto issueBillkey(BillkeyIssueRequestDto request) {
        // 현재 시간 생성 (yyyyMMddHHmmss)
        String ediDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // 상점 주문번호 생성
        String moid = generateMoid();

        // 요청 파라미터 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("MID", merchantID); // 상점 아이디
        params.add("EdiDate", ediDate); // 전문 생성일시
        params.add("Moid", moid); // 상점 주문번호

        // EncData 생성 (카드정보 암호화)
        String encData = generateEncData(request);
        params.add("EncData", encData);
        log.info("=== EncData: {}", encData);

        // SignData 생성 (위변조 검증 데이터)
        String signData = generateSignData(ediDate, moid);
        params.add("SignData", signData);
        log.info("==== SignData: {}", signData);

        // // 추가 파라미터 (선택사항)
        // if (request.getBuyerName() != null) {
        // params.add("BuyerName", request.getBuyerName());
        // }
        // if (request.getBuyerEmail() != null) {
        // params.add("BuyerEmail", request.getBuyerEmail());
        // }
        // if (request.getBuyerTel() != null) {
        // params.add("BuyerTel", request.getBuyerTel());
        // }

        // // 응답 파라미터 인코딩 방식 설정
        // params.add("CharSet", "utf-8");

        // // 응답전문 유형 설정 (Key=Value 형식)
        // params.add("EdiType", "KV");

        // API 호출 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        log.info("나이스페이먼츠 빌키 발급 요청: MID={}, Moid={}", merchantID, moid);

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(billingUrl, entity,
                    String.class);

            String responseBody = responseEntity.getBody();
            log.info("나이스페이먼츠 빌키 발급 응답: {}", responseBody);

            // 응답 파싱
            BillkeyIssueResponseDto response = parseResponse(responseBody);

            // 빌키 발급 성공 시 법인 정보 업데이트
            if (response != null && "F100".equals(response.getResultCode())) {
                updateCorporation(request, response);
            }

            return response;
        } catch (Exception e) {
            log.error("나이스페이먼츠 빌키 발급 오류", e);
            throw new RuntimeException("빌키 발급 중 오류가 발생했습니다", e);
        }
    }

    /**
     * EncData 생성 (카드정보 암호화)
     * 암호화 알고리즘: AES/ECB/PKCS5padding
     * 암호결과 인코딩: Hex Encoding
     * 암호 key: MID에 부여된 MerchantKey 앞 16자리
     */
    private String generateEncData(BillkeyIssueRequestDto request) {
        try {
            // 평문 생성
            StringBuilder plainText = new StringBuilder();
            plainText.append("CardNo=").append(request.getCardNum());
            plainText.append("&ExpYear=").append(request.getCardExpire().substring(0, 2));
            plainText.append("&ExpMonth=").append(request.getCardExpire().substring(2));
            plainText.append("&IDNo=").append(request.getBizNo());
            plainText.append("&CardPw=").append(request.getCardPwd());

            // 암호화 키 (상점키 앞 16자리)
            String encKey = merchantKey.substring(0, 16);

            // AES 암호화
            SecretKeySpec secretKey = new SecretKeySpec(encKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encrypted = cipher.doFinal(plainText.toString().getBytes(StandardCharsets.UTF_8));

            // Hex 인코딩
            return bytesToHex(encrypted);
        } catch (Exception e) {
            log.error("EncData 생성 오류", e);
            throw new RuntimeException("카드정보 암호화 오류", e);
        }
    }

    /**
     * SignData 생성 (위변조 검증 데이터)
     * 암호화 알고리즘: SHA-256
     * 암호결과 인코딩: Hex Encoding
     * 평문: MID + EdiDate + Moid + 상점키
     */
    private String generateSignData(String ediDate, String moid) {
        try {
            // 평문 생성
            String plainText = merchantID + ediDate + moid + merchantKey;

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

    /**
     * 고유한 주문번호 생성
     */
    private String generateMoid() {
        // 고유한 주문번호 생성 (예: BILL_yyyyMMddHHmmssSSS)
        return "BILL_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 응답 문자열 파싱
     */
    private BillkeyIssueResponseDto parseResponse(String responseBody) {
        // Key=Value 형식의 응답을 파싱
        try {
            // JSON 문자열을 BillkeyIssueResponseDto 객체로 변환
            return objectMapper.readValue(responseBody, BillkeyIssueResponseDto.class);
        } catch (Exception e) {
            log.error("응답 파싱 오류", e);
            return new BillkeyIssueResponseDto(); // 빈 객체 반환
        }
    }

    /**
     * 법인 빌키정보 업데이트트
     */
    private void updateCorporation(BillkeyIssueRequestDto request, BillkeyIssueResponseDto response) {
        // // 사업자번호로 법인 정보 조회
        // Optional<Corporation> corporationOpt =
        // corporationRepository.findByBizNo(request.getBizNo());

        // Corporation corporation = corporationOpt.orElseGet(() -> {
        // // 없으면 새로 생성
        // Corporation newCorp = new Corporation();
        // newCorp.setBizNo(request.getBizNo());
        // newCorp.setBizName(request.getBizName());
        // return newCorp;
        // });

        // // 빌키 정보 업데이트
        // corporation.setTid(response.getBid());
        // corporation.setCardCode(response.getCardCode());

        // // 카드번호 마스킹 처리 (앞 6자리, 뒤 4자리만 저장)
        // String cardNo = request.getCardNum();
        // if (cardNo.length() >= 10) {
        // String maskedCardNo = cardNo.substring(0, 6) + "******" +
        // cardNo.substring(cardNo.length() - 4);
        // corporation.setCardNum(maskedCardNo);
        // }

        // // 저장
        // corporationRepository.save(corporation);
    }
}
