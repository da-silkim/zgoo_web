package zgoo.cpos.domain.users;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class LoginHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loginhist_id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "login_date")
    private LocalDateTime loginDate;

    @Column(name = "logout_date")
    private LocalDateTime logoutDate;

    public void updateLogoutDate() {
        this.logoutDate = LocalDateTime.now();
    }
}
