package zgoo.cpos.domain.calc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseRegDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class PurchaseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchaseinfo_id")
    private Long id;

    @Column(name = "approval_no")
    private String approvalNo;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "account_code")
    private String accountCode;

    @Column(name = "biz_num")
    private String bizNum;

    @Column(name = "biz_name")
    private String bizName;

    @Column(name = "item")
    private String item;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "unit_price")
    private Integer unitPrice;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "supply_price")
    private Integer supplyPrice;

    @Column(name = "vat")
    private Integer vat;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "del_yn")
    private String delYn;

    @Column(name = "reg_user_id")
    private String regUserId;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "mod_user_id")
    private String modUserId;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    @JoinColumn(name = "station_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CsInfo station;

    public void updatePurchase(PurchaseRegDto dto, CsInfo station, String modUserId) {
        this.station = station;
        this.approvalNo = dto.getApprovalNo();
        this.purchaseDate = dto.getPurchaseDate();
        this.accountCode = dto.getAccountCode();
        this.bizNum = dto.getBizNum();
        this.bizName = dto.getBizName();
        this.item = dto.getItem();
        this.paymentMethod = dto.getPaymentMethod();
        this.unitPrice = dto.getUnitPrice();
        this.amount = dto.getAmount();
        this.supplyPrice = dto.getSupplyPrice();
        this.vat = dto.getVat();
        this.totalAmount = dto.getTotalAmount();
        this.modUserId = modUserId;
        this.modDt = LocalDateTime.now();
    }
}
