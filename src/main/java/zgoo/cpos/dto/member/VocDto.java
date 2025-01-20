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
        private Long vocId;
        private Long userId;
        private Long memberId;
        private String name;
        private String type;
        private String title;
        private String phoneNo;
        private String content;
        private String replyStat;
        private LocalDateTime regDt;
        private LocalDateTime replyDt;

    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class VocListDto extends VocBaseDto {
        private String typeName;
        private String replyStatName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class VocRegDto extends VocBaseDto {
        private String delYn;
        private String channel;
        private String channelName;
        private String replyContent;
        private String regUserId;
        private String replyUserId;
    }
}
