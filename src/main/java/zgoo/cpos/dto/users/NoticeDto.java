package zgoo.cpos.dto.users;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class NoticeDto {

    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class BaseNoticeDto {
        private Long idx;
        private String title;
        private String type;
        private String userId;
        private Long views;
        private String delYn;
        private LocalDateTime regDt;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class NoticeListDto extends BaseNoticeDto {
        private String userName;
        private Long companyId;
        private String companyName;
        private String typeName;
        private boolean isNew;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class NoticeRegDto extends BaseNoticeDto {
        private String content;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class NoticeDetailDto extends BaseNoticeDto {
        private String content;
        private String typeName;
        private String userName;
    }
}
