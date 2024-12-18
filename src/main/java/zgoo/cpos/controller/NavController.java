package zgoo.cpos.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto;
import zgoo.cpos.dto.menu.MenuDto;
import zgoo.cpos.service.MenuService;
import zgoo.cpos.service.UsersService;

@RestController
@RequestMapping("/api/nav")
public class NavController {
    private final MenuService menuService;
    private UsersService usersService;

    public NavController(MenuService menuService, UsersService usersService) {
        this.menuService = menuService;
        this.usersService = usersService;
    }

    @GetMapping("/list")
    public List<MenuDto.MenuListDto> getMenuList() {
        return this.menuService.findMenuListWithChild();
    }

    @GetMapping("/company")
    public List<CompanyMenuAuthorityDto.CompanyMenuAuthorityListDto> getCompanyMenuList(
            @ModelAttribute("companyId") Long companyId) {
        return this.menuService.findCompanyMenuAuthorityList(companyId);
    }

    @GetMapping("/menu")
    public List<?> getMenuBasedOnAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자인지 확인
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            // 로그인한 사용자의 경우
            String userId = authentication.getName();
            Long companyId = this.usersService.findCompanyId(userId); // userId로 companyId 조회
            return this.menuService.findCompanyMenuAuthorityList(companyId); // /company 경로의 기능 실행
        } else {
            // 비로그인 사용자의 경우
            return this.menuService.findMenuListWithChild(); // /list 경로의 기능 실행
        }
    }

}
