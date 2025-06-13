package zgoo.cpos.domain.member;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "MEMBER_AUTH_HIST", indexes = {
        @Index(name = "idx_member_auth_hist_charger_id", columnList = "charger_id"),
        @Index(name = "idx_member_auth_hist_id_tag", columnList = "id_tag"),
        @Index(name = "idx_member_auth_hist_auth_dt", columnList = "auth_dt")
})
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class MemberAuthHist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_member_auth_hist_member"))
    private Member member;

    @Column(name = "charger_id", length = 15)
    private String chargerId;

    @Column(name = "id_tag", length = 20)
    private String idTag;

    @Column(name = "auth_result", length = 10)
    private String authResult;

    @Column(name = "auth_dt")
    private LocalDateTime authDt;

}
