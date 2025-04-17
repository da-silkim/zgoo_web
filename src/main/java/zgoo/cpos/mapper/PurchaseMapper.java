package zgoo.cpos.mapper;

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
                .totalAmount(dto.getTotalAmount())
                .delYn("N")
                .regUserId(regUserId)
                .regDt(LocalDateTime.now())
                .build();
        return purchase;
    }
}
