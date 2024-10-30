package zgoo.cpos.dto.company;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CompanyListDto {
    private Long companyId;
    private String companyName;
    private String companyType;
    private String companyLv;
    private String bizNum;
    private String bizType;
    private String consignmentPayment;
    private Long parentCompanyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
