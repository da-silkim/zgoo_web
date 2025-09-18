package zgoo.cpos.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    private static final Logger logger = LoggerFactory.getLogger(DebugController.class);

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/debug/message")
    @ResponseBody
    public Map<String, Object> debugMessage(@RequestParam String key) {
        Map<String, Object> result = new HashMap<>();

        try {
            Locale currentLocale = LocaleContextHolder.getLocale();
            String message = messageSource.getMessage(key, null, currentLocale);

            result.put("success", true);
            result.put("key", key);
            result.put("locale", currentLocale.getLanguage());
            result.put("message", message);

            logger.info("메시지 조회: key={}, locale={}, message={}", key, currentLocale.getLanguage(), message);

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            logger.error("메시지 조회 실패: key={}", key, e);
        }

        return result;
    }

    @GetMapping("/debug/locale")
    @ResponseBody
    public Map<String, Object> debugLocale() {
        Map<String, Object> result = new HashMap<>();

        Locale currentLocale = LocaleContextHolder.getLocale();
        result.put("locale", currentLocale.getLanguage());
        result.put("displayName", currentLocale.getDisplayLanguage(currentLocale));
        result.put("country", currentLocale.getCountry());
        result.put("variant", currentLocale.getVariant());

        logger.info("로케일 정보: {}", currentLocale);

        return result;
    }
}
