package zgoo.cpos.domain.member;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.member.MemberDto.MemberCarDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class MemberCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memcar_id")
    private Long id;

    @Column(name = "car_num")
    private String carNum;

    @Column(name = "car_type")
    private String carType;

    @Column(name = "model")
    private String model;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateMemberCarInfo(MemberCarDto dto) {
        this.carNum = dto.getCarNum();
        this.carType = dto.getCarType();
        this.model = dto.getModel();
    }
}
