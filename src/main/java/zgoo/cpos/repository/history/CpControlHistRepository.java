package zgoo.cpos.repository.history;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.history.CpControlHist;

public interface CpControlHistRepository extends JpaRepository<CpControlHist, Long> {

}
