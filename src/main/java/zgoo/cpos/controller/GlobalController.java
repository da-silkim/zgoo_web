package zgoo.cpos.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import zgoo.cpos.dto.users.UsersDto.UsersRegDto;
import zgoo.cpos.service.CodeService;
import zgoo.cpos.service.MenuService;
import zgoo.cpos.service.UsersService;

@ControllerAdvice
public class GlobalController {
    private MenuService menuService;
    private UsersService usersService;
    private CodeService codeService;

    public GlobalController(MenuService menuService, UsersService usersService, CodeService codeService) {
        this.menuService = menuService;
        this.usersService = usersService;
        this.codeService = codeService;
    }

    @ModelAttribute("loginUserId")
    public void loginUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getPrincipal())) {
            String loginUserId = authentication.getName();
            System.out.println("loginUserId: " + loginUserId);
            model.addAttribute("loginUserId", loginUserId);

            UsersRegDto user = this.usersService.findUserOne(loginUserId);
            String loginUserName = user.getName();
            model.addAttribute("loginUserName", loginUserName);

            String authority = user.getAuthority();
            model.addAttribute("loginUserAuthority", this.codeService.findCommonCodeName(authority));

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
