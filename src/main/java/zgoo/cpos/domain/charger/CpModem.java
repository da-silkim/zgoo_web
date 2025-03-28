package zgoo.cpos.domain.charger;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CP_MODEM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class CpModem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cpmodem_id")
    private Long id;

    private String modemNo; // 모뎀전화번호
    private String serialNo; // 모뎀시리얼번호
    private LocalDate contractStart; // 계약시작일
    private LocalDate contractEnd; // 계약만료일
    private String pricePlan; // 요금제
    private String dataCapacity; // 데이터용량
    private String telCorp; // 통신사
    private String modelName; // 모델명
    private String contractStatus; // 계약상태(공통코드, 미계약:MODEMNC, 계약중:MODEMUC, 계약만료:MODEMCF)

    public void updateCpModemInfo(String modemNo, String serialNo, LocalDate contractStart, LocalDate contractEnd,
            String pricePlan, String dataCapacity, String telCorp, String modelName, String contractStatus) {
        this.modemNo = modemNo;
        this.serialNo = serialNo;
        this.contractStart = contractStart;
        this.contractEnd = contractEnd;
        this.pricePlan = pricePlan;
        this.dataCapacity = dataCapacity;
        this.telCorp = telCorp;
        this.modelName = modelName;
        this.contractStatus = contractStatus;
    }
}
