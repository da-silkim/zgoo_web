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
        private Long companyId;
        private String bizNo;
        private String bizName;
        private String tid;
        private String cardNum;
        private String fnCode;
        private LocalDateTime regDt;

        private String cardNum1;
        private String cardNum2;
        private String cardNum3;
        private String cardNum4;

        private String cardYn;
        private String tidYn;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class BizInfoListDto extends BizInfoRegDto {
        private String fnCodeName;   // 카드사 코드명
        private String companyName;  // 사업자명
    }
}
