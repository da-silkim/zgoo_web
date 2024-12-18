package zgoo.cpos.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.menu.MenuDto;
import zgoo.cpos.service.MenuService;
import zgoo.cpos.service.UsersService;

@ControllerAdvice
public class GlobalController {
    private MenuService menuService;
    private UsersService usersService;

    public GlobalController(MenuService menuService, UsersService usersService) {
        this.menuService = menuService;
        this.usersService = usersService;
    }

    @ModelAttribute("loginUserId")
    public void loginUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getPrincipal())) {
            String loginUserId = authentication.getName();
            System.out.println("loginUserId: " + loginUserId);
            model.addAttribute("loginUserId", loginUserId);
            // List<MenuDto.MenuListDto> menuList = menuService.findMenuListWithChild();
            // model.addAttribute("navList", menuList);

            Long companyId = this.usersService.findCompanyId(loginUserId);
            model.addAttribute("companyId", companyId);
        }
    }

    @ModelAttribute
    public String loginUserIdInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return null;
    }
}
