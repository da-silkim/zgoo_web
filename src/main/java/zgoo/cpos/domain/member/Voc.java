package zgoo.cpos.domain.member;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.member.VocDto.VocRegDto;
import zgoo.cpos.domain.member.Member;

@Table(name = "VOC")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Voc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voc_id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "regDt")
    private LocalDateTime regDt;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "del_yn")
    private String delYn;

    @Column(name = "reply_stat")
    private String replyStat;

    @Column(name = "reply_dt")
    private LocalDateTime replyDt;

    @Lob
    @Column(name = "reply_content")
    private String replyContent;

    @Column(name = "channel")
    private String channel;

    @Column(name = "reg_user_id")
    private String regUserId;

    @Column(name = "reply_user_id")
    private String replyUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateVocAnswer(VocRegDto dto) {
        this.replyStat = dto.getReplyStat();
        this.replyContent = dto.getReplyContent();
        this.replyUserId = dto.getReplyUserId();
        this.replyDt = LocalDateTime.now();
    }
}
