package zgoo.cpos.repository.fw;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.fw.CpFwVersion;

public interface CpFwVersionRepository extends JpaRepository<CpFwVersion, Long>, CpFwVersionRepositoryCustom {

}
