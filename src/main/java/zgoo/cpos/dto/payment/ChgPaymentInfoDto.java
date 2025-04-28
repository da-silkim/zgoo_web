package zgoo.cpos.dto.payment;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 충전 결제 정보 DTO 클래스
 * 관제 결제 데이터와 PG 거래 데이터를 함께 표시하기 위한 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChgPaymentInfoDto {
    // 관제 결제 데이터
    private String paymentTime; // 결제시간
    private String companyName; // 사업자
    private String stationName; // 충전소명
    private String chargerId; // 충전기ID
    private BigDecimal chgAmount; // 충전량
    private Integer chgPrice; // 승인금액(최초결제금액)
    private Integer cancelCost; // 부분취소
    private Integer realCost; // 최종결제금액

    // PG 거래 데이터
    private String pgAppNum; // 승인번호
    private String pgAppTime; // 승인일시
    private String pgPayType; // 지불수단
    private BigDecimal pgAppAmount; // 거래금액
    private BigDecimal pgCancelAmount; // 거래부분취소
    private BigDecimal pgPaymentAmount; // 결제금액
    private String cardNumber; // 카드번호

    // 추가 필드 (필요시 사용)
    private String tid; // 거래 ID
    private String otid; // 원거래 ID
    private String stateCd; // 상태코드
}
