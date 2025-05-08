package zgoo.cpos.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.service.MenuService;
import zgoo.cpos.service.UsersService;

@Slf4j
@RestController
@RequestMapping("/api/nav")
public class NavController {
    private final MenuService menuService;
    private UsersService usersService;

    public NavController(MenuService menuService, UsersService usersService) {
        this.menuService = menuService;
        this.usersService = usersService;
    }

    @GetMapping("/menu")
    public List<?> getMenuBasedOnAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자인지 확인
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String userId = authentication.getName();
            Long companyId = this.usersService.findCompanyId(userId);

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                String authorityName = authority.getAuthority();
    
                if ("SU".equals(authorityName)) {
                    return this.menuService.findMenuListWithChild(authorityName);
                }

                // company 경로의 기능 실행
                return this.menuService.findCompanyMenuAuthorityBasedUserAuthority(companyId, authorityName);
            }
        }

        return Collections.emptyList();
    }

}
