package zgoo.cpos.dto.menu;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MenuAuthorityDto {

    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class MenuAuthorityBaseDto {
        private String topMenu;
        private String midMenu;
        private String lowMenu;
        private String modYn;
        private String readYn;
        private String excelYn;
    }

    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class CompanyAuthorityListDto {
        private Long companyId;
        private String companyName;
        private String authority;
        private String authorityName;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class MenuAuthorityListDto extends MenuAuthorityBaseDto {
        private String modUserId;
        private String authority;
        private LocalDateTime regAt;
        private LocalDateTime modAt;
    }
}
