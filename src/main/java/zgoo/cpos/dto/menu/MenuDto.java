package zgoo.cpos.dto.menu;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MenuDto {

    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class MenuBaseDto {
        private String menuCode;
        private String parentCode;
        private String menuUrl;
        private String menuName;
        private String menuLv;
        private String useYn;
        private String iconClass;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class MenuRegDto extends MenuBaseDto {
        private LocalDateTime regDt;
        private LocalDateTime modDt;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class MenuListDto extends MenuBaseDto {
        private String menuLvName;
        private long childCnt;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class MenuAuthorityListDto extends MenuListDto {
        private String parentCodeName;
    }
}