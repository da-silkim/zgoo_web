package zgoo.cpos.domain.cp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.cp.CpModelDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelRegDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CpModelDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_detail_id")
    private Long id;

    @Column(name = "input_voltage")
    private String inputVoltage;

    @Column(name = "input_frequency")
    private String inputFrequency;

    @Column(name = "input_type")
    private String inputType;

    @Column(name = "input_curr")
    private String inputCurr;

    @Column(name = "input_power")
    private String inputPower;

    @Column(name = "power_factor")
    private Double powerFactor;

    @Column(name = "output_voltage")
    private String outputVoltage;

    @Column(name = "max_output_curr")
    private String maxOutputCurr;

    @Column(name = "rated_power")
    private String ratedPower;

    @Column(name = "peak_effciency")
    private Integer peakEfficiency;

    @Column(name = "thdi")
    private String thdi;

    @Column(name = "grd_type")
    private String grdType;

    @Column(name = "op_altitude")
    private String opAltitude;

    @Column(name = "op_temperature")
    private String opTemperature;

    @Column(name = "temperature_derating")
    private String temperatureDerating;

    @Column(name = "storage_temperature_range")
    private String storageTemperatureRange;

    @Column(name = "humidity")
    private String humidity;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "ip_n_ik")
    private String ipNIk;

    @Column(name = "weight")
    private String weight;

    @Column(name = "material")
    private String material;

    @Column(name = "cable_length")
    private String cableLength;

    @Column(name = "screen")
    private String screen;

    @Column(name = "rfid")
    private String rfid;

    @Column(name = "emergency_btn")
    private String emergencyBtn;

    @Column(name = "communication_interface")
    private String communicationInterface;

    @Column(name = "lang")
    private String lang;

    @Column(name = "cooling_method")
    private String coolingMethod;

    @Column(name = "emc")
    private String emc;

    @Column(name = "protection")
    private String protection;

    @Column(name = "op_func")
    private String opFunc;

    @Column(name = "standard")
    private String standard;

    @Column(name = "power_module")
    private String powerModule;

    @Column(name = "charger")
    private String charger;

    @JoinColumn(name = "model_id")
    @OneToOne(fetch = FetchType.LAZY)
    private CpModel cpModel;

    public void updateCpModelDetailInfo(CpModelRegDto dto) {
        this.inputVoltage = dto.getInputVoltage();
        this.inputFrequency = dto.getInputFrequency();
        this.inputType = dto.getInputType();
        this.inputCurr = dto.getInputCurr();
        this.inputPower = dto.getInputPower();
        this.powerFactor = dto.getPowerFactor();
        this.outputVoltage = dto.getOutputVoltage();
        this.maxOutputCurr = dto.getMaxOutputCurr();
        this.ratedPower = dto.getRatedPower();
        this.peakEfficiency = dto.getPeakEfficiency();
        this.thdi = dto.getThdi();
        this.grdType = dto.getGrdType();
        this.opAltitude = dto.getOpAltitude();
        this.opTemperature = dto.getOpTemperature();
        this.temperatureDerating = dto.getTemperatureDerating();
        this.storageTemperatureRange = dto.getStorageTemperatureRange();
        this.humidity = dto.getHumidity();
        this.dimensions = dto.getDimensions();
        this.ipNIk = dto.getIpNIk();
        this.weight = dto.getWeight();
        this.material = dto.getMaterial();
        this.cableLength = dto.getCableLength();
        this.screen = dto.getScreen();
        this.rfid = dto.getRfid();
        this.emergencyBtn = dto.getEmergencyBtn();
        this.communicationInterface = dto.getCommunicationInterface();
        this.lang = dto.getLang();
        this.coolingMethod = dto.getCoolingMethod();
        this.emc = dto.getEmc();
        this.protection = dto.getProtection();
        this.opFunc = dto.getOpFunc();
        this.standard = dto.getStandard();
        this.powerModule = dto.getPowerModule();
        this.charger = dto.getCharger();
    }
}
