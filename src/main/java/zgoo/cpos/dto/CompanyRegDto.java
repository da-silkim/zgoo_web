package zgoo.cpos.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * 1. controller와 service 사이의 data 전달객체
 * 2. 엔티티에에서는 핵심 비지니스 로직만 처리하고 화면 구성을 위한 로직은 해당 클래스에서 진행
 * 3. 변수명은 엔티티와 동일하게
 */
@Getter
@Setter
@ToString
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegDto {
    @NotEmpty(message = "사업자 이름을 입력해 주세요.")
    private String companyName; // 사업자명
    @NotEmpty(message = "사업자 유형(위탁운영사/충전사업자)을 선택해주세요.")
    private String companyType; // 사업자유형
    @NotEmpty(message = "사업자 계층을 선택해주세요.")
    private String companyLv; // 사업자레벨
    private String bizNum; // 사업자번호
    @NotEmpty(message = "사업자 구분(법인/개인)을 선택해주세요.")
    private String bizType; // 사업자구분(법인/개인)
    private String bizKind; // 업종
    private String ceoName; // 대표자명
    private String headPhone; // 대표전화
    private String zipcode;
    private String address;
    private String addressDetail;
    private String staffName; // 담당자명
    private String staffEmail;
    private String staffTel;
    private String staffPhone;
    private String consignmentPayment; // 위탁결제구분

    // companyRelationinfo 관련
    private String parentId;

}
