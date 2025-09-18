package zgoo.cpos.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LocaleController {

    private static final Logger logger = LoggerFactory.getLogger(LocaleController.class);

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/change-locale")
    @ResponseBody
    public Map<String, Object> changeLocale(
            @RequestParam String lang,
            HttpServletRequest request,
            HttpServletResponse response) {

        Map<String, Object> result = new HashMap<>();
        logger.info("언어 변경 요청: {}", lang);

        try {
            Locale locale = new Locale(lang);
            localeResolver.setLocale(request, response, locale);

            // 현재 로케일 확인
            Locale currentLocale = localeResolver.resolveLocale(request);
            logger.info("언어 변경 완료: {} -> {}", lang, currentLocale.getLanguage());

            result.put("success", true);
            result.put("message", "언어가 변경되었습니다.");
            result.put("locale", locale.getLanguage());

        } catch (Exception e) {
            logger.error("언어 변경 실패: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "언어 변경에 실패했습니다.");
        }

        return result;
    }

    @GetMapping("/get-current-locale")
    @ResponseBody
    public Map<String, Object> getCurrentLocale() {
        Map<String, Object> result = new HashMap<>();
        Locale currentLocale = LocaleContextHolder.getLocale();

        logger.info("현재 로케일 조회: {}", currentLocale.getLanguage());

        result.put("locale", currentLocale.getLanguage());
        result.put("displayName", currentLocale.getDisplayLanguage(currentLocale));

        return result;
    }
}
