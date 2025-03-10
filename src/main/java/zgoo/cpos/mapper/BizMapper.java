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
                .cardExpireMonth(dto.getCardExpireMonth())
                .cardExpireYear(dto.getCardExpireYear())
                .termsEtf(dto.isTermsEtf())
                .termsRb(dto.isTermsRb())
                .termsPrivacy(dto.isTermsPrivacy())
                .termsPrivacy3rd(dto.isTermsPrivacy3rd())
                .authDate(dto.getAuthDate())
                .regDt(LocalDateTime.now())
                .build();
        return biz;
    }
}
