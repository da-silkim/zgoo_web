package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.cp.CpConnector;
import zgoo.cpos.domain.cp.CpModel;
import zgoo.cpos.domain.cp.CpModelDetail;
import zgoo.cpos.dto.cp.CpModelDto;
import zgoo.cpos.dto.cp.CpModelDto.CpConnectorDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelRegDto;

public class CpModelMapper {

    /*
     * cp model(dto >> entity) 
     */
    public static CpModel toEntityModel(CpModelRegDto dto, Company company) {
        CpModel model = CpModel.builder()
                .company(company)
                .modelCode(dto.getModelCode())
                .modelName(dto.getModelName())
                .manufCd(dto.getManufCd())
                .powerUnit(dto.getPowerUnit())
                .installationType(dto.getInstallationType())
                .cpType(dto.getCpType())
                .dualYn(dto.getDualYn())
                .regDt(LocalDateTime.now())
                .userId(dto.getUserId())
                .build();
        return model;
    }

    /*
     * cp model detail(dto >> entity) 
     */
    public static CpModelDetail toEntityModelDetail(CpModelRegDto dto, CpModel model) {
        CpModelDetail modelDetail = CpModelDetail.builder()
                .cpModel(model)
                .inputVoltage(dto.getInputVoltage())
                .inputFrequency(dto.getInputFrequency())
                .inputType(dto.getInputType())
                .inputCurr(dto.getInputCurr())
                .inputPower(dto.getInputPower())
                .powerFactor(dto.getPowerFactor())
                .outputVoltage(dto.getOutputVoltage())
                .maxOutputCurr(dto.getMaxOutputCurr())
                .ratedPower(dto.getRatedPower())
                .peakEfficiency(dto.getPeakEfficiency())
                .thdi(dto.getThdi())
                .grdType(dto.getGrdType())
                .opAltitude(dto.getOpAltitude())
                .opTemperature(dto.getOpTemperature())
                .temperatureDerating(dto.getTemperatureDerating())
                .storageTemperatureRange(dto.getStorageTemperatureRange())
                .humidity(dto.getHumidity())
                .dimensions(dto.getDimensions())
                .ipNIk(dto.getIpNIk())
                .weight(dto.getWeight())
                .material(dto.getMaterial())
                .cableLength(dto.getCableLength())
                .screen(dto.getScreen())
                .rfid(dto.getRfid())
                .emergencyBtn(dto.getEmergencyBtn())
                .communicationInterface(dto.getCommunicationInterface())
                .lang(dto.getLang())
                .coolingMethod(dto.getCoolingMethod())
                .emc(dto.getEmc())
                .protection(dto.getProtection())
                .opFunc(dto.getOpFunc())
                .standard(dto.getStandard())
                .powerModule(dto.getPowerModule())
                .charger(dto.getCharger())
                .build();
        return modelDetail;
    }

    /*
     * cp connector(dto >> entity) 
     */
    public static CpConnector toEntityConnector(CpConnectorDto dto, CpModel model) {
        CpConnector connector = CpConnector.builder()
                .cpModel(model)
                .connectorId(dto.getConnectorId())
                .connectorType(dto.getConnectorType())
                .build();
        return connector;
    }
}
