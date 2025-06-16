package zgoo.cpos.dto.member;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MemberAuthHistDto {
    private String stationName;
    private String chargerId;
    private String cpType;
    private String memberName;
    private String memberType;
    private String idTag;
    private String result;
    private LocalDateTime authTime;
}
