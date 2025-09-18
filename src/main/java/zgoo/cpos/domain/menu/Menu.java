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
import zgoo.cpos.util.LocaleUtil;

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

    @Column(name = "menu_name_en")
    private String menuNameEn;

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
        this.menuNameEn = menu.getMenuNameEn();
        this.menuLv = menu.getMenuLv();
        this.useYn = menu.getUseYn();
        this.iconClass = menu.getIconClass();
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    /**
     * 현재 로케일에 따라 적절한 메뉴 이름을 반환
     * 
     * @return 로케일별 메뉴 이름
     */
    public String getLocalizedMenuName() {
        // 한국어는 기존 menuName 사용, 영어는 menuNameEn 사용
        if (LocaleUtil.isEnglish() && this.menuNameEn != null && !this.menuNameEn.trim().isEmpty()) {
            return this.menuNameEn;
        }
        return this.menuName; // 기본값은 한국어 (기존 컬럼)
    }
}
