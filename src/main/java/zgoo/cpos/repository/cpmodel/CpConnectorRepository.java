package zgoo.cpos.repository.cpmodel;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.cp.CpConnector;

public interface CpConnectorRepository extends JpaRepository<CpConnector, Long>, CpConnectorRepositoryCustom {

}
