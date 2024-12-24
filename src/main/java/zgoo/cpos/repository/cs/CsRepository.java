package zgoo.cpos.repository.cs;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.cs.CsInfo;

public interface CsRepository extends JpaRepository<CsInfo, String>, CsRepositoryCustom {

}
