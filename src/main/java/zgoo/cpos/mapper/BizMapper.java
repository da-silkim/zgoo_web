package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.biz.BizInfo;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;

public class BizMapper {

    /*
     * dto >> entity
     */
    public static BizInfo toEntity(BizInfoRegDto dto) {
        BizInfo biz = BizInfo.builder()
                .bizNo(dto.getBizNo())
                .bizName(dto.getBizName())
                .bid(dto.getBid())
                .cardNum(dto.getCardNum())
                .cardCode(dto.getCardCode())
                .cardName(dto.getCardName())
                .authDate(dto.getAuthDate())
                .regDt(LocalDateTime.now())
                .build();
        return biz;
    }
}
