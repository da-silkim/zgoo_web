package zgoo.cpos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test-locale")
    public String testLocale() {
        return "test-locale";
    }

    @GetMapping("/debug-i18n")
    public String debugI18n() {
        return "debug-i18n";
    }

    @GetMapping("/test-locale-session")
    public String testLocaleSession() {
        return "test-locale-session";
    }
}
