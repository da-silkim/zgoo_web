package zgoo.cpos.repository.charger;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.charger.KeyValue;
import zgoo.cpos.domain.charger.KeyValueId;

public interface KeyValueRepository extends JpaRepository<KeyValue, KeyValueId> {

}
