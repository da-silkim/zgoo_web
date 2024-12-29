package zgoo.cpos.domain.cp;

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
import zgoo.cpos.dto.cp.CpModelDto.CpConnectorDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CpConnector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cpconnector_id")
    private Long id;

    @Column(name = "connector_id")
    private Integer connectorId;

    @Column(name = "connector_type")
    private String connectorType;

    @JoinColumn(name = "model_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CpModel cpModel;

    public void updateCpConnectorInfo(CpConnectorDto dto) {
        this.connectorId = dto.getConnectorId();
        this.connectorType = dto.getConnectorType();
    }
}
