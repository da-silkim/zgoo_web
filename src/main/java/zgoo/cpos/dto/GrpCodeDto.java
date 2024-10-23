package zgoo.cpos.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GrpCodeDto {
    private String grpCode;
    private String grpcdName;
    private String regUserId;
    private LocalDateTime regDt;
    private String modUserId;
    private LocalDateTime modDt;
}
