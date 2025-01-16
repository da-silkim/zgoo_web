package zgoo.cpos.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MenuServiceTest {

    @Autowired
    MenuService menuService;

    @Test
    @DisplayName("메뉴 - 등록")
    public void createMenu() throws Exception {

        // MenuRegDto dto = MenuRegDto.builder()
        //         .menuCode("X0001")
        //         .menuName("테스트1")
        //         .parentCode(null)
        //         .menuLv("0")
        //         .menuUrl("/test1")
        //         .useYn("N")
        //         .iconClass(null)
        //         .regDt(LocalDateTime.now())
        //         .modDt(null)
        //         .build();

        // this.menuService.saveMenu(dto);

        // List<MenuDto.MenuListDto> menuList = this.menuService.findMenuList();
        // for (MenuListDto menuListDto : menuList) {
        //     System.out.println("=== full search after saving menu info : " + menuListDto.toString());
        // }
    }

    @Test
    @DisplayName("메뉴 - 수정")
    public void updateMenu() throws Exception {

        // // before update
        // MenuRegDto before = this.menuService.findMenuOne("X0001");
        // System.out.println("=== before update: " + before.toString());

        // // after update
        // MenuRegDto dto = MenuRegDto.builder()
        //     .menuCode("X0001")
        //     .menuName("테스트1")
        //     .parentCode(null)
        //     .menuLv("0")
        //     .menuUrl("test1")
        //     .useYn("Y")
        //     .iconClass(null)
        //     .modDt(LocalDateTime.now())
        //     .build();

        // this.menuService.updateMenu(dto);
        // MenuRegDto after = this.menuService.findMenuOne("X0001");
        // System.out.println("=== after update: " + after.toString());
    }

    @Test
    @Rollback(false)
    @DisplayName("메뉴 - 삭제")
    public void deleteMenu() throws Exception {
        // String menuCode = "X0001";
        // this.menuService.deleteMenu(menuCode);
    }
}
