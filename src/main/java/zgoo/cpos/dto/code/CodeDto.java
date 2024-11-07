package zgoo.cpos.dto.code;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CodeDto {

    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class GrpCodeBaseDto {
        private String grpCode;
        private String grpcdName;

    }

    /*
     * grpcode 등록 dto
     */
    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class GrpCodeDto extends GrpCodeBaseDto {
        private String regUserId;
        private LocalDateTime regDt;
        private String modUserId;
        private LocalDateTime modDt;
    }

    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class CommCdBaseDto {
        private String grpCode;
        private String commonCode;
        private String commonCodeName;
    }

    /*
     * common code 등록 dto
     */
    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CommCodeDto extends CommCdBaseDto {
        private String regUserId;
        private LocalDateTime regDt;
        private String reserved;
        private String refCode1;
        private String refCode2;
        private String refCode3;
        private String modUserId;
        private LocalDateTime modDt;
    }
}
