package zgoo.cpos.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.member.ConditionCode;

public interface ConditionCodeRepository extends JpaRepository<ConditionCode, String>, ConditionCodeRepositoryCustom {

}
