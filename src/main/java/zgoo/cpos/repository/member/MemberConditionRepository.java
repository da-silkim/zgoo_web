package zgoo.cpos.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.member.MemberCondition;

public interface MemberConditionRepository extends JpaRepository<MemberCondition, Long>, MemberConditionRepositoryCustom {

}
