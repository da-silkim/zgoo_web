package zgoo.cpos.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.type.CommonCodeKey;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class CommonCode {

    @EmbeddedId
    private CommonCodeKey id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "reserved", length = 100)
    private String reserved;

    @Column(name = "ref_code1", length = 10)
    private String refCode1;

    @Column(name = "ref_code2", length = 10)
    private String refCode2;

    @Column(name = "ref_code3", length = 10)
    private String refCode3;

    @Column(name = "reg_user_id", length = 20)
    private String regUserId;

    @CreatedDate
    private LocalDateTime regDt;

    @Column(name = "mod_user_id", length = 20)
    private String modUserId;

    @LastModifiedDate
    private LocalDateTime modDt;

    @JoinColumn(name = "grp_code")
    @MapsId("grpCode")
    @ManyToOne(fetch = FetchType.LAZY)
    private GrpCode group;

    @Builder
    protected CommonCode(CommonCodeKey id, String name, String reserved, String refCode1, String refCode2,
            String refCode3,
            String regUserId, LocalDateTime regDt, String modUserId, LocalDateTime modDt, GrpCode grpCode) {
        this.id = id;
        this.name = name;
        this.reserved = reserved;
        this.refCode1 = refCode1;
        this.refCode2 = refCode2;
        this.refCode3 = refCode3;
        this.regUserId = regUserId;
        this.regDt = regDt;
        this.modUserId = modUserId;
        this.modDt = modDt;
        this.group = grpCode;
    }

}
