package zgoo.cpos.domain.cs;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoRegDto;

@Entity
@Table(name = "CS_KEPCO_CONTRACTINFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CsKepcoContractInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cskepcocontractinfo_id")
    private Long id;

    @Column(name = "kepco_cust_no")
    private String KepcoCustNo;

    @Column(name = "openingDate")
    private LocalDate openingDate;

    @Column(name = "cont_power")
    private Integer contPower;

    @Column(name = "rcv_capacity_method")
    private String rcvCapacityMethod;

    @Column(name = "rcv_capacity")
    private Integer rcvCapacity;

    @Column(name = "voltage_type")
    private String voltageType;

    public void updateCsKepcoContractInfo(CsInfoRegDto dto) {
        this.KepcoCustNo = dto.getKepcoCustNo();
        this.openingDate = dto.getOpeningDate();
        this.contPower = dto.getContPower();
        this.rcvCapacityMethod = dto.getRcvCapacityMethod();
        this.rcvCapacity = dto.getRcvCapacity();
        this.voltageType = dto.getVoltageType();
    }
}
