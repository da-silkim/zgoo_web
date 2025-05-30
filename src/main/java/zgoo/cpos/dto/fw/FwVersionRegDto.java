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
public class FwVersionRegDto {
    private Long companyId;
    private String cpModelCode;
    private String version;
    private String url;
    private String regUser;
    private LocalDateTime regDate;
}
