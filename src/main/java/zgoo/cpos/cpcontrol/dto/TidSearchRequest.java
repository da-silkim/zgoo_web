package zgoo.cpos.cpcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TidSearchRequest {
    private String tid;
    private String approvalDate;
}
