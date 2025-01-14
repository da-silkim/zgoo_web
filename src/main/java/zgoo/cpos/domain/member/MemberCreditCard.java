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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.member.MemberDto.MemberCreditCardDto;

@Table(name = "MEMBER_CREDITCARD")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class MemberCreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memcreditcard_id")
    private Long id;

    @Column(name = "tid")
    private String tid;

    @Column(name = "card_num")
    private String cardNum;

    @Column(name = "fn_code")
    private String fnCode;

    @Column(name = "representative_card")
    private String representativeCard;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateMemberCreditCardInfo(MemberCreditCardDto dto) {
        this.tid = dto.getTid();
        this.cardNum = dto.getCardNum();
        this.fnCode = dto.getFnCode();
        this.representativeCard = dto.getRepresentativeCard();
    }
}
