package zgoo.cpos.dto.users;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UsersDto {
    
    @Data
    @SuperBuilder
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class BaseUsersDto {
        private String userId;
        private String name;
        private String password;
        private String phone;
        private String email;
        private String authority;
        private String authorityName;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class UsersListDto extends BaseUsersDto {
        private Long companyId;
        private String companyName;
        private String companyType;
        private String companyTypeName;
        private LocalDateTime regDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class UsersRegDto extends BaseUsersDto {
        private Long companyId;
        private LocalDateTime regDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UsersPasswordDto {
        private String existPassword;
        private String newPassword;
        private String newPasswordCheck;
    }
}
