package zgoo.cpos.domain.cs;

import java.time.LocalTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoRegDto;

@Entity
@Table(name = "CS_INFO", indexes = {
        @Index(name = "idx_csinfo_company_id", columnList = "company_id"),
        @Index(name = "idx_csinfo_sido", columnList = "sido"),
        @Index(name = "idx_csinfo_location", columnList = "latitude,longitude")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CsInfo {

    @Id
    @Column(name = "station_id")
    private String id;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "station_type")
    private String stationType;

    @Column(name = "facility_type")
    private String facilityType;

    @Column(name = "as_num")
    private String asNum;

    @Column(name = "op_status")
    private String opStatus;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "open_start_time")
    private LocalTime openStartTime;

    @Column(name = "open_end_time")
    private LocalTime openEndTime;

    @Column(name = "parking_fee_yn")
    private String parkingFeeYn;

    @Column(name = "sido")
    private String sido;

    @Column(name = "safety_management_fee")
    private Integer safetyManagementFee;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @JoinColumn(name = "cslandinfo_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CsLandInfo csLandInfo;

    @JoinColumn(name = "cskepcocontractinfo_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CsKepcoContractInfo csKepcoContractInfo;

    public void updateCsInfo(CsInfoRegDto dto) {
        this.stationName = dto.getStationName();
        this.stationType = dto.getStationType();
        this.facilityType = dto.getFacilityType();
        this.asNum = dto.getAsNum();
        this.opStatus = dto.getOpStatus();
        this.latitude = dto.getLatitude();
        this.longitude = dto.getLongitude();
        this.zipcode = dto.getZipcode();
        this.address = dto.getAddress();
        this.addressDetail = dto.getAddressDetail();
        this.openStartTime = dto.getOpenStartTime();
        this.openEndTime = dto.getOpenEndTime();
        this.parkingFeeYn = dto.getParkingFeeYn();
        this.sido = dto.getSido();
        this.safetyManagementFee = dto.getSafetyManagementFee();
    }
}
