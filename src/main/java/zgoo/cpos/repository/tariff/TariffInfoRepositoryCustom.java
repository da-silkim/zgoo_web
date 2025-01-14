package zgoo.cpos.repository.tariff;

import java.util.List;
import java.util.Optional;

import zgoo.cpos.domain.tariff.TariffInfo;

public interface TariffInfoRepositoryCustom {

    Optional<List<TariffInfo>> findTariffInfoListByTariffId(Long tariffId);
}
