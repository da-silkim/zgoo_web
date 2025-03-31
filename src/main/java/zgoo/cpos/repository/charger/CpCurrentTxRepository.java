package zgoo.cpos.repository.charger;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.charger.CpCurrentTx;
import zgoo.cpos.domain.charger.CpCurrentTxId;

public interface CpCurrentTxRepository extends JpaRepository<CpCurrentTx, CpCurrentTxId>, CpCurrentTxRepositoryCustom {

}
