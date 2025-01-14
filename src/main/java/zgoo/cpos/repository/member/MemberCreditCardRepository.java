package zgoo.cpos.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.member.MemberCreditCard;

public interface MemberCreditCardRepository extends JpaRepository<MemberCreditCard, Long>, MemberCreditCardRepositoryCustom {

}
