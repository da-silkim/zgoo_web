package zgoo.cpos.util;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import zgoo.cpos.service.LoginHistService;

@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final LoginHistService loginHistService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        HttpSession session = request.getSession();
        String loginUserId = authentication.getName();
        session.setAttribute("userId", loginUserId);

        this.loginHistService.recordLogin(loginUserId);

        System.out.println("로그인 성공: 사용자 ID가 세션에 저장되었습니다. userId: " + loginUserId);
        
        String remember = request.getParameter("rememberCheckbox");
        if ("on".equals(remember)) {
            String userId = request.getParameter("userId");
            Cookie cookie = new Cookie("rememberedUserId", userId);
            cookie.setMaxAge(365 * 24 * 60 * 60);
            cookie.setPath("/");
            cookie.setHttpOnly(false);
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("rememberedUserId", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        response.sendRedirect("/dashboard");
    }
}
