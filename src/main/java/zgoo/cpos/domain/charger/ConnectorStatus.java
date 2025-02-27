package zgoo.cpos.domain.charger;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zgoo.cpos.type.ChargePointErrorCode;
import zgoo.cpos.type.ChargePointStatus;

@Entity
@Table(name = "CONNECTOR_STATUS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class ConnectorStatus {
    @EmbeddedId // 복합키 지정
    private ConnectorStatusId id; // 임베디드된 복합키

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChargePointStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private ChargePointErrorCode errCode;

    @Column
    private String vendorId;

    @Column
    private String vendorErrCode;

    @Column
    private LocalDateTime timestamp;
}
