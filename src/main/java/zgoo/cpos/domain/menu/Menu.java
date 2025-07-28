package zgoo.cpos.domain.menu;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.menu.MenuDto;

@Entity
@Table(name = "MENU", indexes = {
        @Index(name = "idx_menu_parent_code", columnList = "parent_code"),
        @Index(name = "idx_menu_use_yn", columnList = "use_yn")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Menu {

    @Id
    @Column(name = "menu_code")
    private String menuCode;

    @Column(name = "parent_code")
    private String parentCode;

    @Column(name = "menu_url")
    private String menuUrl;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_lv")
    private String menuLv;

    @Column(name = "use_yn")
    private String useYn;

    @Column(name = "icon_class")
    private String iconClass;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    public void updateMenuInfo(MenuDto.MenuRegDto menu) {
        this.modDt = LocalDateTime.now();
        this.menuCode = menu.getMenuCode();
        this.parentCode = menu.getParentCode();
        this.menuUrl = menu.getMenuUrl();
        this.menuName = menu.getMenuName();
        this.menuLv = menu.getMenuLv();
        this.useYn = menu.getUseYn();
        this.iconClass = menu.getIconClass();
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }
}
