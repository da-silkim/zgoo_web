package zgoo.cpos.domain.cp;

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
import zgoo.cpos.dto.cp.CpModelDto.CpModelRegDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CpModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Long id;

    @Column(name = "model_code")
    private String modelCode;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "manuf_cd")
    private String manufCd;

    @Column(name = "power_unit")
    private Integer powerUnit;

    @Column(name = "installation_type")
    private String installationType;

    @Column(name = "cp_type")
    private String cpType;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    @Column(name = "user_id")
    private String userId;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    public void updateCpModelInfo(CpModelRegDto dto) {
        this.modelCode = dto.getModelCode();
        this.modelName = dto.getModelName();
        this.manufCd = dto.getManufCd();
        this.powerUnit = dto.getPowerUnit();
        this.installationType = dto.getInstallationType();
        this.cpType = dto.getCpType();
        this.modDt = LocalDateTime.now();
    }
}
