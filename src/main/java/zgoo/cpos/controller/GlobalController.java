package zgoo.cpos.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import zgoo.cpos.dto.menu.MenuDto;
import zgoo.cpos.service.MenuService;

@ControllerAdvice
public class GlobalController {
    private MenuService menuService;

    @ModelAttribute
    public void loginUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getPrincipal())) {
            String loginUserId = authentication.getName();
            System.out.println("loginUserId: " + loginUserId);
            model.addAttribute("loginUserId", loginUserId);
            List<MenuDto.MenuListDto> menuList = menuService.findMenuListWithChild();
            model.addAttribute("navList", menuList);
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
