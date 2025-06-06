package zgoo.cpos.domain.charger;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zgoo.cpos.type.ConnectionStatus;

@Entity
@Table(name = "CP_STATUS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class CpStatus {

    @Id
    @Column(name = "charger_id")
    private String chargerId;

    @Column(name = "connection_yn", columnDefinition = "CHAR(1)")
    @Enumerated(EnumType.STRING)
    private ConnectionStatus connectionYn;

    @Column(name = "last_boot_time")
    private LocalDateTime lastBootTime;

    @Column(name = "last_heartbeat_rcv_time")
    private LocalDateTime lastHeartbeatRcvTime;

    @Column(name = "last_fwupdate_time")
    private LocalDateTime lastFwupdateTime;

    public void updateLastFwUpdateTime(LocalDateTime updateTime) {
        this.lastFwupdateTime = updateTime;
    }
}
