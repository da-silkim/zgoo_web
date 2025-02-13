package zgoo.cpos.dto.member;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MemberDto {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class MemberBaseDto {
        private Long companyId;
        private Long memberId;
        private Long bizId;
        private String bizType;
        private String memLoginId;
        private String password;
        private String name;
        private String phoneNo;
        private String idTag;
        private String email;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class MemberListDto extends MemberBaseDto {
        private String companyName;
        private LocalDateTime joinedDt;
        private String bizTypeName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class MemberRegDto extends MemberBaseDto {
        private String birth;
        private String zipCode;
        private String address;
        private String addressDetail;
        private String userState;
        private String creditcardStat;

        // 결제카드 정보
        private List<MemberCreditCardDto> card;

        // 차량 정보
        private List<MemberCarDto> car;
    
        // 약관 정보
        private List<MemberConditionDto> condition;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberCreditCardDto {
        private String tid;
        private String cardNum;
        private String fnCode;
        private String fnCodeName;
        private String representativeCard;
        private LocalDateTime cardRegDt;
        private String tidCheck;
        private String representativeCardCheck;

        private String carNum1;
        private String carNum2;
        private String carNum3;
        private String carNum4;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberCarDto {
        private String carType;
        private String carTypeName;
        private String carNum;
        private String model;
        private LocalDateTime carRegDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberConditionDto {
        private String conditionCode;
        private String agreeYn;
        private String agreeYnCheck;
        private String section;
        private String conditionCodeName;
        private LocalDateTime agreeDt;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class MemberDetailDto extends MemberRegDto {
        private String companyName;
        private String bizName;
        private String bizNo;
        private String bizTypeName;
        private String cardRegYn;
        private String tidRegYn;
        private String creditcardStatName;  // 회원상태 코드명
        private String userStateName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberPasswordDto {
        private String existPassword;
        private String newPassword;
        private String newPasswordCheck;
    }
}
