package zgoo.cpos.domain.menu;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CompanyMenuAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_menu_authority")
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "menu_code")
    private String menuCode;

    @Column(name = "use_yn")
    private String useYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_code", insertable = false, updatable = false)
    private Menu menu;

    public void updateCompanyMenuInfo(CompanyMenuAuthorityDto.CompanyMenuAuthorityBaseDto dto) {
        this.useYn = dto.getUseYn();
    }

    public void updateCompanyMenuInfoWithMenu(String useYn) {
        this.useYn = useYn;
    }
}
