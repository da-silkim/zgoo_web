package zgoo.cpos.dto;

import java.time.LocalDateTime;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zgoo.cpos.type.CommonCodeKey;

@Getter
@Setter
@ToString
@NoArgsConstructor
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

}
