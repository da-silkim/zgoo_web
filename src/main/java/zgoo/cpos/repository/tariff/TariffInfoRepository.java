package zgoo.cpos.repository.tariff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import zgoo.cpos.domain.tariff.TariffInfo;

public interface TariffInfoRepository extends JpaRepository<TariffInfo, Long>, TariffInfoRepositoryCustom {

    @Modifying
    @Query("DELETE FROM TariffInfo ti WHERE ti.tariffPolicy.id = :tariffId")
    void deleteTariffInfoByTariffId(@Param("tariffId") Long tariffId);
}
