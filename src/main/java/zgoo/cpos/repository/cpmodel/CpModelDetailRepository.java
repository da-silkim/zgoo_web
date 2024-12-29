package zgoo.cpos.repository.cpmodel;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.cp.CpModelDetail;


public interface CpModelDetailRepository extends JpaRepository<CpModelDetail, Long>, CpModelDetailRepositoryCustom {

}
