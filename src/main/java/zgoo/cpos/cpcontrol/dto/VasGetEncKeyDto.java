package zgoo.cpos.cpcontrol.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VasGetEncKeyDto extends BaseDto {
    private String resultCode;
    private String resultTime;
    private int successCnt;
    private String errCode;
    private String errMsg;
    private int reqChargerCnt;
    private List<ChargerKeySet> chargerKeySet;

    @Data
    public static class ChargerKeySet {
        private String chargerId;
        private String keyId;
        private String encryptPub;
        private String signData;
        private String validTime;
        private String retVal;
    }
}
