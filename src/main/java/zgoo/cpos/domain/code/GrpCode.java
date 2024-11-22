package zgoo.cpos.domain.code;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.code.CodeDto.GrpCodeDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class GrpCode {
    @Id
    @Column(name = "grp_code", length = 20)
    private String grpCode;

    @Column(name = "grpcd_name", length = 50)
    private String grpcdName;

    @Column(name = "reg_user_id", length = 20)
    private String regUserId;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "mod_user_id", length = 20)
    private String modUserId;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    // @Builder
    // protected GrpCode(String grpCode, String grpcdName, String regUserId,
    // LocalDateTime regDt, String modUserId,
    // LocalDateTime modDt) {
    // this.grpCode = grpCode;
    // this.grpcdName = grpcdName;
    // this.regUserId = regUserId;
    // this.regDt = regDt;
    // this.modUserId = modUserId;
    // this.modDt = modDt;
    // }

    public void updateGrpcdCode(GrpCodeDto grpcode) {
        this.grpcdName = grpcode.getGrpcdName();
        this.modUserId = grpcode.getModUserId();
        this.modDt = LocalDateTime.now();
    }
}
