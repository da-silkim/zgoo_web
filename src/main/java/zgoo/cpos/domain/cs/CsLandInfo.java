package zgoo.cpos.domain.cs;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoRegDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CsLandInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cslandinfo_id")
    private Long id;

    @Column(name = "institution_name")
    private String institutionName;

    @Column(name = "land_type")
    private String landType;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "staff_phone")
    private String staffPhone;

    @Column(name = "contract_date")
    private LocalDate contractDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "land_use_type")
    private String landUseType;

    @Column(name = "land_use_fee")
    private Integer landUseFee;


    public void updateCsLandInfo(CsInfoRegDto dto) {
        this.institutionName = dto.getInstitutionName();
        this.landType = dto.getLandType();
        this.staffName = dto.getStaffName();
        this.staffPhone = dto.getStaffPhone();
        this.contractDate = dto.getContractDate();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.landUseType = dto.getLandUseType();
        this.landUseFee = dto.getLandUseFee();
    }
}
