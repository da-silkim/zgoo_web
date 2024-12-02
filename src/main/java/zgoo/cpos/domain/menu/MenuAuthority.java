package zgoo.cpos.domain.menu;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
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
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.type.MenuAuthorityKey;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class MenuAuthority {

    // @EmbeddedId
    // private MenuAuthorityKey id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menuauthority_id")
    private Long id;

    @Column(name = "authority")
    private String authority;

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

    @Column(name = "mod_user_id")
    private String modUserId;

    @Column(name = "reg_at")
    private LocalDateTime regAt;

    @Column(name = "mod_at")
    private LocalDateTime modAt;

    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}
