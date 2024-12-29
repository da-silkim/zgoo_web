package zgoo.cpos.repository.cpmodel;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.cp.CpModel;

public interface CpModelRepository extends JpaRepository<CpModel, Long>, CpModelRepositoryCustom {

}
