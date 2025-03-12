package zgoo.cpos.cpcontrol.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTestResponseDto {
    // 결제결과코드(성공:3001 / 그외실패)
    @JsonProperty("ResultCode")
    private String resultCode;

    // 결제결과메시지
    @JsonProperty("ResultMsg")
    private String resultMsg;

    // 거래번호, 거래를 구분하는 transactionId
    @JsonProperty("TID")
    private String tid;

    // 주문번호
    @JsonProperty("Moid")
    private String moid;

    // 금액
    @JsonProperty("Amt")
    private String amt;

    // 승인번호
    @JsonProperty("AuthCode")
    private String authCode;

    // 승인일자(YYMMDDHHMMSLS)
    @JsonProperty("AuthDate")
    private String authDate;

    // 매입카드사코드
    @JsonProperty("AcquCardCode")
    private String acquCardCode;

    // 매입카드사명
    @JsonProperty("AcquCardName")
    private String acquCardName;

    // 카드번호
    @JsonProperty("CardNo")
    private String cardNo;

    // 카드사 코드
    @JsonProperty("CardCode")
    private String cardCode;

    // 카드사 명
    @JsonProperty("CardName")
    private String cardName;

    // 할부개월
    @JsonProperty("CardQuota")
    private String cardQuota;

    // 카드타입(결제성공인 경우에만 리턴(0:신용카드, 1:체크카드))
    @JsonProperty("CardCl")
    private String cardCl;

    // 무이자여부(0:이자, 1:무이자)
    @JsonProperty("CardInterest")
    private String cardInterest;

    // 부분취소 가능여부(0:불가능, 1:가능)
    @JsonProperty("CcPartCl")
    private String ccPartcl;

    // 상점정보 전달용 여분필드(요청값을 그대로 응답함함)
    @JsonProperty("MallReserved")
    private String mallReserved;

}
