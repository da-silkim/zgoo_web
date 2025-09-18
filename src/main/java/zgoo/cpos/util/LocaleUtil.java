package zgoo.cpos.util;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 다국어 지원을 위한 로케일 유틸리티 클래스
 */
public class LocaleUtil {

    /**
     * 현재 로케일에 따라 적절한 언어 컬럼명을 반환
     * 
     * @param baseColumn 기본 컬럼명 (예: "name", "menu_name")
     * @return 언어별 컬럼명 (예: "name", "name_en", "name")
     */
    public static String getLocalizedColumn(String baseColumn) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String language = currentLocale.getLanguage();

        switch (language) {
            case "ko":
                return baseColumn; // 한국어는 기존 컬럼 사용
            case "en":
                return baseColumn + "_en"; // 영어는 _en 컬럼 사용
            case "ja":
                return baseColumn + "_ja"; // 일본어는 _ja 컬럼 사용 (향후 확장용)
            default:
                return baseColumn; // 기본값은 원본 컬럼
        }
    }

    /**
     * 현재 로케일의 언어 코드 반환
     * 
     * @return 언어 코드 (ko, en, ja)
     */
    public static String getCurrentLanguage() {
        return LocaleContextHolder.getLocale().getLanguage();
    }

    /**
     * 현재 로케일이 한국어인지 확인
     * 
     * @return 한국어 여부
     */
    public static boolean isKorean() {
        return "ko".equals(getCurrentLanguage());
    }

    /**
     * 현재 로케일이 영어인지 확인
     * 
     * @return 영어 여부
     */
    public static boolean isEnglish() {
        return "en".equals(getCurrentLanguage());
    }

    /**
     * 현재 로케일이 일본어인지 확인
     * 
     * @return 일본어 여부
     */
    public static boolean isJapanese() {
        return "ja".equals(getCurrentLanguage());
    }
}
