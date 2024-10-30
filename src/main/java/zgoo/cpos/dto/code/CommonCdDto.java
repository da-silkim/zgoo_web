package zgoo.cpos.dto.code;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zgoo.cpos.domain.GrpCode;
import zgoo.cpos.type.CommonCodeKey;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonCdDto {
    private CommonCodeKey id;
    private String name;
    private String reserved;
    private String refCode1;
    private String refCode2;
    private String refCode3;
    private String regUserId;
    private LocalDateTime regDt;
    private String modUserId;
    private LocalDateTime modDt;
    private GrpCode group;

}
