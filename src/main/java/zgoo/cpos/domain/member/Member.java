package zgoo.cpos.domain.member;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import zgoo.cpos.domain.biz.BizInfo;
import zgoo.cpos.dto.member.MemberDto.MemberRegDto;

@Table(name = "MEMBER", indexes = {
        @Index(name = "idx_member_login_id", columnList = "mem_login_id"),
        @Index(name = "idx_member_id_tag", columnList = "id_tag"),
        @Index(name = "idx_member_biz_id", columnList = "biz_id")
})
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "mem_login_id", unique = true, nullable = false)
    private String memLoginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "biz_type", nullable = false)
    private String bizType;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "id_tag", unique = true, nullable = false, length = 16)
    private String idTag;

    @Column(name = "email")
    private String email;

    @Column(name = "birth")
    private String birth;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "user_state")
    private String userState;

    @Column(name = "joined_dt")
    private LocalDateTime joinedDt;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    @Column(name = "creditcard_stat")
    private String creditcardStat;

    @Column(name = "login_dt")
    private LocalDateTime loginDt;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "company_id")
    // private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biz_id")
    private BizInfo biz;

    public void updateMemberInfo(MemberRegDto dto) {
        this.name = dto.getName();
        this.bizType = dto.getBizType();
        this.phoneNo = dto.getPhoneNo();
        this.idTag = dto.getIdTag();
        this.email = dto.getEmail();
        this.birth = dto.getBirth();
        this.zipCode = dto.getZipCode();
        this.address = dto.getAddress();
        this.addressDetail = dto.getAddressDetail();
        this.userState = dto.getUserState();
        this.modDt = LocalDateTime.now();
    }

    public void updateCreditStatInfo(String creditcardStat) {
        this.creditcardStat = creditcardStat;
    }

    public void updatePasswordInfo(String password) {
        this.password = password;
    }
}
