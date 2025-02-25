package zgoo.cpos.repository.cp;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.cp.CpMaintain;

public interface CpMaintainRepository extends JpaRepository<CpMaintain, Long>, CpMaintainRepositoryCustom {

}
