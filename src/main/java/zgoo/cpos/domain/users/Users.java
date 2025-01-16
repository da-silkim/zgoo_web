package zgoo.cpos.domain.users;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.dto.users.UsersDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@lombok.Builder(toBuilder = true)
@AllArgsConstructor
public class Users {
    @Id
    @Column(name = "user_id", length = 10, nullable=false)
    private String userId;

    @Column(name = "name", length = 20, nullable=false)
    private String name;

    @Column(name = "password", length = 64, nullable=false)
    private String password;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "authority", length = 2)
    private String authority;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public void updateUsersinfo(UsersDto.UsersRegDto user) {
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.authority = user.getAuthority();
    }

    public void updatePasswordInfo(String password) {
        this.password = password;
    }
}
