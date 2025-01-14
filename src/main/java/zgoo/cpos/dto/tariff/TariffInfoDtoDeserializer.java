package zgoo.cpos.dto.tariff;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import zgoo.cpos.dto.tariff.TariffDto.TariffInfoDto;

public class TariffInfoDtoDeserializer extends JsonDeserializer<List<TariffInfoDto>> {

    @Override
    public List<TariffInfoDto> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
        List<TariffInfoDto> tariffInfoList = new ArrayList<>();
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode rootNode = mapper.readTree(p);

        for (JsonNode node : rootNode) {
            TariffInfoDto infoDto = new TariffInfoDto();
            infoDto.setHour(node.get(0).asInt());
            infoDto.setMemSlowUnitCost(new BigDecimal(node.get(1).asText()));
            infoDto.setNomemSlowUnitCost(new BigDecimal(node.get(2).asText()));
            infoDto.setMemFastUnitCost(new BigDecimal(node.get(3).asText()));
            infoDto.setNomemFastUnitCost(new BigDecimal(node.get(4).asText()));
            tariffInfoList.add(infoDto);
        }

        return tariffInfoList;
    }

}
