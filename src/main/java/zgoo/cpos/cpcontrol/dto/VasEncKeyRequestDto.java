package zgoo.cpos.cpcontrol.dto;

import lombok.Data;

@Data
public class VasEncKeyRequestDto {
    private String bid;
    private String bkey;
    private String chargerId;
    private int chargerCnt;
}
