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
import zgoo.cpos.util.LocaleUtil;

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

    @Column(name = "grpcd_name_en", length = 100)
    private String grpcdNameEn;

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
        this.grpcdNameEn = grpcode.getGrpcdNameEn();
        this.modUserId = grpcode.getModUserId();
        this.modDt = LocalDateTime.now();
    }

    /**
     * 현재 로케일에 따라 적절한 그룹코드 이름을 반환
     * 
     * @return 로케일별 그룹코드 이름
     */
    public String getLocalizedGrpcdName() {
        // 한국어는 기존 grpcdName 사용, 영어는 grpcdNameEn 사용
        if (LocaleUtil.isEnglish() && this.grpcdNameEn != null && !this.grpcdNameEn.trim().isEmpty()) {
            return this.grpcdNameEn;
        }
        return this.grpcdName; // 기본값은 한국어 (기존 컬럼)
    }
}
