package zgoo.cpos.dto.users;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class FaqDto {

    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class BaseFaqDto {
        private Long id;
        private String title;
        private String userId;
        private String delYn;
        private String section;
        private LocalDateTime regDt;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class FaqListDto extends BaseFaqDto {
        public String userName;
        public String sectionName;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class FaqRegDto extends BaseFaqDto {
        private String content;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class FaqDetailDto extends BaseFaqDto {
        private String content;
        public String userName;
        public String sectionName;
    }
}
