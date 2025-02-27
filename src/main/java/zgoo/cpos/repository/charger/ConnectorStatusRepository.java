package zgoo.cpos.repository.charger;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.charger.ConnectorStatus;
import zgoo.cpos.domain.charger.ConnectorStatusId;

public interface ConnectorStatusRepository
        extends JpaRepository<ConnectorStatus, ConnectorStatusId>, ConnectorStatusRepositoryCustom {

}
