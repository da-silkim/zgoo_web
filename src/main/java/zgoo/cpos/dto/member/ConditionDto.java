package zgoo.cpos.dto.member;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ConditionDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class ConditionCodeBaseDto {
        private String conditionCode;
        private String conditionName;
        private String section;
        private LocalDateTime regDt;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class ConditionVersionHistBaseDto extends ConditionCodeBaseDto {
        private Long conditionVersionHistId;
        private String filePath;
        private MultipartFile file;
        private String originalName;
        private String storedName;
        private String version;
        private String memo;
        private String applyYn;
        private LocalDateTime histRegDt;
        private LocalDateTime applyDt;
        private String applyDtString;
    }
}
