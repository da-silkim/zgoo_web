package zgoo.cpos.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SimpleTestController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/simple-test")
    public String simpleTest(Model model) {
        Locale currentLocale = LocaleContextHolder.getLocale();

        try {
            String message = messageSource.getMessage("dashboard.charger.status", null, currentLocale);
            model.addAttribute("testMessage", message);
            model.addAttribute("locale", currentLocale.getLanguage());
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("testMessage", "Error: " + e.getMessage());
            model.addAttribute("success", false);
        }

        return "simple-test";
    }

    @GetMapping("/api/test-message")
    @ResponseBody
    public Map<String, Object> testMessage() {
        Map<String, Object> result = new HashMap<>();
        Locale currentLocale = LocaleContextHolder.getLocale();

        try {
            String message = messageSource.getMessage("dashboard.charger.status", null, currentLocale);
            result.put("success", true);
            result.put("message", message);
            result.put("locale", currentLocale.getLanguage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("locale", currentLocale.getLanguage());
        }

        return result;
    }
}
