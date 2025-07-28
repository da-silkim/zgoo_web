package zgoo.cpos.domain.history;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "CHG_COMMLOG", indexes = {
        @Index(name = "idx_chg_commlog_charger_id", columnList = "charger_id"),
        @Index(name = "idx_chg_commlog_action", columnList = "action")
})
@ToString
public class ChgCommLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long id;
    @Column(nullable = false)
    private String chargerId;
    @Column(nullable = false)
    private int connectorId;
    @Column(nullable = false)
    private String action;

    // Receiver -- 서버에서 메시지를 수신한 내용==================================
    private Timestamp recvTime;
    private String recvUuid; // uuid
    @Column(columnDefinition = "TEXT")
    private String recvPayload;

    // Sender -- 서버에서 메시지를 송신한 내용================================
    private Timestamp sendTime;
    private String sendUuid; // uuid
    @Column(columnDefinition = "TEXT")
    private String sendPayload;

}
