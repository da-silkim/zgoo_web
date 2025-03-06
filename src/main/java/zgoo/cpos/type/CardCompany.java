package zgoo.cpos.type;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardCompany {
    BC("01", "비씨"),
    KB("02", "KB국민"),
    HANAFORIGN("03", "하나(외환)"),
    SAMSUNG("04", "삼성"),
    SHINHAN("06", "신한"),
    HYUNDAI("07", "현대"),
    LOTTE("08", "롯데"),
    HANMIN("09", "한미"),
    SINSAEHANMI("10", "신세계한미"),
    CITY("11", "씨티"),
    NHCHAEUM("12", "NH채움"),
    SUHYUP("13", "수협"),
    SHINHYUP("14", "신협"),
    WOORIBC("15", "우리BC"),
    HANA("16", "하나"),
    WOORI("17", "우리"),
    GWANGJU("21", "광주"),
    JEONBUK("22", "전북"),
    JEJU("23", "제주"),
    SANEUN("24", "산은캐피탈"),
    VISA("25", "해외비자"),
    MASTER("26", "해외마스터"),
    DINERS("27", "해외다이너스"),
    AMEX("28", "해외AMX"),
    JCB("29", "해외JCB"),
    SKOKCASHBAG("31", "SK오케이캐쉬백"),
    POSTOFFICE("32", "우체국"),
    SAVINGS("33", "저축은행"),
    ENRYUN("34", "은련"),
    SAMAEL("35", "새마을금고"),
    KDB("36", "KDB산업"),
    KAKAO("37", "카카오뱅크"),
    KBANK("38", "케이뱅크"),
    PAYCO("39", "PAYCO포인트"),
    KAKAOMONEY("40", "카카오머니"),
    SSGMONEY("41", "SSG머니"),
    NAVER("42", "네이버포인트"),
    TOSS("44", "토스뱅크");

    private final String code;
    private final String name;

    private static final Map<String, CardCompany> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(CardCompany::getCode, Function.identity()));

    /**
     * 코드로 카드사 Enum 조회
     * 
     * @param code 카드사 코드
     * @return 카드사 Enum
     */
    public static CardCompany fromCode(String code) {
        return Optional.ofNullable(CODE_MAP.get(code))
                .orElse(null);
    }

    /**
     * 코드로 카드사명 조회
     * 
     * @param code 카드사 코드
     * @return 카드사명
     */
    public static String getNameByCode(String code) {
        return fromCode(code).getName();
    }

    /**
     * 카드사명으로 코드 조회
     * 
     * @param name 카드사명
     * @return 카드사 코드
     */
    public static String getCodeByName(String name) {
        return Arrays.stream(values())
                .filter(company -> company.getName().equals(name))
                .findFirst()
                .map(CardCompany::getCode)
                .orElse("");
    }
}
