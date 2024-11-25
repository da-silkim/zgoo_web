package zgoo.cpos.domain.authority;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.type.MenuAuthorityKey;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class MenuAuthority {

    @EmbeddedId
    private MenuAuthorityKey id;

    @Column(name = "top_menu")
    private String topMenu;

    @Column(name = "mid_menu")
    private String midMenu;

    @Column(name = "low_menu")
    private String lowMenu;

    @Column(name = "mod_yn")
    private String modYn;

    @Column(name = "read_yn")
    private String readYn;

    @Column(name = "excel_yn")
    private String excelYn;

    @Column(name = "reg_at")
    private LocalDateTime regDt;

    @Column(name = "mod_at")
    private LocalDateTime modAt;

    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}
