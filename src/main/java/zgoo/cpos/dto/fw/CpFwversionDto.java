package zgoo.cpos.dto.fw;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CpFwversionDto {
    private Long fwId;
    private String companyName;
    private String manfName;
    private String modelCode;
    private String fwVersion;
    private String url;
    private String regUser;
    private LocalDateTime regDate;
}
