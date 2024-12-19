package zgoo.cpos.dto.users;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class LoginHistDto {

    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class LoginHistBaseDto {
        private String userId;
        private LocalDateTime loginDate;
        private LocalDateTime logoutDate;
    }
}
