package zgoo.cpos.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.member.MemberAuthHist;

public interface MemberAuthHistRepository extends JpaRepository<MemberAuthHist, Long>, MemberAuthHistRepositoryCusotm {

}
