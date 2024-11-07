package zgoo.cpos.dto.company;

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
public class CompanyDto {

    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class BaseCompnayDto {
        private Long companyId;
        private String companyName;
        private String companyLv; // 사업자구분
        private String companyType; // 사업자유형
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CompanyListDto extends BaseCompnayDto {

        private Long parentId; // 상위사업자ID
        private LocalDateTime contractedAt; // 서비스가입일
        private LocalDateTime contractEnd; // 가입종료일
        private String contractStatus; // 계약상태
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CompanyRegDto extends BaseCompnayDto {
        // 사업자 정보
        private String bizNum; // 사업자번호
        private String ceoName; // 대표자명
        private String headPhone; // 대표전화
        private String bizKind; // 업종
        private String bizType; // 사업자구분(법인/개인)
        private String zipcode;
        private String address;
        private String addressDetail;
        private String consignmentPayment; // 위탁결제구분

        // 사업자 관계정보
        private Long parentId;

        // 담당자 정보
        private String staffName; // 담당자명
        private String staffEmail;
        private String staffTel;
        private String staffPhone;

        // 로밍정보
        private List<CompanyRoamingtDto> romaing;

        // PG 결제정보
        private String mid;
        private String merchantKey;
        private String sspMallId;

        // 계약정보
        private String contractStatus;
        private LocalDateTime contractedAt;
        private LocalDateTime contractStart;
        private LocalDateTime contractEnd;
        private String asCompany;
        private String asNum;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CompanyRoamingtDto {
        private String institutionCode;
        private String institutionKey;
        private String institutionEmail;
    }

}