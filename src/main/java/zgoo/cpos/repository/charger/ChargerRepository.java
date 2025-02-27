package zgoo.cpos.repository.charger;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.charger.CpInfo;

public interface ChargerRepository extends JpaRepository<CpInfo, String>, ChargerRepositoryCustom {

}
