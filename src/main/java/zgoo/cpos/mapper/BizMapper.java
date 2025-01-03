package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.biz.BizInfo;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;

public class BizMapper {

    /* 
     * dto >> entity
     */
    public static BizInfo toEntity(BizInfoRegDto dto, Company company) {
        BizInfo biz = BizInfo.builder()
                .company(company)
                .bizNo(dto.getBizNo())
                .bizName(dto.getBizName())
                .tid(dto.getTid())
                .cardNum(dto.getCardNum())
                .fnCode(dto.getFnCode())
                .regDt(LocalDateTime.now())
                .build();
        return biz;
    }
}
