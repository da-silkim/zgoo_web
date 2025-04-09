package zgoo.cpos.repository.charger;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import zgoo.cpos.domain.charger.CpStatus;

public interface CpStatusRepository extends JpaRepository<CpStatus, String> {

    @Query("SELECT c FROM CpStatus c WHERE c.chargerId = :chargerId")
    Optional<CpStatus> findByChargerId(@Param("chargerId") String chargerId);

}
