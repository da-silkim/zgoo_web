package zgoo.cpos.domain.history;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zgoo.cpos.type.ChargePointErrorCode;

@Entity
@Table(name = "ERROR_HIST", indexes = {
        @Index(name = "idx_error_hist_charger_id", columnList = "chargerId"),
        @Index(name = "idx_error_hist_err_code", columnList = "errCode"),
        @Index(name = "idx_error_hist_occur_date", columnList = "occurDate"),
        @Index(name = "idx_error_hist_composite", columnList = "chargerId, errCode, occurDate")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorHist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long id;

    @Column(name = "charger_id")
    private String chargerId;

    @Column(name = "err_code")
    @Enumerated(EnumType.STRING)
    private ChargePointErrorCode errCode;

    @Column(name = "occur_date")
    private LocalDateTime occurDate;

    @Column(name = "connector_id")
    private Integer connectorId;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "vendor_error_code")
    private String vendorErrorCode;

    @Column(name = "error_name")
    private String errorName;
}
