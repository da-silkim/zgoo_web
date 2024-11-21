package zgoo.cpos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String home(HttpServletRequest request, @RequestParam(value = "error", required = false) String error, Model model) {
        log.info("home controller");

        Cookie[] cookies = request.getCookies();
        String rememberedUserId = null;

        // error 파라미터가 없으면 userId, password 값을 세션에서 삭제
        if (error == null) {
            request.getSession().removeAttribute("userId");
            request.getSession().removeAttribute("password");
        }

        // rememberedUserId라는 이름을 가진 쿠키가 있으면 value 가져오기
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberedUserId".equals(cookie.getName())) {
                    rememberedUserId = cookie.getValue();
                }
            }
        }

        model.addAttribute("rememberedUserId", rememberedUserId);
        return "pages/login/login_page";
    }
}
