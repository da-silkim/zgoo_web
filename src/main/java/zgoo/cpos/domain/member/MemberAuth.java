package zgoo.cpos.domain.member;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.member.MemberDto.MemberAuthDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class MemberAuth {

    @Id
    @Column(name = "id_tag")
    private String idTag;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "use_yn")
    private String useYn;

    @Column(name = "parent_id_tag")
    private String parentIdTag;

    @Column(name = "total_charging_power")
    private BigDecimal totalChargingPower;

    @Column(name = "status")
    private String status;

    @Column(name = "total_charging_price", precision = 12, scale = 3)
    private Long totalChargingPrice;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateMemberAuthInfo(MemberAuthDto dto) {
        this.useYn = dto.getUseYn();
        this.expireDate = dto.getExpireDate();
        this.parentIdTag = dto.getParentIdTag();
        this.status = dto.getStatus();
        this.modDt = LocalDateTime.now();
    }
}
