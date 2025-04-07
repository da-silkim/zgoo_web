package zgoo.cpos.repository.history;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.history.ChgCommLog;

public interface ChgCommLogRepository extends JpaRepository<ChgCommLog, Long>, ChgCommLogRepositoryCustom {

}
