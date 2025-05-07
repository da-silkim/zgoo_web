package zgoo.cpos.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import zgoo.cpos.domain.calc.PurchaseInfo;
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseRegDto;

public class PurchaseMapper {

    /* 
     * dto >> entity
     */
    public static PurchaseInfo toEntity(PurchaseRegDto dto, CsInfo station, String regUserId) {
        PurchaseInfo purchase = PurchaseInfo.builder()
                .station(station)
                .approvalNo(dto.getApprovalNo())
                .purchaseDate(dto.getPurchaseDate())
                .accountCode(dto.getAccountCode())
                .bizNum(dto.getBizNum())
                .bizName(dto.getBizName())
                .item(dto.getItem())
                .paymentMethod(dto.getPaymentMethod())
                .unitPrice(dto.getUnitPrice())
                .amount(dto.getAmount())
                .supplyPrice(dto.getSupplyPrice())
                .vat(dto.getVat())
                .charge(dto.getCharge())
                .surcharge(dto.getSurcharge())
                .cutoffAmount(dto.getCutoffAmount())
                .unpaidAmount(dto.getUnpaidAmount())
                .totalAmount(dto.getTotalAmount())
                .power(dto.getPower())
                .delYn("N")
                .regUserId(regUserId)
                .regDt(LocalDateTime.now())
                .build();
        return purchase;
    }

    /* 
     * dto >> entity(electricity)
     */
    public static PurchaseInfo toEntityElec(PurchaseRegDto dto, CsInfo station, String regUserId) {
        PurchaseInfo purchase = PurchaseInfo.builder()
                .station(station)
                .approvalNo(null)
                .purchaseDate(LocalDate.now())
                .accountCode("ELCFEE")
                .bizNum("120-82-00052")
                .bizName("한국전력공사")
                .item(null)
                .paymentMethod(dto.getPaymentMethod())
                .unitPrice(dto.getSupplyPrice())
                .amount(1)
                .supplyPrice(dto.getSupplyPrice())
                .vat(dto.getVat())
                .charge(dto.getCharge())
                .surcharge(dto.getSurcharge())
                .cutoffAmount(dto.getCutoffAmount())
                .unpaidAmount(dto.getUnpaidAmount())
                .totalAmount(dto.getTotalAmount())
                .power(dto.getPower())
                .delYn("N")
                .regUserId(regUserId)
                .regDt(LocalDateTime.now())
                .build();
        return purchase;
    }
}
