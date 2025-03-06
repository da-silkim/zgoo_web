package zgoo.cpos.dto.biz;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BizInfoDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class BizInfoRegDto {
        private Long bizId;
        private String bizNo;
        private String bizName;
        private String bid;
        private String cardNum;
        private String cardCode;
        private String cardName;
        private String authDate;
        private LocalDateTime regDt;

        private String cardNum1;
        private String cardNum2;
        private String cardNum3;
        private String cardNum4;

        private String cardYn;
        private String bidYn;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class BizInfoListDto extends BizInfoRegDto {
        private String cardName; // 카드사 코드명
    }
}
