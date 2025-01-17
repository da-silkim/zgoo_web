package zgoo.cpos.dto.cp;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import zgoo.cpos.dto.tariff.TariffDto.TariffInfoDto;
import zgoo.cpos.dto.tariff.TariffInfoDtoDeserializer;

@Data
@SuperBuilder
public class ChargerDto {




    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChargerRegDto {
        //충전소 정보
        private Long companyId;
        private String stationName;
        private String stationId;

        //충전기 정보
        private String chargerName;         //충전기 이름
        private String chargerId;           //충전기 ID
        private String modelCode;           //충전기 모델코드드
        


        //모뎀정보



        private Long tariffId;
        @NotBlank(message = "요금제 명을 입력해 주세요.")
        private String policyName;
        @FutureOrPresent(message = "현재 혹은 미래시간만 가능합니다.")
        private LocalDate applyStartDate; // 적용시작일자

        private String applyCode; // 적용상태 >> 적용상태는 Mapper에서 적용시작/마감일자 계산해서 코드 적용

        @JsonDeserialize(using = TariffInfoDtoDeserializer.class)
        List<TariffInfoDto> tariffInfo;
    }

}
