package zgoo.cpos.cpcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDto {
    private String chargerId;
    private String userId;
    private String userName;

}
