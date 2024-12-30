package zgoo.cpos.dto.code;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ChgErrorCodeDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class ChgErrorCodeBaseDto {
        private Long errcdId;
        private String errCode;
        private String menufCode;
        private String errName;
        private LocalDateTime regDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class ChgErrorCodeRegDto extends ChgErrorCodeBaseDto {
        private LocalDateTime modDt;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class ChgErrorCodeListDto extends ChgErrorCodeBaseDto {
        private String menufCodeName;   // 제조사 코드명
    }
}
