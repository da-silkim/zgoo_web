package zgoo.cpos.dto.member;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class VocDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class VocBaseDto {
        private Long memberId;
        private Long userId;
        private Long vocId;
        private String title;
        private String content;
        private String replyStat;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class VocListDto extends VocBaseDto {
        private String typeName;
        private String name;
        private String phoneNo;
        private String replyStatName;
        private LocalDateTime regDt;
        private LocalDateTime replyDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class VocRegDto extends VocBaseDto {
        private String type;
        private String delYn;
        private String channel;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class VocAnswerDto extends VocListDto {
        private String channelName;
        private String replyContent;
    }
}
