package zgoo.cpos.domain.biz;

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
import zgoo.cpos.domain.company.Company;
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

    @Column(name = "tid")
    private String tid;

    @Column(name = "card_num")
    private String cardNum;

    @Column(name = "fn_code")
    private String fnCode;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    public void updateBizInfo(BizInfoRegDto dto) {
        this.bizNo = dto.getBizNo();
        this.bizName = dto.getBizName();
        this.tid = dto.getTid();
        this.cardNum = dto.getCardNum();
        this.fnCode = dto.getFnCode();
    }
}
