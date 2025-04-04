package zgoo.cpos.repository.history;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.history.ChargingHist;

public interface ChargingHistRepository extends JpaRepository<ChargingHist, Long>, ChargingHistRepositoryCustom {

}
