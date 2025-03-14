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
public class CancelTestRsponseDto {
    // 결제결과코드(성공:2001 / 그외실패)
    @JsonProperty("ResultCode")
    private String resultCode;

    // 결제결과메시지
    @JsonProperty("ResultMsg")
    private String resultMsg;

    // 에러코드
    @JsonProperty("ErrorCD")
    private String errorCd;

    // 에러메시지
    @JsonProperty("ErrorMsg")
    private String errorMsg;

    // 취소금액
    @JsonProperty("CancelAmt")
    private String cancelAmt;

    // mid
    @JsonProperty("MID")
    private String mid;

    // MOID
    @JsonProperty("Moid")
    private String moid;

    // Signature : 위변조검증 데이터 (가맹점 수준에서 비교로직 구현 필수 > TID + MID + CancelAmt +
    // MerchantKey)
    @JsonProperty("Signature")
    private String signature;

    // PayMethod : 결제수단코드(신용카드:CARD)
    @JsonProperty("PayMethod")
    private String payMethod;

    // TID
    @JsonProperty("TID")
    private String tid;

    // 취소일자 YYYYMMDD
    @JsonProperty("CancelDate")
    private String cancelDate;

    // 취소시간 HHmmss
    @JsonProperty("CancelTime")
    private String cancelTime;

    // 취소번호
    @JsonProperty("CancelNum")
    private String cancelNum;

    // RemainAmt : 취소 후 잔액
    @JsonProperty("RemainAmt")
    private String remainAmt;

    // MallReserved(예비필드)
    @JsonProperty("MallReserved")
    private String mallReserved;

}
