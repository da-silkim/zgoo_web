package zgoo.cpos.repository.biz;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.biz.BizInfo;

public interface BizRepository extends JpaRepository<BizInfo, Long>, BizRepositoryCustom {

}
