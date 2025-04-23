package zgoo.cpos.mapper;

import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.domain.cs.CsKepcoContractInfo;
import zgoo.cpos.domain.cs.CsLandInfo;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoRegDto;

public class CsMapper {

    /*
     * cs(dto >> entity) 
     */
    public static CsInfo toEntityCs(CsInfoRegDto dto, String stationId, Company company, CsLandInfo landInfo, CsKepcoContractInfo kepcoInfo) {
        CsInfo cs = CsInfo.builder()
                .id(stationId)
                .company(company)
                .csLandInfo(landInfo)
                .csKepcoContractInfo(kepcoInfo)
                .stationName(dto.getStationName())
                .stationType(dto.getStationType())
                .facilityType(dto.getFacilityType())
                .asNum(dto.getAsNum())
                .opStatus(dto.getOpStatus())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .zipcode(dto.getZipcode())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .openStartTime(dto.getOpenStartTime())
                .openEndTime(dto.getOpenEndTime())
                .parkingFeeYn(dto.getParkingFeeYn())
                .sido(dto.getSido())
                .safetyManagementFee(dto.getSafetyManagementFee())
                .build();
        return cs;
    }

    /*
     * land(dto >> entity) 
     */
    public static CsLandInfo toEntityLand(CsInfoRegDto dto) {
        CsLandInfo land = CsLandInfo.builder()
                .institutionName(dto.getInstitutionName())
                .landType(dto.getLandType())
                .staffName(dto.getStaffName())
                .staffPhone(dto.getStaffPhone())
                .contractDate(dto.getContractDate())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .landUseType(dto.getLandUseType())
                .landUseFee(dto.getLandUseFee())
                .settlementDate(dto.getSettlementDate())
                .build();
        return land;
    }

    /*
     * kepco(dto >> entity)
     */
    public static CsKepcoContractInfo toEntityKepco(CsInfoRegDto dto) {
        CsKepcoContractInfo kepco = CsKepcoContractInfo.builder()
                .KepcoCustNo(dto.getKepcoCustNo())
                .openingDate(dto.getOpeningDate())
                .contPower(dto.getContPower())
                .rcvCapacityMethod(dto.getRcvCapacityMethod())
                .rcvCapacity(dto.getRcvCapacity())
                .voltageType(dto.getVoltageType())
                .build();
        return kepco;
    }
}
