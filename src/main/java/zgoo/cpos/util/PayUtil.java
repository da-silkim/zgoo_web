package zgoo.cpos.util;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("payUtil")
public class PayUtil {
    private static final Map<String, String> CARD_COMPANY_MAP = Map.ofEntries(
            Map.entry("01", "비씨"),
            Map.entry("02", "KB국민"),
            Map.entry("03", "하나(외환)"),
            Map.entry("04", "삼성"),
            Map.entry("06", "신한"),
            Map.entry("07", "현대"),
            Map.entry("08", "롯데"),
            Map.entry("09", "한미"),
            Map.entry("10", "신세계한미"),
            Map.entry("11", "씨티"),
            Map.entry("12", "NH채움"),
            Map.entry("13", "수협"),
            Map.entry("14", "신협"),
            Map.entry("15", "우리BC"),
            Map.entry("16", "하나"),
            Map.entry("17", "우리"),
            Map.entry("21", "광주"),
            Map.entry("22", "전북"),
            Map.entry("23", "제주"),
            Map.entry("24", "산은캐피탈"),
            Map.entry("25", "해외비자"),
            Map.entry("26", "해외마스터"),
            Map.entry("27", "해외다이너스"),
            Map.entry("28", "해외AMX"),
            Map.entry("29", "해외JCB"),
            Map.entry("31", "SK-OK CASH-BAG"),
            Map.entry("32", "우체국"),
            Map.entry("33", "저축은행"),
            Map.entry("34", "은련"),
            Map.entry("35", "새마을금고"),
            Map.entry("36", "KDB산업"),
            Map.entry("37", "카카오뱅크"),
            Map.entry("38", "케이뱅크"),
            Map.entry("39", "PAYCO 포인트"),
            Map.entry("40", "카카오머니"),
            Map.entry("41", "SSG머니"),
            Map.entry("42", "네이버포인트"),
            Map.entry("44", "토스뱅크"));

    public String getCardCompanyName(String code) {
        return CARD_COMPANY_MAP.getOrDefault(code, code);
    }
}
