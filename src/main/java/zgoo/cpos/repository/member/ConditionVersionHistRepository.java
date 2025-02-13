package zgoo.cpos.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.member.ConditionVersionHist;

public interface ConditionVersionHistRepository extends JpaRepository<ConditionVersionHist, Long>, ConditionVersionHistRepositoryCustom {

}
