package zgoo.cpos.domain.biz;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class BizInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biz_id")
    private Long id;

    @Column(name = "biz_no", unique = true)
    private String bizNo;

    @Column(name = "biz_name")
    private String bizName;

    @Column(name = "bid")
    private String bid;

    @Column(name = "card_num")
    private String cardNum;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "card_code")
    private String cardCode;

    @Column(name = "auth_date")
    private String authDate;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    public void updateBizInfo(BizInfoRegDto dto) {
        this.bizNo = dto.getBizNo();
        this.bizName = dto.getBizName();
        this.bid = dto.getBid();
        this.cardNum = dto.getCardNum();
        this.cardCode = dto.getCardCode();
        this.cardName = dto.getCardName();
        this.authDate = dto.getAuthDate();
    }
}
