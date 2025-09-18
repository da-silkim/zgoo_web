package zgoo.cpos.domain.code;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import zgoo.cpos.dto.code.CodeDto.CommCodeDto;
import zgoo.cpos.util.LocaleUtil;

@Entity
@Table(name = "COMMON_CODE", indexes = {
        @Index(name = "idx_common_code_grp", columnList = "grp_code")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CommonCode {

    @Id
    @Column(name = "common_code")
    private String commonCode;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "name_en", length = 100)
    private String nameEn;

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

    private LocalDateTime regDt;

    @Column(name = "mod_user_id", length = 20)
    private String modUserId;

    private LocalDateTime modDt;

    @JoinColumn(name = "grp_code")
    @ManyToOne(fetch = FetchType.LAZY)
    private GrpCode group;

    public void updateCommonCodeName(CommCodeDto cdto) {
        this.name = cdto.getCommonCodeName();
        this.nameEn = cdto.getCommonCodeNameEn();
        this.modUserId = cdto.getModUserId();
        this.modDt = LocalDateTime.now();
    }

    /**
     * 현재 로케일에 따라 적절한 이름을 반환
     * 
     * @return 로케일별 이름
     */
    public String getLocalizedName() {
        // 한국어는 기존 name 사용, 영어는 nameEn 사용
        if (LocaleUtil.isEnglish() && this.nameEn != null && !this.nameEn.trim().isEmpty()) {
            return this.nameEn;
        }
        return this.name; // 기본값은 한국어 (기존 컬럼)
    }
}
