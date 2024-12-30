package zgoo.cpos.repository.code;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.code.ChgErrorCode;

public interface ChgErrorCodeRepository extends JpaRepository<ChgErrorCode, Long>, ChgErrorCodeRepositoryCustom {

}
