package zgoo.cpos.dto.menu;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CompanyMenuAuthorityDto {

    private List<CompanyMenuAuthorityBaseDto> findCompanyMenuAuthorityList;

    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class CompanyMenuAuthorityBaseDto {
        private long companyId;
        private String menuCode;
        private String useYn;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CompanyMenuAuthorityListDto extends CompanyMenuAuthorityBaseDto {
        private String companyName;
        private String menuLv;
        private String menuUrl;
        private String menuName;
        private String iconClass;
        private String parentCode;
        private String parentCodeName;
        private long childCnt;
        private String readYn;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CompanyMenuRegDto extends CompanyMenuAuthorityBaseDto {
        private String companyName;
    }
}
