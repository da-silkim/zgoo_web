package zgoo.cpos.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.member.MemberCar;

public interface MemberCarRepository extends JpaRepository<MemberCar, Long>, MemberCarRepositoryCustom {

}
