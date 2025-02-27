package zgoo.cpos.repository.charger;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.charger.CpModem;
import zgoo.cpos.domain.charger.QCpModem;

@RequiredArgsConstructor
@Slf4j
public class CpModemRepositoryCustomImpl implements CpModemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QCpModem modem = QCpModem.cpModem;

    @Override
    public boolean isModemSerialDuplicate(String serialnum) {

        CpModem result = queryFactory.selectFrom(modem)
                .where(modem.serialNo.eq(serialnum))
                .limit(1)
                .fetchOne();

        return result != null;
    }

    @Override
    public boolean isModemNumDuplicate(String modemNum) {
        CpModem result = queryFactory.selectFrom(modem)
                .where(modem.modemNo.eq(modemNum))
                .limit(1)
                .fetchOne();

        return result != null;
    }

}
