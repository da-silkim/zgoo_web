package zgoo.cpos.repository.tariff;

import java.util.List;
import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.tariff.QTariffInfo;
import zgoo.cpos.domain.tariff.TariffInfo;

@RequiredArgsConstructor
@Slf4j
public class TariffInfoRepositoryCustomImpl implements TariffInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QTariffInfo tinfo = QTariffInfo.tariffInfo;

    @Override
    public Optional<List<TariffInfo>> findTariffInfoListByTariffId(Long tariffId) {
        List<TariffInfo> result = queryFactory.selectFrom(tinfo).where(tinfo.tariffPolicy.id.eq(tariffId)).fetch();

        return Optional.ofNullable(result);
    }

}
