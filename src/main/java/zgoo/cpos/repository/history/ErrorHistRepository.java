package zgoo.cpos.repository.history;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.history.ErrorHist;

public interface ErrorHistRepository extends JpaRepository<ErrorHist, Long>, ErrorHistRepositoryCustom {

}
