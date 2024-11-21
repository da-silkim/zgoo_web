package zgoo.cpos.type;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("AD"),
    USER("NO"),
    ASMANAGER("AS"),
    SUPERADMIN("SU");

    UserRole (String value) {
        this.value = value;
    }

    private String value;
}
