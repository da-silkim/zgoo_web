package zgoo.cpos.dto;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CompanyLvInfoDto {
    private Long companyId;
    private String companyName;
}
