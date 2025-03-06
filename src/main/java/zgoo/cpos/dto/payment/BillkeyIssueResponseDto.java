package zgoo.cpos.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillkeyIssueResponseDto {

    @JsonProperty("ResultCode")
    private String resultCode; // F100 성공, 그외실패

    @JsonProperty("ResultMsg")
    private String resultMsg;

    @JsonProperty("TID")
    private String tid; // 거래번호, 거래를 구분하는 transactnio ID

    @JsonProperty("BID")
    private String bid; // 빌키, 가맹점 DB에 저장하여 승인요청 시 전달

    @JsonProperty("AuthDate")
    private String authDate; // 인증일자(YYYYMMDD)

    @JsonProperty("CardCode")
    private String cardCode; // 카드사 코드(예, 04, 삼성)

    @JsonProperty("CardName")
    private String cardName; // 카드사명

    @JsonProperty("CardCl")
    private String cardCl; // 체크카드 여부(0:신용카드, 1:체크카드)

    @JsonProperty("AcquCardCode")
    private String acquCardCode; // 매입카드사 코드(예, 04)

    @JsonProperty("AcquCardName")
    private String acquCardName; // 매입카드사명(예, 삼성)
}
