package zgoo.cpos.repository.charger;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.charger.CpModem;

public interface CpModemRepository extends JpaRepository<CpModem, Long>, CpModemRepositoryCustom {

}
